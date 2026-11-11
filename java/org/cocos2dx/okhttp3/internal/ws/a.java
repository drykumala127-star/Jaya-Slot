package org.cocos2dx.okhttp3.internal.ws;

import java.io.IOException;
import java.util.Random;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Timeout;

final class a {
    final boolean a;
    final Random b;
    final BufferedSink c;
    final Buffer d;
    boolean e;
    final Buffer f = new Buffer();
    final a g = new a();
    boolean h;
    private final byte[] i;
    private final Buffer.UnsafeCursor j;

    final class a implements Sink {
        int i;
        long j;
        boolean k;
        boolean l;

        a() {
        }

        @Override
        public void close() throws IOException {
            if (this.l) {
                throw new IOException("closed");
            }
            a aVar = a.this;
            aVar.d(this.i, aVar.f.size(), this.k, true);
            this.l = true;
            a.this.h = false;
        }

        @Override
        public void flush() throws IOException {
            if (this.l) {
                throw new IOException("closed");
            }
            a aVar = a.this;
            aVar.d(this.i, aVar.f.size(), this.k, false);
            this.k = false;
        }

        @Override
        public Timeout timeout() {
            return a.this.c.timeout();
        }

        @Override
        public void write(Buffer buffer, long j) throws IOException {
            if (this.l) {
                throw new IOException("closed");
            }
            a.this.f.write(buffer, j);
            boolean z = this.k && this.j != -1 && a.this.f.size() > this.j - 8192;
            long jCompleteSegmentByteCount = a.this.f.completeSegmentByteCount();
            if (jCompleteSegmentByteCount <= 0 || z) {
                return;
            }
            a.this.d(this.i, jCompleteSegmentByteCount, this.k, false);
            this.k = false;
        }
    }

    a(boolean z, BufferedSink bufferedSink, Random random) {
        if (bufferedSink == null) {
            throw new NullPointerException("sink == null");
        }
        if (random == null) {
            throw new NullPointerException("random == null");
        }
        this.a = z;
        this.c = bufferedSink;
        this.d = bufferedSink.buffer();
        this.b = random;
        this.i = z ? new byte[4] : null;
        this.j = z ? new Buffer.UnsafeCursor() : null;
    }

    private void c(int i, ByteString byteString) throws IOException {
        if (this.e) {
            throw new IOException("closed");
        }
        int size = byteString.size();
        if (size > 125) {
            throw new IllegalArgumentException("Payload size must be less than or equal to 125");
        }
        this.d.writeByte(i | 128);
        if (this.a) {
            this.d.writeByte(size | 128);
            this.b.nextBytes(this.i);
            this.d.write(this.i);
            if (size > 0) {
                long size2 = this.d.size();
                this.d.write(byteString);
                this.d.readAndWriteUnsafe(this.j);
                this.j.seek(size2);
                WebSocketProtocol.toggleMask(this.j, this.i);
                this.j.close();
            }
        } else {
            this.d.writeByte(size);
            this.d.write(byteString);
        }
        this.c.flush();
    }

    Sink a(int i, long j) {
        if (this.h) {
            throw new IllegalStateException("Another message writer is active. Did you call close()?");
        }
        this.h = true;
        a aVar = this.g;
        aVar.i = i;
        aVar.j = j;
        aVar.k = true;
        aVar.l = false;
        return aVar;
    }

    void b(int i, ByteString byteString) {
        ByteString byteString2 = ByteString.EMPTY;
        if (i != 0 || byteString != null) {
            if (i != 0) {
                WebSocketProtocol.validateCloseCode(i);
            }
            Buffer buffer = new Buffer();
            buffer.writeShort(i);
            if (byteString != null) {
                buffer.write(byteString);
            }
            byteString2 = buffer.readByteString();
        }
        try {
            c(8, byteString2);
        } finally {
            this.e = true;
        }
    }

    void d(int i, long j, boolean z, boolean z2) throws IOException {
        if (this.e) {
            throw new IOException("closed");
        }
        if (!z) {
            i = 0;
        }
        if (z2) {
            i |= 128;
        }
        this.d.writeByte(i);
        int i2 = this.a ? 128 : 0;
        if (j <= 125) {
            this.d.writeByte(((int) j) | i2);
        } else if (j <= 65535) {
            this.d.writeByte(i2 | 126);
            this.d.writeShort((int) j);
        } else {
            this.d.writeByte(i2 | 127);
            this.d.writeLong(j);
        }
        if (this.a) {
            this.b.nextBytes(this.i);
            this.d.write(this.i);
            if (j > 0) {
                long size = this.d.size();
                this.d.write(this.f, j);
                this.d.readAndWriteUnsafe(this.j);
                this.j.seek(size);
                WebSocketProtocol.toggleMask(this.j, this.i);
                this.j.close();
            }
        } else {
            this.d.write(this.f, j);
        }
        this.c.emit();
    }

    void e(ByteString byteString) throws IOException {
        c(9, byteString);
    }

    void f(ByteString byteString) throws IOException {
        c(10, byteString);
    }
}
