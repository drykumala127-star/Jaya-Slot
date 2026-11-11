package org.cocos2dx.okio;

import java.util.zip.CRC32;
import java.util.zip.Deflater;

public final class GzipSink implements Sink {
    private boolean closed;
    private final CRC32 crc = new CRC32();
    private final Deflater deflater;
    private final DeflaterSink deflaterSink;
    private final BufferedSink sink;

    public GzipSink(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        Deflater deflater = new Deflater(-1, true);
        this.deflater = deflater;
        BufferedSink bufferedSinkBuffer = Okio.buffer(sink);
        this.sink = bufferedSinkBuffer;
        this.deflaterSink = new DeflaterSink(bufferedSinkBuffer, deflater);
        writeHeader();
    }

    private void updateCrc(Buffer buffer, long j) {
        f fVar = buffer.head;
        while (j > 0) {
            int iMin = (int) Math.min(j, fVar.c - fVar.b);
            this.crc.update(fVar.a, fVar.b, iMin);
            j -= (long) iMin;
            fVar = fVar.f;
        }
    }

    private void writeFooter() {
        this.sink.writeIntLe((int) this.crc.getValue());
        this.sink.writeIntLe((int) this.deflater.getBytesRead());
    }

    private void writeHeader() {
        Buffer buffer = this.sink.buffer();
        buffer.writeShort(8075);
        buffer.writeByte(8);
        buffer.writeByte(0);
        buffer.writeInt(0);
        buffer.writeByte(0);
        buffer.writeByte(0);
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.deflaterSink.finishDeflate();
        writeFooter();
        th = null;
        try {
            this.deflater.end();
        } catch (Throwable th) {
            if (th == null) {
                th = th;
            }
        }
        try {
            this.sink.close();
        } catch (Throwable th2) {
            if (th == null) {
                th = th2;
            }
        }
        this.closed = true;
        if (th != null) {
            i.f(th);
        }
    }

    public final Deflater deflater() {
        return this.deflater;
    }

    @Override
    public void flush() {
        this.deflaterSink.flush();
    }

    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }

    @Override
    public void write(Buffer buffer, long j) {
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        }
        if (j == 0) {
            return;
        }
        updateCrc(buffer, j);
        this.deflaterSink.write(buffer, j);
    }
}
