package org.cocos2dx.okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public final class GzipSource implements Source {
    private static final byte FCOMMENT = 4;
    private static final byte FEXTRA = 2;
    private static final byte FHCRC = 1;
    private static final byte FNAME = 3;
    private static final byte SECTION_BODY = 1;
    private static final byte SECTION_DONE = 3;
    private static final byte SECTION_HEADER = 0;
    private static final byte SECTION_TRAILER = 2;
    private final Inflater inflater;
    private final InflaterSource inflaterSource;
    private final BufferedSource source;
    private int section = 0;
    private final CRC32 crc = new CRC32();

    public GzipSource(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        Inflater inflater = new Inflater(true);
        this.inflater = inflater;
        BufferedSource bufferedSourceBuffer = Okio.buffer(source);
        this.source = bufferedSourceBuffer;
        this.inflaterSource = new InflaterSource(bufferedSourceBuffer, inflater);
    }

    private void checkEqual(String str, int i, int i2) throws IOException {
        if (i2 != i) {
            throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", str, Integer.valueOf(i2), Integer.valueOf(i)));
        }
    }

    private void consumeHeader() throws IOException {
        this.source.require(10L);
        byte b = this.source.buffer().getByte(3L);
        boolean z = ((b >> 1) & 1) == 1;
        if (z) {
            updateCrc(this.source.buffer(), 0L, 10L);
        }
        checkEqual("ID1ID2", 8075, this.source.readShort());
        this.source.skip(8L);
        if (((b >> 2) & 1) == 1) {
            this.source.require(2L);
            if (z) {
                updateCrc(this.source.buffer(), 0L, 2L);
            }
            long shortLe = this.source.buffer().readShortLe();
            this.source.require(shortLe);
            if (z) {
                updateCrc(this.source.buffer(), 0L, shortLe);
            }
            this.source.skip(shortLe);
        }
        if (((b >> 3) & 1) == 1) {
            long jIndexOf = this.source.indexOf(SECTION_HEADER);
            if (jIndexOf == -1) {
                throw new EOFException();
            }
            if (z) {
                updateCrc(this.source.buffer(), 0L, jIndexOf + 1);
            }
            this.source.skip(jIndexOf + 1);
        }
        if (((b >> FCOMMENT) & 1) == 1) {
            long jIndexOf2 = this.source.indexOf(SECTION_HEADER);
            if (jIndexOf2 == -1) {
                throw new EOFException();
            }
            if (z) {
                updateCrc(this.source.buffer(), 0L, jIndexOf2 + 1);
            }
            this.source.skip(jIndexOf2 + 1);
        }
        if (z) {
            checkEqual("FHCRC", this.source.readShortLe(), (short) this.crc.getValue());
            this.crc.reset();
        }
    }

    private void consumeTrailer() throws IOException {
        checkEqual("CRC", this.source.readIntLe(), (int) this.crc.getValue());
        checkEqual("ISIZE", this.source.readIntLe(), (int) this.inflater.getBytesWritten());
    }

    private void updateCrc(Buffer buffer, long j, long j2) {
        f fVar = buffer.head;
        while (true) {
            int i = fVar.c;
            int i2 = fVar.b;
            if (j < i - i2) {
                break;
            }
            j -= (long) (i - i2);
            fVar = fVar.f;
        }
        while (j2 > 0) {
            int i3 = (int) (((long) fVar.b) + j);
            int iMin = (int) Math.min(fVar.c - i3, j2);
            this.crc.update(fVar.a, i3, iMin);
            j2 -= (long) iMin;
            fVar = fVar.f;
            j = 0;
        }
    }

    @Override
    public void close() {
        this.inflaterSource.close();
    }

    @Override
    public long read(Buffer buffer, long j) throws IOException {
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        }
        if (j == 0) {
            return 0L;
        }
        if (this.section == 0) {
            consumeHeader();
            this.section = 1;
        }
        if (this.section == 1) {
            long j2 = buffer.size;
            long j3 = this.inflaterSource.read(buffer, j);
            if (j3 != -1) {
                updateCrc(buffer, j2, j3);
                return j3;
            }
            this.section = 2;
        }
        if (this.section == 2) {
            consumeTrailer();
            this.section = 3;
            if (!this.source.exhausted()) {
                throw new IOException("gzip finished without exhausting source");
            }
        }
        return -1L;
    }

    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}
