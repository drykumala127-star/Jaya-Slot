package org.cocos2dx.okhttp3.internal.ws;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ByteString;

final class WebSocketReader {
    final boolean a;
    final BufferedSource b;
    final FrameCallback c;
    boolean d;
    int e;
    long f;
    boolean g;
    boolean h;
    private final Buffer i = new Buffer();
    private final Buffer j = new Buffer();
    private final byte[] k;
    private final Buffer.UnsafeCursor l;

    public interface FrameCallback {
        void onReadClose(int i, String str);

        void onReadMessage(String str);

        void onReadMessage(ByteString byteString);

        void onReadPing(ByteString byteString);

        void onReadPong(ByteString byteString);
    }

    WebSocketReader(boolean z, BufferedSource bufferedSource, FrameCallback frameCallback) {
        if (bufferedSource == null) {
            throw new NullPointerException("source == null");
        }
        if (frameCallback == null) {
            throw new NullPointerException("frameCallback == null");
        }
        this.a = z;
        this.b = bufferedSource;
        this.c = frameCallback;
        this.k = z ? null : new byte[4];
        this.l = z ? null : new Buffer.UnsafeCursor();
    }

    private void b() throws ProtocolException {
        short s;
        String utf8;
        long j = this.f;
        if (j > 0) {
            this.b.readFully(this.i, j);
            if (!this.a) {
                this.i.readAndWriteUnsafe(this.l);
                this.l.seek(0L);
                WebSocketProtocol.toggleMask(this.l, this.k);
                this.l.close();
            }
        }
        switch (this.e) {
            case 8:
                long size = this.i.size();
                if (size == 1) {
                    throw new ProtocolException("Malformed close payload length of 1.");
                }
                if (size != 0) {
                    s = this.i.readShort();
                    utf8 = this.i.readUtf8();
                    String strCloseCodeExceptionMessage = WebSocketProtocol.closeCodeExceptionMessage(s);
                    if (strCloseCodeExceptionMessage != null) {
                        throw new ProtocolException(strCloseCodeExceptionMessage);
                    }
                } else {
                    s = 1005;
                    utf8 = "";
                }
                this.c.onReadClose(s, utf8);
                this.d = true;
                return;
            case 9:
                this.c.onReadPing(this.i.readByteString());
                return;
            case 10:
                this.c.onReadPong(this.i.readByteString());
                return;
            default:
                throw new ProtocolException("Unknown control opcode: " + Integer.toHexString(this.e));
        }
    }

    private void c() throws IOException {
        if (this.d) {
            throw new IOException("closed");
        }
        long jTimeoutNanos = this.b.timeout().timeoutNanos();
        this.b.timeout().clearTimeout();
        try {
            byte b = this.b.readByte();
            this.b.timeout().timeout(jTimeoutNanos, TimeUnit.NANOSECONDS);
            this.e = b & 15;
            boolean z = (b & 128) != 0;
            this.g = z;
            boolean z2 = (b & 8) != 0;
            this.h = z2;
            if (z2 && !z) {
                throw new ProtocolException("Control frames must be final.");
            }
            boolean z3 = (b & 64) != 0;
            boolean z4 = (b & 32) != 0;
            boolean z5 = (b & 16) != 0;
            if (z3 || z4 || z5) {
                throw new ProtocolException("Reserved flags are unsupported.");
            }
            byte b2 = this.b.readByte();
            boolean z6 = (b2 & 128) != 0;
            if (z6 == this.a) {
                throw new ProtocolException(this.a ? "Server-sent frames must not be masked." : "Client-sent frames must be masked.");
            }
            long j = b2 & 127;
            this.f = j;
            if (j == 126) {
                this.f = ((long) this.b.readShort()) & 65535;
            } else if (j == 127) {
                long j2 = this.b.readLong();
                this.f = j2;
                if (j2 < 0) {
                    throw new ProtocolException("Frame length 0x" + Long.toHexString(this.f) + " > 0x7FFFFFFFFFFFFFFF");
                }
            }
            if (this.h && this.f > 125) {
                throw new ProtocolException("Control frame must be less than 125B.");
            }
            if (z6) {
                this.b.readFully(this.k);
            }
        } catch (Throwable th) {
            this.b.timeout().timeout(jTimeoutNanos, TimeUnit.NANOSECONDS);
            throw th;
        }
    }

    private void d() throws IOException {
        while (!this.d) {
            long j = this.f;
            if (j > 0) {
                this.b.readFully(this.j, j);
                if (!this.a) {
                    this.j.readAndWriteUnsafe(this.l);
                    this.l.seek(this.j.size() - this.f);
                    WebSocketProtocol.toggleMask(this.l, this.k);
                    this.l.close();
                }
            }
            if (this.g) {
                return;
            }
            f();
            if (this.e != 0) {
                throw new ProtocolException("Expected continuation opcode. Got: " + Integer.toHexString(this.e));
            }
        }
        throw new IOException("closed");
    }

    private void e() throws IOException {
        int i = this.e;
        if (i != 1 && i != 2) {
            throw new ProtocolException("Unknown opcode: " + Integer.toHexString(i));
        }
        d();
        if (i == 1) {
            this.c.onReadMessage(this.j.readUtf8());
        } else {
            this.c.onReadMessage(this.j.readByteString());
        }
    }

    private void f() throws IOException {
        while (!this.d) {
            c();
            if (!this.h) {
                return;
            } else {
                b();
            }
        }
    }

    void a() throws IOException {
        c();
        if (this.h) {
            b();
        } else {
            e();
        }
    }
}
