package org.cocos2dx.okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

final class d implements BufferedSink {
    public final Buffer i = new Buffer();
    public final Sink j;
    boolean k;

    d(Sink sink) {
        if (sink == null) {
            throw new NullPointerException("sink == null");
        }
        this.j = sink;
    }

    @Override
    public Buffer buffer() {
        return this.i;
    }

    @Override
    public void close() {
        if (this.k) {
            return;
        }
        Buffer buffer = this.i;
        long j = buffer.size;
        if (j > 0) {
            this.j.write(buffer, j);
        }
        th = null;
        try {
            this.j.close();
        } catch (Throwable th) {
            if (th == null) {
                th = th;
            }
        }
        this.k = true;
        if (th != null) {
            i.f(th);
        }
    }

    @Override
    public BufferedSink emit() {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        long size = this.i.size();
        if (size > 0) {
            this.j.write(this.i, size);
        }
        return this;
    }

    @Override
    public BufferedSink emitCompleteSegments() {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        long jCompleteSegmentByteCount = this.i.completeSegmentByteCount();
        if (jCompleteSegmentByteCount > 0) {
            this.j.write(this.i, jCompleteSegmentByteCount);
        }
        return this;
    }

    @Override
    public void flush() {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        Buffer buffer = this.i;
        long j = buffer.size;
        if (j > 0) {
            this.j.write(buffer, j);
        }
        this.j.flush();
    }

    @Override
    public boolean isOpen() {
        return !this.k;
    }

    @Override
    public OutputStream outputStream() {
        return new a();
    }

    @Override
    public Timeout timeout() {
        return this.j.timeout();
    }

    public String toString() {
        return "buffer(" + this.j + ")";
    }

    @Override
    public void write(Buffer buffer, long j) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.write(buffer, j);
        emitCompleteSegments();
    }

    @Override
    public long writeAll(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long j = 0;
        while (true) {
            long j2 = source.read(this.i, 8192L);
            if (j2 == -1) {
                return j;
            }
            j += j2;
            emitCompleteSegments();
        }
    }

    @Override
    public BufferedSink writeByte(int i) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeByte(i);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeDecimalLong(long j) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeDecimalLong(j);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeHexadecimalUnsignedLong(long j) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeHexadecimalUnsignedLong(j);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeInt(int i) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeInt(i);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeIntLe(int i) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeIntLe(i);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeLong(long j) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeLong(j);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeLongLe(long j) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeLongLe(j);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeShort(int i) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeShort(i);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeShortLe(int i) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeShortLe(i);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeString(String str, Charset charset) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeString(str, charset);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeUtf8(String str) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeUtf8(str);
        return emitCompleteSegments();
    }

    @Override
    public BufferedSink writeUtf8CodePoint(int i) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        this.i.writeUtf8CodePoint(i);
        return emitCompleteSegments();
    }

    class a extends OutputStream {
        a() {
        }

        @Override
        public void close() {
            d.this.close();
        }

        @Override
        public void flush() {
            d dVar = d.this;
            if (dVar.k) {
                return;
            }
            dVar.flush();
        }

        public String toString() {
            return d.this + ".outputStream()";
        }

        @Override
        public void write(int i) throws IOException {
            d dVar = d.this;
            if (dVar.k) {
                throw new IOException("closed");
            }
            dVar.i.writeByte((int) ((byte) i));
            d.this.emitCompleteSegments();
        }

        @Override
        public void write(byte[] bArr, int i, int i2) throws IOException {
            d dVar = d.this;
            if (!dVar.k) {
                dVar.i.write(bArr, i, i2);
                d.this.emitCompleteSegments();
                return;
            }
            throw new IOException("closed");
        }
    }

    @Override
    public BufferedSink write(ByteString byteString) {
        if (!this.k) {
            this.i.write(byteString);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeString(String str, int i, int i2, Charset charset) {
        if (!this.k) {
            this.i.writeString(str, i, i2, charset);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeUtf8(String str, int i, int i2) {
        if (!this.k) {
            this.i.writeUtf8(str, i, i2);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink write(byte[] bArr) {
        if (!this.k) {
            this.i.write(bArr);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink write(byte[] bArr, int i, int i2) {
        if (!this.k) {
            this.i.write(bArr, i, i2);
            return emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public int write(ByteBuffer byteBuffer) {
        if (!this.k) {
            int iWrite = this.i.write(byteBuffer);
            emitCompleteSegments();
            return iWrite;
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink write(Source source, long j) throws EOFException {
        while (j > 0) {
            long j2 = source.read(this.i, j);
            if (j2 != -1) {
                j -= j2;
                emitCompleteSegments();
            } else {
                throw new EOFException();
            }
        }
        return this;
    }
}
