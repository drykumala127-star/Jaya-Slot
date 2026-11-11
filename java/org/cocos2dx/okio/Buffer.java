package org.cocos2dx.okio;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    private static final byte[] DIGITS = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    static final int REPLACEMENT_CHARACTER = 65533;

    @Nullable
    f head;
    long size;

    public static final class UnsafeCursor implements Closeable {
        public Buffer buffer;
        public byte[] data;
        public boolean readWrite;
        private f segment;
        public long offset = -1;
        public int start = -1;
        public int end = -1;

        @Override
        public void close() {
            if (this.buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            }
            this.buffer = null;
            this.segment = null;
            this.offset = -1L;
            this.data = null;
            this.start = -1;
            this.end = -1;
        }

        public final long expandBuffer(int i) {
            if (i <= 0) {
                throw new IllegalArgumentException("minByteCount <= 0: " + i);
            }
            if (i > 8192) {
                throw new IllegalArgumentException("minByteCount > Segment.SIZE: " + i);
            }
            Buffer buffer = this.buffer;
            if (buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            }
            if (!this.readWrite) {
                throw new IllegalStateException("expandBuffer() only permitted for read/write buffers");
            }
            long j = buffer.size;
            f fVarWritableSegment = buffer.writableSegment(i);
            int i2 = 8192 - fVarWritableSegment.c;
            fVarWritableSegment.c = 8192;
            long j2 = i2;
            this.buffer.size = j + j2;
            this.segment = fVarWritableSegment;
            this.offset = j;
            this.data = fVarWritableSegment.a;
            this.start = 8192 - i2;
            this.end = 8192;
            return j2;
        }

        public final int next() {
            long j = this.offset;
            if (j != this.buffer.size) {
                return j == -1 ? seek(0L) : seek(j + ((long) (this.end - this.start)));
            }
            throw new IllegalStateException();
        }

        public final long resizeBuffer(long j) {
            Buffer buffer = this.buffer;
            if (buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            }
            if (!this.readWrite) {
                throw new IllegalStateException("resizeBuffer() only permitted for read/write buffers");
            }
            long j2 = buffer.size;
            if (j <= j2) {
                if (j < 0) {
                    throw new IllegalArgumentException("newSize < 0: " + j);
                }
                long j3 = j2 - j;
                while (j3 > 0) {
                    Buffer buffer2 = this.buffer;
                    f fVar = buffer2.head.g;
                    int i = fVar.c;
                    long j4 = i - fVar.b;
                    if (j4 > j3) {
                        fVar.c = (int) (((long) i) - j3);
                        break;
                    }
                    buffer2.head = fVar.b();
                    g.a(fVar);
                    j3 -= j4;
                }
                this.segment = null;
                this.offset = j;
                this.data = null;
                this.start = -1;
                this.end = -1;
            } else if (j > j2) {
                long j5 = j - j2;
                boolean z = true;
                while (j5 > 0) {
                    f fVarWritableSegment = this.buffer.writableSegment(1);
                    int iMin = (int) Math.min(j5, 8192 - fVarWritableSegment.c);
                    int i2 = fVarWritableSegment.c + iMin;
                    fVarWritableSegment.c = i2;
                    j5 -= (long) iMin;
                    if (z) {
                        this.segment = fVarWritableSegment;
                        this.offset = j2;
                        this.data = fVarWritableSegment.a;
                        this.start = i2 - iMin;
                        this.end = i2;
                        z = false;
                    }
                }
            }
            this.buffer.size = j;
            return j2;
        }

        public final int seek(long j) {
            if (j >= -1) {
                Buffer buffer = this.buffer;
                long j2 = buffer.size;
                if (j <= j2) {
                    if (j == -1 || j == j2) {
                        this.segment = null;
                        this.offset = j;
                        this.data = null;
                        this.start = -1;
                        this.end = -1;
                        return -1;
                    }
                    f fVar = buffer.head;
                    f fVarC = this.segment;
                    long j3 = 0;
                    if (fVarC != null) {
                        long j4 = this.offset - ((long) (this.start - fVarC.b));
                        if (j4 > j) {
                            j2 = j4;
                            fVarC = fVar;
                            fVar = fVarC;
                        } else {
                            j3 = j4;
                        }
                    } else {
                        fVarC = fVar;
                    }
                    if (j2 - j > j - j3) {
                        while (true) {
                            int i = fVarC.c;
                            int i2 = fVarC.b;
                            if (j < ((long) (i - i2)) + j3) {
                                break;
                            }
                            j3 += (long) (i - i2);
                            fVarC = fVarC.f;
                        }
                    } else {
                        while (j2 > j) {
                            fVar = fVar.g;
                            j2 -= (long) (fVar.c - fVar.b);
                        }
                        fVarC = fVar;
                        j3 = j2;
                    }
                    if (this.readWrite && fVarC.d) {
                        f fVarF = fVarC.f();
                        Buffer buffer2 = this.buffer;
                        if (buffer2.head == fVarC) {
                            buffer2.head = fVarF;
                        }
                        fVarC = fVarC.c(fVarF);
                        fVarC.g.b();
                    }
                    this.segment = fVarC;
                    this.offset = j;
                    this.data = fVarC.a;
                    int i3 = fVarC.b + ((int) (j - j3));
                    this.start = i3;
                    int i4 = fVarC.c;
                    this.end = i4;
                    return i4 - i3;
                }
            }
            throw new ArrayIndexOutOfBoundsException(String.format("offset=%s > size=%s", Long.valueOf(j), Long.valueOf(this.buffer.size)));
        }
    }

    class a extends OutputStream {
        a() {
        }

        @Override
        public void close() {
        }

        @Override
        public void flush() {
        }

        public String toString() {
            return Buffer.this + ".outputStream()";
        }

        @Override
        public void write(int i) {
            Buffer.this.writeByte((int) ((byte) i));
        }

        @Override
        public void write(byte[] bArr, int i, int i2) {
            Buffer.this.write(bArr, i, i2);
        }
    }

    class b extends InputStream {
        b() {
        }

        @Override
        public int available() {
            return (int) Math.min(Buffer.this.size, 2147483647L);
        }

        @Override
        public void close() {
        }

        @Override
        public int read() {
            Buffer buffer = Buffer.this;
            if (buffer.size > 0) {
                return buffer.readByte() & 255;
            }
            return -1;
        }

        public String toString() {
            return Buffer.this + ".inputStream()";
        }

        @Override
        public int read(byte[] bArr, int i, int i2) {
            return Buffer.this.read(bArr, i, i2);
        }
    }

    private ByteString digest(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str);
            f fVar = this.head;
            if (fVar != null) {
                byte[] bArr = fVar.a;
                int i = fVar.b;
                messageDigest.update(bArr, i, fVar.c - i);
                f fVar2 = this.head;
                while (true) {
                    fVar2 = fVar2.f;
                    if (fVar2 == this.head) {
                        break;
                    }
                    byte[] bArr2 = fVar2.a;
                    int i2 = fVar2.b;
                    messageDigest.update(bArr2, i2, fVar2.c - i2);
                }
            }
            return ByteString.of(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            throw new AssertionError();
        }
    }

    private ByteString hmac(String str, ByteString byteString) {
        try {
            Mac mac = Mac.getInstance(str);
            mac.init(new SecretKeySpec(byteString.toByteArray(), str));
            f fVar = this.head;
            if (fVar != null) {
                byte[] bArr = fVar.a;
                int i = fVar.b;
                mac.update(bArr, i, fVar.c - i);
                f fVar2 = this.head;
                while (true) {
                    fVar2 = fVar2.f;
                    if (fVar2 == this.head) {
                        break;
                    }
                    byte[] bArr2 = fVar2.a;
                    int i2 = fVar2.b;
                    mac.update(bArr2, i2, fVar2.c - i2);
                }
            }
            return ByteString.of(mac.doFinal());
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException unused) {
            throw new AssertionError();
        }
    }

    @Override
    public Buffer buffer() {
        return this;
    }

    public final void clear() {
        try {
            skip(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void close() {
    }

    public final long completeSegmentByteCount() {
        long j = this.size;
        if (j == 0) {
            return 0L;
        }
        f fVar = this.head.g;
        int i = fVar.c;
        return (i >= 8192 || !fVar.e) ? j : j - ((long) (i - fVar.b));
    }

    public final Buffer copyTo(OutputStream outputStream) {
        return copyTo(outputStream, 0L, this.size);
    }

    @Override
    public BufferedSink emit() {
        return this;
    }

    @Override
    public Buffer emitCompleteSegments() {
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Buffer)) {
            return false;
        }
        Buffer buffer = (Buffer) obj;
        long j = this.size;
        if (j != buffer.size) {
            return false;
        }
        long j2 = 0;
        if (j == 0) {
            return true;
        }
        f fVar = this.head;
        f fVar2 = buffer.head;
        int i = fVar.b;
        int i2 = fVar2.b;
        while (j2 < this.size) {
            long jMin = Math.min(fVar.c - i, fVar2.c - i2);
            int i3 = 0;
            while (i3 < jMin) {
                int i4 = i + 1;
                int i5 = i2 + 1;
                if (fVar.a[i] != fVar2.a[i2]) {
                    return false;
                }
                i3++;
                i = i4;
                i2 = i5;
            }
            if (i == fVar.c) {
                fVar = fVar.f;
                i = fVar.b;
            }
            if (i2 == fVar2.c) {
                fVar2 = fVar2.f;
                i2 = fVar2.b;
            }
            j2 += jMin;
        }
        return true;
    }

    @Override
    public boolean exhausted() {
        return this.size == 0;
    }

    @Override
    public void flush() {
    }

    public final byte getByte(long j) {
        int i;
        i.b(this.size, j, 1L);
        long j2 = this.size;
        if (j2 - j <= j) {
            long j3 = j - j2;
            f fVar = this.head;
            do {
                fVar = fVar.g;
                int i2 = fVar.c;
                i = fVar.b;
                j3 += (long) (i2 - i);
            } while (j3 < 0);
            return fVar.a[i + ((int) j3)];
        }
        f fVar2 = this.head;
        while (true) {
            int i3 = fVar2.c;
            int i4 = fVar2.b;
            long j4 = i3 - i4;
            if (j < j4) {
                return fVar2.a[i4 + ((int) j)];
            }
            j -= j4;
            fVar2 = fVar2.f;
        }
    }

    public int hashCode() {
        f fVar = this.head;
        if (fVar == null) {
            return 0;
        }
        int i = 1;
        do {
            int i2 = fVar.c;
            for (int i3 = fVar.b; i3 < i2; i3++) {
                i = (i * 31) + fVar.a[i3];
            }
            fVar = fVar.f;
        } while (fVar != this.head);
        return i;
    }

    public final ByteString hmacSha1(ByteString byteString) {
        return hmac("HmacSHA1", byteString);
    }

    public final ByteString hmacSha256(ByteString byteString) {
        return hmac("HmacSHA256", byteString);
    }

    public final ByteString hmacSha512(ByteString byteString) {
        return hmac("HmacSHA512", byteString);
    }

    @Override
    public long indexOf(byte b2) {
        return indexOf(b2, 0L, Long.MAX_VALUE);
    }

    @Override
    public long indexOfElement(ByteString byteString) {
        return indexOfElement(byteString, 0L);
    }

    @Override
    public InputStream inputStream() {
        return new b();
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    public final ByteString md5() {
        return digest("MD5");
    }

    @Override
    public OutputStream outputStream() {
        return new a();
    }

    @Override
    public boolean rangeEquals(long j, ByteString byteString) {
        return rangeEquals(j, byteString, 0, byteString.size());
    }

    @Override
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override
    public long readAll(Sink sink) {
        long j = this.size;
        if (j > 0) {
            sink.write(this, j);
        }
        return j;
    }

    public final UnsafeCursor readAndWriteUnsafe() {
        return readAndWriteUnsafe(new UnsafeCursor());
    }

    @Override
    public byte readByte() {
        long j = this.size;
        if (j == 0) {
            throw new IllegalStateException("size == 0");
        }
        f fVar = this.head;
        int i = fVar.b;
        int i2 = fVar.c;
        int i3 = i + 1;
        byte b2 = fVar.a[i];
        this.size = j - 1;
        if (i3 == i2) {
            this.head = fVar.b();
            g.a(fVar);
        } else {
            fVar.b = i3;
        }
        return b2;
    }

    @Override
    public byte[] readByteArray() {
        try {
            return readByteArray(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public ByteString readByteString() {
        return new ByteString(readByteArray());
    }

    @Override
    public long readDecimalLong() {
        if (this.size == 0) {
            throw new IllegalStateException("size == 0");
        }
        int i = 0;
        long j = -7;
        boolean z = false;
        long j2 = 0;
        boolean z2 = false;
        do {
            f fVar = this.head;
            byte[] bArr = fVar.a;
            int i2 = fVar.b;
            int i3 = fVar.c;
            while (i2 < i3) {
                byte b2 = bArr[i2];
                if (b2 >= 48 && b2 <= 57) {
                    int i4 = 48 - b2;
                    if (j2 < -922337203685477580L || (j2 == -922337203685477580L && i4 < j)) {
                        Buffer bufferWriteByte = new Buffer().writeDecimalLong(j2).writeByte((int) b2);
                        if (!z) {
                            bufferWriteByte.readByte();
                        }
                        throw new NumberFormatException("Number too large: " + bufferWriteByte.readUtf8());
                    }
                    j2 = (j2 * 10) + ((long) i4);
                } else {
                    if (b2 != 45 || i != 0) {
                        if (i != 0) {
                            z2 = true;
                            break;
                        }
                        throw new NumberFormatException("Expected leading [0-9] or '-' character but was 0x" + Integer.toHexString(b2));
                    }
                    j--;
                    z = true;
                }
                i2++;
                i++;
            }
            if (i2 == i3) {
                this.head = fVar.b();
                g.a(fVar);
            } else {
                fVar.b = i2;
            }
            if (z2) {
                break;
            }
        } while (this.head != null);
        this.size -= (long) i;
        return z ? j2 : -j2;
    }

    public final Buffer readFrom(InputStream inputStream) throws IOException {
        readFrom(inputStream, Long.MAX_VALUE, true);
        return this;
    }

    @Override
    public void readFully(Buffer buffer, long j) throws EOFException {
        long j2 = this.size;
        if (j2 >= j) {
            buffer.write(this, j);
        } else {
            buffer.write(this, j2);
            throw new EOFException();
        }
    }

    @Override
    public long readHexadecimalUnsignedLong() {
        int i;
        if (this.size == 0) {
            throw new IllegalStateException("size == 0");
        }
        int i2 = 0;
        long j = 0;
        boolean z = false;
        do {
            f fVar = this.head;
            byte[] bArr = fVar.a;
            int i3 = fVar.b;
            int i4 = fVar.c;
            while (i3 < i4) {
                byte b2 = bArr[i3];
                if (b2 >= 48 && b2 <= 57) {
                    i = b2 - 48;
                } else if (b2 >= 97 && b2 <= 102) {
                    i = b2 - 87;
                } else {
                    if (b2 < 65 || b2 > 70) {
                        if (i2 != 0) {
                            z = true;
                            break;
                        }
                        throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + Integer.toHexString(b2));
                    }
                    i = b2 - 55;
                }
                if (((-1152921504606846976L) & j) != 0) {
                    throw new NumberFormatException("Number too large: " + new Buffer().writeHexadecimalUnsignedLong(j).writeByte((int) b2).readUtf8());
                }
                j = (j << 4) | ((long) i);
                i3++;
                i2++;
            }
            if (i3 == i4) {
                this.head = fVar.b();
                g.a(fVar);
            } else {
                fVar.b = i3;
            }
            if (z) {
                break;
            }
        } while (this.head != null);
        this.size -= (long) i2;
        return j;
    }

    @Override
    public int readInt() {
        long j = this.size;
        if (j < 4) {
            throw new IllegalStateException("size < 4: " + this.size);
        }
        f fVar = this.head;
        int i = fVar.b;
        int i2 = fVar.c;
        if (i2 - i < 4) {
            return ((readByte() & 255) << 24) | ((readByte() & 255) << 16) | ((readByte() & 255) << 8) | (readByte() & 255);
        }
        byte[] bArr = fVar.a;
        int i3 = i + 3;
        int i4 = ((bArr[i + 1] & 255) << 16) | ((bArr[i] & 255) << 24) | ((bArr[i + 2] & 255) << 8);
        int i5 = i + 4;
        int i6 = (bArr[i3] & 255) | i4;
        this.size = j - 4;
        if (i5 == i2) {
            this.head = fVar.b();
            g.a(fVar);
        } else {
            fVar.b = i5;
        }
        return i6;
    }

    @Override
    public int readIntLe() {
        return i.c(readInt());
    }

    @Override
    public long readLong() {
        long j = this.size;
        if (j < 8) {
            throw new IllegalStateException("size < 8: " + this.size);
        }
        f fVar = this.head;
        int i = fVar.b;
        int i2 = fVar.c;
        if (i2 - i < 8) {
            return ((((long) readInt()) & 4294967295L) << 32) | (4294967295L & ((long) readInt()));
        }
        byte[] bArr = fVar.a;
        int i3 = i + 7;
        long j2 = ((((long) bArr[i + 1]) & 255) << 48) | ((((long) bArr[i]) & 255) << 56) | ((((long) bArr[i + 2]) & 255) << 40) | ((((long) bArr[i + 3]) & 255) << 32) | ((((long) bArr[i + 4]) & 255) << 24) | ((((long) bArr[i + 5]) & 255) << 16) | ((((long) bArr[i + 6]) & 255) << 8);
        int i4 = i + 8;
        long j3 = j2 | (((long) bArr[i3]) & 255);
        this.size = j - 8;
        if (i4 == i2) {
            this.head = fVar.b();
            g.a(fVar);
        } else {
            fVar.b = i4;
        }
        return j3;
    }

    @Override
    public long readLongLe() {
        return i.d(readLong());
    }

    @Override
    public short readShort() {
        long j = this.size;
        if (j < 2) {
            throw new IllegalStateException("size < 2: " + this.size);
        }
        f fVar = this.head;
        int i = fVar.b;
        int i2 = fVar.c;
        if (i2 - i < 2) {
            return (short) (((readByte() & 255) << 8) | (readByte() & 255));
        }
        byte[] bArr = fVar.a;
        int i3 = i + 1;
        int i4 = (bArr[i] & 255) << 8;
        int i5 = i + 2;
        int i6 = (bArr[i3] & 255) | i4;
        this.size = j - 2;
        if (i5 == i2) {
            this.head = fVar.b();
            g.a(fVar);
        } else {
            fVar.b = i5;
        }
        return (short) i6;
    }

    @Override
    public short readShortLe() {
        return i.e(readShort());
    }

    @Override
    public String readString(Charset charset) {
        try {
            return readString(this.size, charset);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public final UnsafeCursor readUnsafe() {
        return readUnsafe(new UnsafeCursor());
    }

    @Override
    public String readUtf8() {
        try {
            return readString(this.size, i.a);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public int readUtf8CodePoint() throws EOFException {
        int i;
        int i2;
        int i3;
        if (this.size == 0) {
            throw new EOFException();
        }
        byte b2 = getByte(0L);
        if ((b2 & 128) == 0) {
            i = b2 & 127;
            i2 = 1;
            i3 = 0;
        } else if ((b2 & 224) == 192) {
            i = b2 & 31;
            i2 = 2;
            i3 = 128;
        } else if ((b2 & 240) == 224) {
            i = b2 & 15;
            i2 = 3;
            i3 = 2048;
        } else {
            if ((b2 & 248) != 240) {
                skip(1L);
                return REPLACEMENT_CHARACTER;
            }
            i = b2 & 7;
            i2 = 4;
            i3 = 65536;
        }
        long j = i2;
        if (this.size < j) {
            throw new EOFException("size < " + i2 + ": " + this.size + " (to read code point prefixed 0x" + Integer.toHexString(b2) + ")");
        }
        for (int i4 = 1; i4 < i2; i4++) {
            long j2 = i4;
            byte b3 = getByte(j2);
            if ((b3 & 192) != 128) {
                skip(j2);
                return REPLACEMENT_CHARACTER;
            }
            i = (i << 6) | (b3 & 63);
        }
        skip(j);
        return i > 1114111 ? REPLACEMENT_CHARACTER : ((i < 55296 || i > 57343) && i >= i3) ? i : REPLACEMENT_CHARACTER;
    }

    @Override
    @Nullable
    public String readUtf8Line() {
        long jIndexOf = indexOf((byte) 10);
        if (jIndexOf != -1) {
            return readUtf8Line(jIndexOf);
        }
        long j = this.size;
        if (j != 0) {
            return readUtf8(j);
        }
        return null;
    }

    @Override
    public String readUtf8LineStrict() {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    @Override
    public boolean request(long j) {
        return this.size >= j;
    }

    @Override
    public void require(long j) throws EOFException {
        if (this.size < j) {
            throw new EOFException();
        }
    }

    List segmentSizes() {
        if (this.head == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        f fVar = this.head;
        arrayList.add(Integer.valueOf(fVar.c - fVar.b));
        f fVar2 = this.head;
        while (true) {
            fVar2 = fVar2.f;
            if (fVar2 == this.head) {
                return arrayList;
            }
            arrayList.add(Integer.valueOf(fVar2.c - fVar2.b));
        }
    }

    @Override
    public int select(Options options) {
        int iSelectPrefix = selectPrefix(options, false);
        if (iSelectPrefix == -1) {
            return -1;
        }
        try {
            skip(options.byteStrings[iSelectPrefix].size());
            return iSelectPrefix;
        } catch (EOFException unused) {
            throw new AssertionError();
        }
    }

    int selectPrefix(Options options, boolean z) {
        int i;
        int i2;
        f fVar;
        int i3;
        int i4;
        f fVar2 = this.head;
        int i5 = -2;
        if (fVar2 == null) {
            if (z) {
                return -2;
            }
            return options.indexOf(ByteString.EMPTY);
        }
        byte[] bArr = fVar2.a;
        int i6 = fVar2.b;
        int i7 = fVar2.c;
        int[] iArr = options.trie;
        f fVar3 = fVar2;
        int i8 = 0;
        int i9 = -1;
        loop0: while (true) {
            int i10 = i8 + 1;
            int i11 = iArr[i8];
            int i12 = i8 + 2;
            int i13 = iArr[i10];
            if (i13 != -1) {
                i9 = i13;
            }
            if (fVar3 == null) {
                break;
            }
            if (i11 >= 0) {
                int i14 = i6 + 1;
                int i15 = bArr[i6] & 255;
                int i16 = i12 + i11;
                while (i12 != i16) {
                    if (i15 == iArr[i12]) {
                        i = iArr[i12 + i11];
                        if (i14 == i7) {
                            fVar3 = fVar3.f;
                            i2 = fVar3.b;
                            bArr = fVar3.a;
                            i7 = fVar3.c;
                            if (fVar3 == fVar2) {
                                fVar3 = null;
                            }
                        } else {
                            i2 = i14;
                        }
                    } else {
                        i12++;
                    }
                }
                return i9;
            }
            int i17 = i12 + (i11 * (-1));
            while (true) {
                int i18 = i6 + 1;
                int i19 = i12 + 1;
                if ((bArr[i6] & 255) != iArr[i12]) {
                    return i9;
                }
                boolean z2 = i19 == i17;
                if (i18 == i7) {
                    f fVar4 = fVar3.f;
                    i4 = fVar4.b;
                    byte[] bArr2 = fVar4.a;
                    i3 = fVar4.c;
                    if (fVar4 != fVar2) {
                        fVar = fVar4;
                        bArr = bArr2;
                    } else {
                        if (!z2) {
                            break loop0;
                        }
                        bArr = bArr2;
                        fVar = null;
                    }
                } else {
                    fVar = fVar3;
                    i3 = i7;
                    i4 = i18;
                }
                if (z2) {
                    i = iArr[i19];
                    i2 = i4;
                    i7 = i3;
                    fVar3 = fVar;
                    break;
                }
                i6 = i4;
                i7 = i3;
                fVar3 = fVar;
                i12 = i19;
            }
            if (i >= 0) {
                return i;
            }
            i8 = -i;
            i6 = i2;
            i5 = -2;
        }
        return z ? i5 : i9;
    }

    public final ByteString sha1() {
        return digest("SHA-1");
    }

    public final ByteString sha256() {
        return digest("SHA-256");
    }

    public final ByteString sha512() {
        return digest("SHA-512");
    }

    public final long size() {
        return this.size;
    }

    @Override
    public void skip(long j) {
        while (j > 0) {
            if (this.head == null) {
                throw new EOFException();
            }
            int iMin = (int) Math.min(j, r0.c - r0.b);
            long j2 = iMin;
            this.size -= j2;
            j -= j2;
            f fVar = this.head;
            int i = fVar.b + iMin;
            fVar.b = i;
            if (i == fVar.c) {
                this.head = fVar.b();
                g.a(fVar);
            }
        }
    }

    public final ByteString snapshot() {
        long j = this.size;
        if (j <= 2147483647L) {
            return snapshot((int) j);
        }
        throw new IllegalArgumentException("size > Integer.MAX_VALUE: " + this.size);
    }

    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }

    public String toString() {
        return snapshot().toString();
    }

    f writableSegment(int i) {
        if (i < 1 || i > 8192) {
            throw new IllegalArgumentException();
        }
        f fVar = this.head;
        if (fVar != null) {
            f fVar2 = fVar.g;
            return (fVar2.c + i > 8192 || !fVar2.e) ? fVar2.c(g.b()) : fVar2;
        }
        f fVarB = g.b();
        this.head = fVarB;
        fVarB.g = fVarB;
        fVarB.f = fVarB;
        return fVarB;
    }

    @Override
    public long writeAll(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long j = 0;
        while (true) {
            long j2 = source.read(this, 8192L);
            if (j2 == -1) {
                return j;
            }
            j += j2;
        }
    }

    public final Buffer writeTo(OutputStream outputStream) {
        return writeTo(outputStream, this.size);
    }

    public Buffer clone() {
        Buffer buffer = new Buffer();
        if (this.size == 0) {
            return buffer;
        }
        f fVarD = this.head.d();
        buffer.head = fVarD;
        fVarD.g = fVarD;
        fVarD.f = fVarD;
        f fVar = this.head;
        while (true) {
            fVar = fVar.f;
            if (fVar == this.head) {
                buffer.size = this.size;
                return buffer;
            }
            buffer.head.g.c(fVar.d());
        }
    }

    public final Buffer copyTo(OutputStream outputStream, long j, long j2) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        i.b(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        f fVar = this.head;
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
            outputStream.write(fVar.a, i3, iMin);
            j2 -= (long) iMin;
            fVar = fVar.f;
            j = 0;
        }
        return this;
    }

    @Override
    public long indexOf(byte b2, long j) {
        return indexOf(b2, j, Long.MAX_VALUE);
    }

    @Override
    public long indexOfElement(ByteString byteString, long j) {
        int i;
        int i2;
        long j2 = 0;
        if (j < 0) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        f fVar = this.head;
        if (fVar == null) {
            return -1L;
        }
        long j3 = this.size;
        if (j3 - j < j) {
            while (j3 > j) {
                fVar = fVar.g;
                j3 -= (long) (fVar.c - fVar.b);
            }
        } else {
            while (true) {
                long j4 = ((long) (fVar.c - fVar.b)) + j2;
                if (j4 >= j) {
                    break;
                }
                fVar = fVar.f;
                j2 = j4;
            }
            j3 = j2;
        }
        if (byteString.size() == 2) {
            byte b2 = byteString.getByte(0);
            byte b3 = byteString.getByte(1);
            while (j3 < this.size) {
                byte[] bArr = fVar.a;
                i = (int) ((((long) fVar.b) + j) - j3);
                int i3 = fVar.c;
                while (i < i3) {
                    byte b4 = bArr[i];
                    if (b4 == b2 || b4 == b3) {
                        i2 = fVar.b;
                        return ((long) (i - i2)) + j3;
                    }
                    i++;
                }
                j3 += (long) (fVar.c - fVar.b);
                fVar = fVar.f;
                j = j3;
            }
            return -1L;
        }
        byte[] bArrInternalArray = byteString.internalArray();
        while (j3 < this.size) {
            byte[] bArr2 = fVar.a;
            i = (int) ((((long) fVar.b) + j) - j3);
            int i4 = fVar.c;
            while (i < i4) {
                byte b5 = bArr2[i];
                for (byte b6 : bArrInternalArray) {
                    if (b5 == b6) {
                        i2 = fVar.b;
                        return ((long) (i - i2)) + j3;
                    }
                }
                i++;
            }
            j3 += (long) (fVar.c - fVar.b);
            fVar = fVar.f;
            j = j3;
        }
        return -1L;
    }

    @Override
    public boolean rangeEquals(long j, ByteString byteString, int i, int i2) {
        if (j < 0 || i < 0 || i2 < 0 || this.size - j < i2 || byteString.size() - i < i2) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (getByte(((long) i3) + j) != byteString.getByte(i + i3)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int read(byte[] bArr, int i, int i2) {
        i.b(bArr.length, i, i2);
        f fVar = this.head;
        if (fVar == null) {
            return -1;
        }
        int iMin = Math.min(i2, fVar.c - fVar.b);
        System.arraycopy(fVar.a, fVar.b, bArr, i, iMin);
        int i3 = fVar.b + iMin;
        fVar.b = i3;
        this.size -= (long) iMin;
        if (i3 == fVar.c) {
            this.head = fVar.b();
            g.a(fVar);
        }
        return iMin;
    }

    public final UnsafeCursor readAndWriteUnsafe(UnsafeCursor unsafeCursor) {
        if (unsafeCursor.buffer != null) {
            throw new IllegalStateException("already attached to a buffer");
        }
        unsafeCursor.buffer = this;
        unsafeCursor.readWrite = true;
        return unsafeCursor;
    }

    @Override
    public ByteString readByteString(long j) {
        return new ByteString(readByteArray(j));
    }

    public final Buffer readFrom(InputStream inputStream, long j) throws IOException {
        if (j >= 0) {
            readFrom(inputStream, j, false);
            return this;
        }
        throw new IllegalArgumentException("byteCount < 0: " + j);
    }

    public final UnsafeCursor readUnsafe(UnsafeCursor unsafeCursor) {
        if (unsafeCursor.buffer != null) {
            throw new IllegalStateException("already attached to a buffer");
        }
        unsafeCursor.buffer = this;
        unsafeCursor.readWrite = false;
        return unsafeCursor;
    }

    @Override
    public String readUtf8LineStrict(long j) throws EOFException {
        if (j < 0) {
            throw new IllegalArgumentException("limit < 0: " + j);
        }
        long j2 = j != Long.MAX_VALUE ? j + 1 : Long.MAX_VALUE;
        long jIndexOf = indexOf((byte) 10, 0L, j2);
        if (jIndexOf != -1) {
            return readUtf8Line(jIndexOf);
        }
        if (j2 < size() && getByte(j2 - 1) == 13 && getByte(j2) == 10) {
            return readUtf8Line(j2);
        }
        Buffer buffer = new Buffer();
        copyTo(buffer, 0L, Math.min(32L, size()));
        throw new EOFException("\\n not found: limit=" + Math.min(size(), j) + " content=" + buffer.readByteString().hex() + (char) 8230);
    }

    @Override
    public Buffer writeByte(int i) {
        f fVarWritableSegment = writableSegment(1);
        byte[] bArr = fVarWritableSegment.a;
        int i2 = fVarWritableSegment.c;
        fVarWritableSegment.c = i2 + 1;
        bArr[i2] = (byte) i;
        this.size++;
        return this;
    }

    @Override
    public Buffer writeDecimalLong(long j) {
        boolean z;
        if (j == 0) {
            return writeByte(48);
        }
        int i = 1;
        if (j < 0) {
            j = -j;
            if (j < 0) {
                return writeUtf8("-9223372036854775808");
            }
            z = true;
        } else {
            z = false;
        }
        if (j >= 100000000) {
            i = j < 1000000000000L ? j < 10000000000L ? j < 1000000000 ? 9 : 10 : j < 100000000000L ? 11 : 12 : j < 1000000000000000L ? j < 10000000000000L ? 13 : j < 100000000000000L ? 14 : 15 : j < 100000000000000000L ? j < 10000000000000000L ? 16 : 17 : j < 1000000000000000000L ? 18 : 19;
        } else if (j >= 10000) {
            i = j < 1000000 ? j < 100000 ? 5 : 6 : j < 10000000 ? 7 : 8;
        } else if (j >= 100) {
            i = j < 1000 ? 3 : 4;
        } else if (j >= 10) {
            i = 2;
        }
        if (z) {
            i++;
        }
        f fVarWritableSegment = writableSegment(i);
        byte[] bArr = fVarWritableSegment.a;
        int i2 = fVarWritableSegment.c + i;
        while (j != 0) {
            i2--;
            bArr[i2] = DIGITS[(int) (j % 10)];
            j /= 10;
        }
        if (z) {
            bArr[i2 - 1] = 45;
        }
        fVarWritableSegment.c += i;
        this.size += (long) i;
        return this;
    }

    @Override
    public Buffer writeHexadecimalUnsignedLong(long j) {
        if (j == 0) {
            return writeByte(48);
        }
        int iNumberOfTrailingZeros = (Long.numberOfTrailingZeros(Long.highestOneBit(j)) / 4) + 1;
        f fVarWritableSegment = writableSegment(iNumberOfTrailingZeros);
        byte[] bArr = fVarWritableSegment.a;
        int i = fVarWritableSegment.c;
        for (int i2 = (i + iNumberOfTrailingZeros) - 1; i2 >= i; i2--) {
            bArr[i2] = DIGITS[(int) (15 & j)];
            j >>>= 4;
        }
        fVarWritableSegment.c += iNumberOfTrailingZeros;
        this.size += (long) iNumberOfTrailingZeros;
        return this;
    }

    @Override
    public Buffer writeInt(int i) {
        f fVarWritableSegment = writableSegment(4);
        byte[] bArr = fVarWritableSegment.a;
        int i2 = fVarWritableSegment.c;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        bArr[i2 + 1] = (byte) ((i >>> 16) & 255);
        bArr[i2 + 2] = (byte) ((i >>> 8) & 255);
        bArr[i2 + 3] = (byte) (i & 255);
        fVarWritableSegment.c = i2 + 4;
        this.size += 4;
        return this;
    }

    @Override
    public Buffer writeIntLe(int i) {
        return writeInt(i.c(i));
    }

    @Override
    public Buffer writeLong(long j) {
        f fVarWritableSegment = writableSegment(8);
        byte[] bArr = fVarWritableSegment.a;
        int i = fVarWritableSegment.c;
        bArr[i] = (byte) ((j >>> 56) & 255);
        bArr[i + 1] = (byte) ((j >>> 48) & 255);
        bArr[i + 2] = (byte) ((j >>> 40) & 255);
        bArr[i + 3] = (byte) ((j >>> 32) & 255);
        bArr[i + 4] = (byte) ((j >>> 24) & 255);
        bArr[i + 5] = (byte) ((j >>> 16) & 255);
        bArr[i + 6] = (byte) ((j >>> 8) & 255);
        bArr[i + 7] = (byte) (j & 255);
        fVarWritableSegment.c = i + 8;
        this.size += 8;
        return this;
    }

    @Override
    public Buffer writeLongLe(long j) {
        return writeLong(i.d(j));
    }

    @Override
    public Buffer writeShort(int i) {
        f fVarWritableSegment = writableSegment(2);
        byte[] bArr = fVarWritableSegment.a;
        int i2 = fVarWritableSegment.c;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        bArr[i2 + 1] = (byte) (i & 255);
        fVarWritableSegment.c = i2 + 2;
        this.size += 2;
        return this;
    }

    @Override
    public Buffer writeShortLe(int i) {
        return writeShort((int) i.e((short) i));
    }

    public final Buffer writeTo(OutputStream outputStream, long j) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        i.b(this.size, 0L, j);
        f fVar = this.head;
        while (j > 0) {
            int iMin = (int) Math.min(j, fVar.c - fVar.b);
            outputStream.write(fVar.a, fVar.b, iMin);
            int i = fVar.b + iMin;
            fVar.b = i;
            long j2 = iMin;
            this.size -= j2;
            j -= j2;
            if (i == fVar.c) {
                f fVarB = fVar.b();
                this.head = fVarB;
                g.a(fVar);
                fVar = fVarB;
            }
        }
        return this;
    }

    @Override
    public Buffer writeUtf8CodePoint(int i) {
        if (i < 128) {
            writeByte(i);
        } else if (i < 2048) {
            writeByte((i >> 6) | 192);
            writeByte((i & 63) | 128);
        } else if (i < 65536) {
            if (i < 55296 || i > 57343) {
                writeByte((i >> 12) | 224);
                writeByte(((i >> 6) & 63) | 128);
                writeByte((i & 63) | 128);
            } else {
                writeByte(63);
            }
        } else {
            if (i > 1114111) {
                throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(i));
            }
            writeByte((i >> 18) | 240);
            writeByte(((i >> 12) & 63) | 128);
            writeByte(((i >> 6) & 63) | 128);
            writeByte((i & 63) | 128);
        }
        return this;
    }

    @Override
    public long indexOf(byte b2, long j, long j2) {
        f fVar;
        long j3 = 0;
        if (j >= 0 && j2 >= j) {
            long j4 = this.size;
            long j5 = j2 > j4 ? j4 : j2;
            if (j == j5 || (fVar = this.head) == null) {
                return -1L;
            }
            if (j4 - j < j) {
                while (j4 > j) {
                    fVar = fVar.g;
                    j4 -= (long) (fVar.c - fVar.b);
                }
            } else {
                while (true) {
                    long j6 = ((long) (fVar.c - fVar.b)) + j3;
                    if (j6 >= j) {
                        break;
                    }
                    fVar = fVar.f;
                    j3 = j6;
                }
                j4 = j3;
            }
            long j7 = j;
            while (j4 < j5) {
                byte[] bArr = fVar.a;
                int iMin = (int) Math.min(fVar.c, (((long) fVar.b) + j5) - j4);
                for (int i = (int) ((((long) fVar.b) + j7) - j4); i < iMin; i++) {
                    if (bArr[i] == b2) {
                        return ((long) (i - fVar.b)) + j4;
                    }
                }
                j4 += (long) (fVar.c - fVar.b);
                fVar = fVar.f;
                j7 = j4;
            }
            return -1L;
        }
        throw new IllegalArgumentException(String.format("size=%s fromIndex=%s toIndex=%s", Long.valueOf(this.size), Long.valueOf(j), Long.valueOf(j2)));
    }

    @Override
    public byte[] readByteArray(long j) throws EOFException {
        i.b(this.size, 0L, j);
        if (j <= 2147483647L) {
            byte[] bArr = new byte[(int) j];
            readFully(bArr);
            return bArr;
        }
        throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + j);
    }

    @Override
    public String readString(long j, Charset charset) {
        i.b(this.size, 0L, j);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        if (j > 2147483647L) {
            throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + j);
        }
        if (j == 0) {
            return "";
        }
        f fVar = this.head;
        int i = fVar.b;
        if (((long) i) + j > fVar.c) {
            return new String(readByteArray(j), charset);
        }
        String str = new String(fVar.a, i, (int) j, charset);
        int i2 = (int) (((long) fVar.b) + j);
        fVar.b = i2;
        this.size -= j;
        if (i2 == fVar.c) {
            this.head = fVar.b();
            g.a(fVar);
        }
        return str;
    }

    @Override
    public String readUtf8(long j) {
        return readString(j, i.a);
    }

    @Override
    public Buffer writeString(String str, Charset charset) {
        return writeString(str, 0, str.length(), charset);
    }

    @Override
    public Buffer writeUtf8(String str) {
        return writeUtf8(str, 0, str.length());
    }

    private void readFrom(InputStream inputStream, long j, boolean z) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        while (true) {
            if (j <= 0 && !z) {
                return;
            }
            f fVarWritableSegment = writableSegment(1);
            int i = inputStream.read(fVarWritableSegment.a, fVarWritableSegment.c, (int) Math.min(j, 8192 - fVarWritableSegment.c));
            if (i == -1) {
                if (!z) {
                    throw new EOFException();
                }
                return;
            } else {
                fVarWritableSegment.c += i;
                long j2 = i;
                this.size += j2;
                j -= j2;
            }
        }
    }

    String readUtf8Line(long j) {
        if (j > 0) {
            long j2 = j - 1;
            if (getByte(j2) == 13) {
                String utf8 = readUtf8(j2);
                skip(2L);
                return utf8;
            }
        }
        String utf82 = readUtf8(j);
        skip(1L);
        return utf82;
    }

    public final ByteString snapshot(int i) {
        if (i == 0) {
            return ByteString.EMPTY;
        }
        return new h(this, i);
    }

    @Override
    public Buffer write(ByteString byteString) {
        if (byteString != null) {
            byteString.write(this);
            return this;
        }
        throw new IllegalArgumentException("byteString == null");
    }

    @Override
    public Buffer writeString(String str, int i, int i2, Charset charset) {
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalAccessError("beginIndex < 0: " + i);
        }
        if (i2 >= i) {
            if (i2 <= str.length()) {
                if (charset != null) {
                    if (charset.equals(i.a)) {
                        return writeUtf8(str, i, i2);
                    }
                    byte[] bytes = str.substring(i, i2).getBytes(charset);
                    return write(bytes, 0, bytes.length);
                }
                throw new IllegalArgumentException("charset == null");
            }
            throw new IllegalArgumentException("endIndex > string.length: " + i2 + " > " + str.length());
        }
        throw new IllegalArgumentException("endIndex < beginIndex: " + i2 + " < " + i);
    }

    @Override
    public Buffer writeUtf8(String str, int i, int i2) {
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalArgumentException("beginIndex < 0: " + i);
        }
        if (i2 >= i) {
            if (i2 > str.length()) {
                throw new IllegalArgumentException("endIndex > string.length: " + i2 + " > " + str.length());
            }
            while (i < i2) {
                char cCharAt = str.charAt(i);
                if (cCharAt < 128) {
                    f fVarWritableSegment = writableSegment(1);
                    byte[] bArr = fVarWritableSegment.a;
                    int i3 = fVarWritableSegment.c - i;
                    int iMin = Math.min(i2, 8192 - i3);
                    int i4 = i + 1;
                    bArr[i + i3] = (byte) cCharAt;
                    while (i4 < iMin) {
                        char cCharAt2 = str.charAt(i4);
                        if (cCharAt2 >= 128) {
                            break;
                        }
                        bArr[i4 + i3] = (byte) cCharAt2;
                        i4++;
                    }
                    int i5 = fVarWritableSegment.c;
                    int i6 = (i3 + i4) - i5;
                    fVarWritableSegment.c = i5 + i6;
                    this.size += (long) i6;
                    i = i4;
                } else {
                    if (cCharAt < 2048) {
                        writeByte((cCharAt >> 6) | 192);
                        writeByte((cCharAt & '?') | 128);
                    } else if (cCharAt >= 55296 && cCharAt <= 57343) {
                        int i7 = i + 1;
                        char cCharAt3 = i7 < i2 ? str.charAt(i7) : (char) 0;
                        if (cCharAt <= 56319 && cCharAt3 >= 56320 && cCharAt3 <= 57343) {
                            int i8 = (((cCharAt & 10239) << 10) | (9215 & cCharAt3)) + 65536;
                            writeByte((i8 >> 18) | 240);
                            writeByte(((i8 >> 12) & 63) | 128);
                            writeByte(((i8 >> 6) & 63) | 128);
                            writeByte((i8 & 63) | 128);
                            i += 2;
                        } else {
                            writeByte(63);
                            i = i7;
                        }
                    } else {
                        writeByte((cCharAt >> '\f') | 224);
                        writeByte(((cCharAt >> 6) & 63) | 128);
                        writeByte((cCharAt & '?') | 128);
                    }
                    i++;
                }
            }
            return this;
        }
        throw new IllegalArgumentException("endIndex < beginIndex: " + i2 + " < " + i);
    }

    private boolean rangeEquals(f fVar, int i, ByteString byteString, int i2, int i3) {
        int i4 = fVar.c;
        byte[] bArr = fVar.a;
        while (i2 < i3) {
            if (i == i4) {
                fVar = fVar.f;
                byte[] bArr2 = fVar.a;
                bArr = bArr2;
                i = fVar.b;
                i4 = fVar.c;
            }
            if (bArr[i] != byteString.getByte(i2)) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }

    @Override
    public void readFully(byte[] bArr) throws EOFException {
        int i = 0;
        while (i < bArr.length) {
            int i2 = read(bArr, i, bArr.length - i);
            if (i2 == -1) {
                throw new EOFException();
            }
            i += i2;
        }
    }

    @Override
    public Buffer write(byte[] bArr) {
        if (bArr != null) {
            return write(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("source == null");
    }

    @Override
    public Buffer write(byte[] bArr, int i, int i2) {
        if (bArr != null) {
            long j = i2;
            i.b(bArr.length, i, j);
            int i3 = i2 + i;
            while (i < i3) {
                f fVarWritableSegment = writableSegment(1);
                int iMin = Math.min(i3 - i, 8192 - fVarWritableSegment.c);
                System.arraycopy(bArr, i, fVarWritableSegment.a, fVarWritableSegment.c, iMin);
                i += iMin;
                fVarWritableSegment.c += iMin;
            }
            this.size += j;
            return this;
        }
        throw new IllegalArgumentException("source == null");
    }

    public final Buffer copyTo(Buffer buffer, long j, long j2) {
        if (buffer != null) {
            i.b(this.size, j, j2);
            if (j2 == 0) {
                return this;
            }
            buffer.size += j2;
            f fVar = this.head;
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
                f fVarD = fVar.d();
                int i3 = (int) (((long) fVarD.b) + j);
                fVarD.b = i3;
                fVarD.c = Math.min(i3 + ((int) j2), fVarD.c);
                f fVar2 = buffer.head;
                if (fVar2 == null) {
                    fVarD.g = fVarD;
                    fVarD.f = fVarD;
                    buffer.head = fVarD;
                } else {
                    fVar2.g.c(fVarD);
                }
                j2 -= (long) (fVarD.c - fVarD.b);
                fVar = fVar.f;
                j = 0;
            }
            return this;
        }
        throw new IllegalArgumentException("out == null");
    }

    @Override
    public int read(ByteBuffer byteBuffer) {
        f fVar = this.head;
        if (fVar == null) {
            return -1;
        }
        int iMin = Math.min(byteBuffer.remaining(), fVar.c - fVar.b);
        byteBuffer.put(fVar.a, fVar.b, iMin);
        int i = fVar.b + iMin;
        fVar.b = i;
        this.size -= (long) iMin;
        if (i == fVar.c) {
            this.head = fVar.b();
            g.a(fVar);
        }
        return iMin;
    }

    @Override
    public int write(ByteBuffer byteBuffer) {
        if (byteBuffer != null) {
            int iRemaining = byteBuffer.remaining();
            int i = iRemaining;
            while (i > 0) {
                f fVarWritableSegment = writableSegment(1);
                int iMin = Math.min(i, 8192 - fVarWritableSegment.c);
                byteBuffer.get(fVarWritableSegment.a, fVarWritableSegment.c, iMin);
                i -= iMin;
                fVarWritableSegment.c += iMin;
            }
            this.size += (long) iRemaining;
            return iRemaining;
        }
        throw new IllegalArgumentException("source == null");
    }

    @Override
    public long indexOf(ByteString byteString) {
        return indexOf(byteString, 0L);
    }

    @Override
    public long indexOf(ByteString byteString, long j) {
        byte[] bArr;
        if (byteString.size() == 0) {
            throw new IllegalArgumentException("bytes is empty");
        }
        long j2 = 0;
        if (j >= 0) {
            f fVar = this.head;
            long j3 = -1;
            if (fVar == null) {
                return -1L;
            }
            long j4 = this.size;
            if (j4 - j < j) {
                while (j4 > j) {
                    fVar = fVar.g;
                    j4 -= (long) (fVar.c - fVar.b);
                }
            } else {
                while (true) {
                    long j5 = ((long) (fVar.c - fVar.b)) + j2;
                    if (j5 >= j) {
                        break;
                    }
                    fVar = fVar.f;
                    j2 = j5;
                }
                j4 = j2;
            }
            byte b2 = byteString.getByte(0);
            int size = byteString.size();
            long j6 = 1 + (this.size - ((long) size));
            long j7 = j;
            f fVar2 = fVar;
            long j8 = j4;
            while (j8 < j6) {
                byte[] bArr2 = fVar2.a;
                int iMin = (int) Math.min(fVar2.c, (((long) fVar2.b) + j6) - j8);
                int i = (int) ((((long) fVar2.b) + j7) - j8);
                while (i < iMin) {
                    if (bArr2[i] == b2) {
                        bArr = bArr2;
                        if (rangeEquals(fVar2, i + 1, byteString, 1, size)) {
                            return ((long) (i - fVar2.b)) + j8;
                        }
                    } else {
                        bArr = bArr2;
                    }
                    i++;
                    bArr2 = bArr;
                }
                j8 += (long) (fVar2.c - fVar2.b);
                fVar2 = fVar2.f;
                j7 = j8;
                j3 = -1;
            }
            return j3;
        }
        throw new IllegalArgumentException("fromIndex < 0");
    }

    @Override
    public long read(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (j >= 0) {
            long j2 = this.size;
            if (j2 == 0) {
                return -1L;
            }
            if (j > j2) {
                j = j2;
            }
            buffer.write(this, j);
            return j;
        }
        throw new IllegalArgumentException("byteCount < 0: " + j);
    }

    @Override
    public BufferedSink write(Source source, long j) throws EOFException {
        while (j > 0) {
            long j2 = source.read(this, j);
            if (j2 == -1) {
                throw new EOFException();
            }
            j -= j2;
        }
        return this;
    }

    @Override
    public void write(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (buffer != this) {
            i.b(buffer.size, 0L, j);
            while (j > 0) {
                f fVar = buffer.head;
                if (j < fVar.c - fVar.b) {
                    f fVar2 = this.head;
                    f fVar3 = fVar2 != null ? fVar2.g : null;
                    if (fVar3 != null && fVar3.e) {
                        if ((((long) fVar3.c) + j) - ((long) (fVar3.d ? 0 : fVar3.b)) <= 8192) {
                            fVar.g(fVar3, (int) j);
                            buffer.size -= j;
                            this.size += j;
                            return;
                        }
                    }
                    buffer.head = fVar.e((int) j);
                }
                f fVar4 = buffer.head;
                long j2 = fVar4.c - fVar4.b;
                buffer.head = fVar4.b();
                f fVar5 = this.head;
                if (fVar5 == null) {
                    this.head = fVar4;
                    fVar4.g = fVar4;
                    fVar4.f = fVar4;
                } else {
                    fVar5.g.c(fVar4).a();
                }
                buffer.size -= j2;
                this.size += j2;
                j -= j2;
            }
            return;
        }
        throw new IllegalArgumentException("source == this");
    }
}
