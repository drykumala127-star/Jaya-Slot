package org.cocos2dx.okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.http2.a;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.Source;
import org.cocos2dx.okio.Timeout;

final class b implements Closeable {
    static final Logger m = Logger.getLogger(Http2.class.getName());
    private final BufferedSource i;
    private final a j;
    private final boolean k;
    final a.a l;

    static final class a implements Source {
        private final BufferedSource i;
        int j;
        byte k;
        int l;
        int m;
        short n;

        a(BufferedSource bufferedSource) {
            this.i = bufferedSource;
        }

        private void a() throws IOException {
            int i = this.l;
            int iQ = b.q(this.i);
            this.m = iQ;
            this.j = iQ;
            byte b = (byte) (this.i.readByte() & 255);
            this.k = (byte) (this.i.readByte() & 255);
            Logger logger = b.m;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(Http2.frameLog(true, this.l, this.j, b, this.k));
            }
            int i2 = this.i.readInt() & Integer.MAX_VALUE;
            this.l = i2;
            if (b != 9) {
                throw Http2.ioException("%s != TYPE_CONTINUATION", Byte.valueOf(b));
            }
            if (i2 != i) {
                throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]);
            }
        }

        @Override
        public void close() {
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            while (true) {
                int i = this.m;
                if (i != 0) {
                    long j2 = this.i.read(buffer, Math.min(j, i));
                    if (j2 == -1) {
                        return -1L;
                    }
                    this.m = (int) (((long) this.m) - j2);
                    return j2;
                }
                this.i.skip(this.n);
                this.n = (short) 0;
                if ((this.k & 4) != 0) {
                    return -1L;
                }
                a();
            }
        }

        @Override
        public Timeout timeout() {
            return this.i.timeout();
        }
    }

    interface b {
        void a(boolean z, int i, int i2, List list);

        void b();

        void c(int i, long j);

        void d(boolean z, Settings settings);

        void e(int i, int i2, List list);

        void f(int i, ErrorCode errorCode, ByteString byteString);

        void g(int i, ErrorCode errorCode);

        void h(boolean z, int i, int i2);

        void i(int i, int i2, int i3, boolean z);

        void j(boolean z, int i, BufferedSource bufferedSource, int i2);
    }

    b(BufferedSource bufferedSource, boolean z) {
        this.i = bufferedSource;
        this.k = z;
        a aVar = new a(bufferedSource);
        this.j = aVar;
        this.l = new a.a(4096, aVar);
    }

    static int a(int i, byte b2, short s) throws IOException {
        if ((b2 & 8) != 0) {
            i--;
        }
        if (s <= i) {
            return (short) (i - s);
        }
        throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", Short.valueOf(s), Integer.valueOf(i));
    }

    private void h(b bVar, int i, byte b2, int i2) throws IOException {
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_DATA streamId == 0", new Object[0]);
        }
        boolean z = (b2 & 1) != 0;
        if ((b2 & 32) != 0) {
            throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]);
        }
        short s = (b2 & 8) != 0 ? (short) (this.i.readByte() & 255) : (short) 0;
        bVar.j(z, i2, this.i, a(i, b2, s));
        this.i.skip(s);
    }

    private void n(b bVar, int i, byte b2, int i2) throws IOException {
        if (i < 8) {
            throw Http2.ioException("TYPE_GOAWAY length < 8: %s", Integer.valueOf(i));
        }
        if (i2 != 0) {
            throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]);
        }
        int i3 = this.i.readInt();
        int i4 = this.i.readInt();
        int i5 = i - 8;
        ErrorCode errorCodeFromHttp2 = ErrorCode.fromHttp2(i4);
        if (errorCodeFromHttp2 == null) {
            throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", Integer.valueOf(i4));
        }
        ByteString byteString = ByteString.EMPTY;
        if (i5 > 0) {
            byteString = this.i.readByteString(i5);
        }
        bVar.f(i3, errorCodeFromHttp2, byteString);
    }

    private List o(int i, short s, byte b2, int i2) {
        a aVar = this.j;
        aVar.m = i;
        aVar.j = i;
        aVar.n = s;
        aVar.k = b2;
        aVar.l = i2;
        this.l.k();
        return this.l.e();
    }

    private void p(b bVar, int i, byte b2, int i2) throws IOException {
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]);
        }
        boolean z = (b2 & 1) != 0;
        short s = (b2 & 8) != 0 ? (short) (this.i.readByte() & 255) : (short) 0;
        if ((b2 & 32) != 0) {
            s(bVar, i2);
            i -= 5;
        }
        bVar.a(z, i2, -1, o(a(i, b2, s), s, b2, i2));
    }

    static int q(BufferedSource bufferedSource) {
        return (bufferedSource.readByte() & 255) | ((bufferedSource.readByte() & 255) << 16) | ((bufferedSource.readByte() & 255) << 8);
    }

    private void r(b bVar, int i, byte b2, int i2) throws IOException {
        if (i != 8) {
            throw Http2.ioException("TYPE_PING length != 8: %s", Integer.valueOf(i));
        }
        if (i2 != 0) {
            throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]);
        }
        bVar.h((b2 & 1) != 0, this.i.readInt(), this.i.readInt());
    }

    private void s(b bVar, int i) {
        int i2 = this.i.readInt();
        bVar.i(i, i2 & Integer.MAX_VALUE, (this.i.readByte() & 255) + 1, (Integer.MIN_VALUE & i2) != 0);
    }

    private void t(b bVar, int i, byte b2, int i2) throws IOException {
        if (i != 5) {
            throw Http2.ioException("TYPE_PRIORITY length: %d != 5", Integer.valueOf(i));
        }
        if (i2 == 0) {
            throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]);
        }
        s(bVar, i2);
    }

    private void u(b bVar, int i, byte b2, int i2) throws IOException {
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]);
        }
        short s = (b2 & 8) != 0 ? (short) (this.i.readByte() & 255) : (short) 0;
        bVar.e(i2, this.i.readInt() & Integer.MAX_VALUE, o(a(i - 4, b2, s), s, b2, i2));
    }

    private void v(b bVar, int i, byte b2, int i2) throws IOException {
        if (i != 4) {
            throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", Integer.valueOf(i));
        }
        if (i2 == 0) {
            throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]);
        }
        int i3 = this.i.readInt();
        ErrorCode errorCodeFromHttp2 = ErrorCode.fromHttp2(i3);
        if (errorCodeFromHttp2 == null) {
            throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", Integer.valueOf(i3));
        }
        bVar.g(i2, errorCodeFromHttp2);
    }

    private void w(b bVar, int i, byte b2, int i2) throws IOException {
        if (i2 != 0) {
            throw Http2.ioException("TYPE_SETTINGS streamId != 0", new Object[0]);
        }
        if ((b2 & 1) != 0) {
            if (i != 0) {
                throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]);
            }
            bVar.b();
            return;
        }
        if (i % 6 != 0) {
            throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", Integer.valueOf(i));
        }
        Settings settings = new Settings();
        for (int i3 = 0; i3 < i; i3 += 6) {
            int i4 = this.i.readShort() & 65535;
            int i5 = this.i.readInt();
            if (i4 == 2) {
                if (i5 != 0 && i5 != 1) {
                    throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]);
                }
            } else if (i4 == 3) {
                i4 = 4;
            } else if (i4 != 4) {
                if (i4 == 5 && (i5 < 16384 || i5 > 16777215)) {
                    throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", Integer.valueOf(i5));
                }
            } else {
                if (i5 < 0) {
                    throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]);
                }
                i4 = 7;
            }
            settings.set(i4, i5);
        }
        bVar.d(false, settings);
    }

    private void x(b bVar, int i, byte b2, int i2) throws IOException {
        if (i != 4) {
            throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", Integer.valueOf(i));
        }
        long j = ((long) this.i.readInt()) & 2147483647L;
        if (j == 0) {
            throw Http2.ioException("windowSizeIncrement was 0", Long.valueOf(j));
        }
        bVar.c(i2, j);
    }

    @Override
    public void close() {
        this.i.close();
    }

    public boolean e(boolean z, b bVar) throws IOException {
        try {
            this.i.require(9L);
            int iQ = q(this.i);
            if (iQ < 0 || iQ > 16384) {
                throw Http2.ioException("FRAME_SIZE_ERROR: %s", Integer.valueOf(iQ));
            }
            byte b2 = (byte) (this.i.readByte() & 255);
            if (z && b2 != 4) {
                throw Http2.ioException("Expected a SETTINGS frame but was %s", Byte.valueOf(b2));
            }
            byte b3 = (byte) (this.i.readByte() & 255);
            int i = this.i.readInt() & Integer.MAX_VALUE;
            Logger logger = m;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(Http2.frameLog(true, i, iQ, b2, b3));
            }
            switch (b2) {
                case 0:
                    h(bVar, iQ, b3, i);
                    return true;
                case 1:
                    p(bVar, iQ, b3, i);
                    return true;
                case 2:
                    t(bVar, iQ, b3, i);
                    return true;
                case 3:
                    v(bVar, iQ, b3, i);
                    return true;
                case 4:
                    w(bVar, iQ, b3, i);
                    return true;
                case 5:
                    u(bVar, iQ, b3, i);
                    return true;
                case 6:
                    r(bVar, iQ, b3, i);
                    return true;
                case 7:
                    n(bVar, iQ, b3, i);
                    return true;
                case 8:
                    x(bVar, iQ, b3, i);
                    return true;
                default:
                    this.i.skip(iQ);
                    return true;
            }
        } catch (IOException unused) {
            return false;
        }
    }

    public void f(b bVar) throws IOException {
        if (this.k) {
            if (!e(true, bVar)) {
                throw Http2.ioException("Required SETTINGS preface not received", new Object[0]);
            }
            return;
        }
        BufferedSource bufferedSource = this.i;
        ByteString byteString = Http2.CONNECTION_PREFACE;
        ByteString byteString2 = bufferedSource.readByteString(byteString.size());
        Logger logger = m;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(Util.format("<< CONNECTION %s", byteString2.hex()));
        }
        if (!byteString.equals(byteString2)) {
            throw Http2.ioException("Expected a connection header but was %s", byteString2.utf8());
        }
    }
}
