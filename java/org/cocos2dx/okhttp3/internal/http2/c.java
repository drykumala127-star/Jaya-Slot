package org.cocos2dx.okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.http2.a;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSink;

final class c implements Closeable {
    private static final Logger o = Logger.getLogger(Http2.class.getName());
    private final BufferedSink i;
    private final boolean j;
    private final Buffer k;
    private int l;
    private boolean m;
    final a.b n;

    c(BufferedSink bufferedSink, boolean z) {
        this.i = bufferedSink;
        this.j = z;
        Buffer buffer = new Buffer();
        this.k = buffer;
        this.n = new a.b(buffer);
        this.l = 16384;
    }

    private void y(int i, long j) {
        while (j > 0) {
            int iMin = (int) Math.min(this.l, j);
            long j2 = iMin;
            j -= j2;
            n(i, iMin, (byte) 9, j == 0 ? (byte) 4 : (byte) 0);
            this.i.write(this.k, j2);
        }
    }

    private static void z(BufferedSink bufferedSink, int i) {
        bufferedSink.writeByte((i >>> 16) & 255);
        bufferedSink.writeByte((i >>> 8) & 255);
        bufferedSink.writeByte(i & 255);
    }

    public synchronized void a(Settings settings) {
        try {
            if (this.m) {
                throw new IOException("closed");
            }
            this.l = settings.getMaxFrameSize(this.l);
            if (settings.getHeaderTableSize() != -1) {
                this.n.e(settings.getHeaderTableSize());
            }
            n(0, 0, (byte) 4, (byte) 1);
            this.i.flush();
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override
    public synchronized void close() {
        this.m = true;
        this.i.close();
    }

    public synchronized void e() {
        try {
            if (this.m) {
                throw new IOException("closed");
            }
            if (this.j) {
                Logger logger = o;
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(Util.format(">> CONNECTION %s", Http2.CONNECTION_PREFACE.hex()));
                }
                this.i.write(Http2.CONNECTION_PREFACE.toByteArray());
                this.i.flush();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void f(boolean z, int i, Buffer buffer, int i2) {
        if (this.m) {
            throw new IOException("closed");
        }
        h(i, z ? (byte) 1 : (byte) 0, buffer, i2);
    }

    public synchronized void flush() {
        if (this.m) {
            throw new IOException("closed");
        }
        this.i.flush();
    }

    void h(int i, byte b, Buffer buffer, int i2) {
        n(i, i2, (byte) 0, b);
        if (i2 > 0) {
            this.i.write(buffer, i2);
        }
    }

    public void n(int i, int i2, byte b, byte b2) {
        Logger logger = o;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(Http2.frameLog(false, i, i2, b, b2));
        }
        int i3 = this.l;
        if (i2 > i3) {
            throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", Integer.valueOf(i3), Integer.valueOf(i2));
        }
        if ((Integer.MIN_VALUE & i) != 0) {
            throw Http2.illegalArgument("reserved bit set: %s", Integer.valueOf(i));
        }
        z(this.i, i2);
        this.i.writeByte(b & 255);
        this.i.writeByte(b2 & 255);
        this.i.writeInt(i & Integer.MAX_VALUE);
    }

    public synchronized void o(int i, ErrorCode errorCode, byte[] bArr) {
        try {
            if (this.m) {
                throw new IOException("closed");
            }
            if (errorCode.httpCode == -1) {
                throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]);
            }
            n(0, bArr.length + 8, (byte) 7, (byte) 0);
            this.i.writeInt(i);
            this.i.writeInt(errorCode.httpCode);
            if (bArr.length > 0) {
                this.i.write(bArr);
            }
            this.i.flush();
        } catch (Throwable th) {
            throw th;
        }
    }

    void p(boolean z, int i, List list) throws IOException {
        if (this.m) {
            throw new IOException("closed");
        }
        this.n.g(list);
        long size = this.k.size();
        int iMin = (int) Math.min(this.l, size);
        long j = iMin;
        byte b = size == j ? (byte) 4 : (byte) 0;
        if (z) {
            b = (byte) (b | 1);
        }
        n(i, iMin, (byte) 1, b);
        this.i.write(this.k, j);
        if (size > j) {
            y(i, size - j);
        }
    }

    public int q() {
        return this.l;
    }

    public synchronized void r(boolean z, int i, int i2) {
        if (this.m) {
            throw new IOException("closed");
        }
        n(0, 8, (byte) 6, z ? (byte) 1 : (byte) 0);
        this.i.writeInt(i);
        this.i.writeInt(i2);
        this.i.flush();
    }

    public synchronized void s(int i, int i2, List list) {
        if (this.m) {
            throw new IOException("closed");
        }
        this.n.g(list);
        long size = this.k.size();
        int iMin = (int) Math.min(this.l - 4, size);
        long j = iMin;
        n(i, iMin + 4, (byte) 5, size == j ? (byte) 4 : (byte) 0);
        this.i.writeInt(i2 & Integer.MAX_VALUE);
        this.i.write(this.k, j);
        if (size > j) {
            y(i, size - j);
        }
    }

    public synchronized void t(int i, ErrorCode errorCode) {
        if (this.m) {
            throw new IOException("closed");
        }
        if (errorCode.httpCode == -1) {
            throw new IllegalArgumentException();
        }
        n(i, 4, (byte) 3, (byte) 0);
        this.i.writeInt(errorCode.httpCode);
        this.i.flush();
    }

    public synchronized void u(Settings settings) {
        try {
            if (this.m) {
                throw new IOException("closed");
            }
            int i = 0;
            n(0, settings.size() * 6, (byte) 4, (byte) 0);
            while (i < 10) {
                if (settings.isSet(i)) {
                    this.i.writeShort(i == 4 ? 3 : i == 7 ? 4 : i);
                    this.i.writeInt(settings.get(i));
                }
                i++;
            }
            this.i.flush();
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void v(boolean z, int i, List list) {
        if (this.m) {
            throw new IOException("closed");
        }
        p(z, i, list);
    }

    public synchronized void w(boolean z, int i, int i2, List list) {
        if (this.m) {
            throw new IOException("closed");
        }
        p(z, i, list);
    }

    public synchronized void x(int i, long j) {
        if (this.m) {
            throw new IOException("closed");
        }
        if (j == 0 || j > 2147483647L) {
            throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", Long.valueOf(j));
        }
        n(i, 4, (byte) 8, (byte) 0);
        this.i.writeInt((int) j);
        this.i.flush();
    }
}
