package org.cocos2dx.okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

final class e implements BufferedSource {
    public final Buffer i = new Buffer();
    public final Source j;
    boolean k;

    e(Source source) {
        if (source == null) {
            throw new NullPointerException("source == null");
        }
        this.j = source;
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
        this.k = true;
        this.j.close();
        this.i.clear();
    }

    @Override
    public boolean exhausted() {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        return this.i.exhausted() && this.j.read(this.i, 8192L) == -1;
    }

    @Override
    public long indexOf(byte b) {
        return indexOf(b, 0L, Long.MAX_VALUE);
    }

    @Override
    public long indexOfElement(ByteString byteString) {
        return indexOfElement(byteString, 0L);
    }

    @Override
    public InputStream inputStream() {
        return new a();
    }

    @Override
    public boolean isOpen() {
        return !this.k;
    }

    @Override
    public boolean rangeEquals(long j, ByteString byteString) {
        return rangeEquals(j, byteString, 0, byteString.size());
    }

    @Override
    public long read(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        }
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        Buffer buffer2 = this.i;
        if (buffer2.size == 0 && this.j.read(buffer2, 8192L) == -1) {
            return -1L;
        }
        return this.i.read(buffer, Math.min(j, this.i.size));
    }

    @Override
    public long readAll(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        long j = 0;
        while (this.j.read(this.i, 8192L) != -1) {
            long jCompleteSegmentByteCount = this.i.completeSegmentByteCount();
            if (jCompleteSegmentByteCount > 0) {
                j += jCompleteSegmentByteCount;
                sink.write(this.i, jCompleteSegmentByteCount);
            }
        }
        if (this.i.size() <= 0) {
            return j;
        }
        long size = j + this.i.size();
        Buffer buffer = this.i;
        sink.write(buffer, buffer.size());
        return size;
    }

    @Override
    public byte readByte() throws EOFException {
        require(1L);
        return this.i.readByte();
    }

    @Override
    public byte[] readByteArray() {
        this.i.writeAll(this.j);
        return this.i.readByteArray();
    }

    @Override
    public ByteString readByteString() {
        this.i.writeAll(this.j);
        return this.i.readByteString();
    }

    @Override
    public long readDecimalLong() throws EOFException {
        require(1L);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (!request(i2)) {
                break;
            }
            byte b = this.i.getByte(i);
            if ((b < 48 || b > 57) && !(i == 0 && b == 45)) {
                if (i != 0) {
                    break;
                }
                throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", Byte.valueOf(b)));
            }
            i = i2;
        }
        return this.i.readDecimalLong();
    }

    @Override
    public void readFully(byte[] bArr) throws EOFException {
        try {
            require(bArr.length);
            this.i.readFully(bArr);
        } catch (EOFException e) {
            int i = 0;
            while (true) {
                Buffer buffer = this.i;
                long j = buffer.size;
                if (j <= 0) {
                    throw e;
                }
                int i2 = buffer.read(bArr, i, (int) j);
                if (i2 == -1) {
                    throw new AssertionError();
                }
                i += i2;
            }
        }
    }

    @Override
    public long readHexadecimalUnsignedLong() throws EOFException {
        require(1L);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (!request(i2)) {
                break;
            }
            byte b = this.i.getByte(i);
            if ((b < 48 || b > 57) && ((b < 97 || b > 102) && (b < 65 || b > 70))) {
                if (i != 0) {
                    break;
                }
                throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", Byte.valueOf(b)));
            }
            i = i2;
        }
        return this.i.readHexadecimalUnsignedLong();
    }

    @Override
    public int readInt() throws EOFException {
        require(4L);
        return this.i.readInt();
    }

    @Override
    public int readIntLe() throws EOFException {
        require(4L);
        return this.i.readIntLe();
    }

    @Override
    public long readLong() throws EOFException {
        require(8L);
        return this.i.readLong();
    }

    @Override
    public long readLongLe() throws EOFException {
        require(8L);
        return this.i.readLongLe();
    }

    @Override
    public short readShort() throws EOFException {
        require(2L);
        return this.i.readShort();
    }

    @Override
    public short readShortLe() throws EOFException {
        require(2L);
        return this.i.readShortLe();
    }

    @Override
    public String readString(Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        this.i.writeAll(this.j);
        return this.i.readString(charset);
    }

    @Override
    public String readUtf8() {
        this.i.writeAll(this.j);
        return this.i.readUtf8();
    }

    @Override
    public int readUtf8CodePoint() throws EOFException {
        require(1L);
        byte b = this.i.getByte(0L);
        if ((b & 224) == 192) {
            require(2L);
        } else if ((b & 240) == 224) {
            require(3L);
        } else if ((b & 248) == 240) {
            require(4L);
        }
        return this.i.readUtf8CodePoint();
    }

    @Override
    public String readUtf8Line() {
        long jIndexOf = indexOf((byte) 10);
        if (jIndexOf != -1) {
            return this.i.readUtf8Line(jIndexOf);
        }
        long j = this.i.size;
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
        Buffer buffer;
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        }
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        do {
            buffer = this.i;
            if (buffer.size >= j) {
                return true;
            }
        } while (this.j.read(buffer, 8192L) != -1);
        return false;
    }

    @Override
    public void require(long j) throws EOFException {
        if (!request(j)) {
            throw new EOFException();
        }
    }

    @Override
    public int select(Options options) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        do {
            int iSelectPrefix = this.i.selectPrefix(options, true);
            if (iSelectPrefix == -1) {
                return -1;
            }
            if (iSelectPrefix != -2) {
                this.i.skip(options.byteStrings[iSelectPrefix].size());
                return iSelectPrefix;
            }
        } while (this.j.read(this.i, 8192L) != -1);
        return -1;
    }

    @Override
    public void skip(long j) throws EOFException {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        while (j > 0) {
            Buffer buffer = this.i;
            if (buffer.size == 0 && this.j.read(buffer, 8192L) == -1) {
                throw new EOFException();
            }
            long jMin = Math.min(j, this.i.size());
            this.i.skip(jMin);
            j -= jMin;
        }
    }

    @Override
    public Timeout timeout() {
        return this.j.timeout();
    }

    public String toString() {
        return "buffer(" + this.j + ")";
    }

    @Override
    public long indexOf(byte b, long j) {
        return indexOf(b, j, Long.MAX_VALUE);
    }

    @Override
    public long indexOfElement(ByteString byteString, long j) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        while (true) {
            long jIndexOfElement = this.i.indexOfElement(byteString, j);
            if (jIndexOfElement != -1) {
                return jIndexOfElement;
            }
            Buffer buffer = this.i;
            long j2 = buffer.size;
            if (this.j.read(buffer, 8192L) == -1) {
                return -1L;
            }
            j = Math.max(j, j2);
        }
    }

    @Override
    public boolean rangeEquals(long j, ByteString byteString, int i, int i2) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        if (j < 0 || i < 0 || i2 < 0 || byteString.size() - i < i2) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            long j2 = ((long) i3) + j;
            if (!request(1 + j2) || this.i.getByte(j2) != byteString.getByte(i + i3)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String readUtf8LineStrict(long j) throws EOFException {
        if (j < 0) {
            throw new IllegalArgumentException("limit < 0: " + j);
        }
        long j2 = j == Long.MAX_VALUE ? Long.MAX_VALUE : j + 1;
        long jIndexOf = indexOf((byte) 10, 0L, j2);
        if (jIndexOf != -1) {
            return this.i.readUtf8Line(jIndexOf);
        }
        if (j2 < Long.MAX_VALUE && request(j2) && this.i.getByte(j2 - 1) == 13 && request(1 + j2) && this.i.getByte(j2) == 10) {
            return this.i.readUtf8Line(j2);
        }
        Buffer buffer = new Buffer();
        Buffer buffer2 = this.i;
        buffer2.copyTo(buffer, 0L, Math.min(32L, buffer2.size()));
        throw new EOFException("\\n not found: limit=" + Math.min(this.i.size(), j) + " content=" + buffer.readByteString().hex() + (char) 8230);
    }

    @Override
    public long indexOf(byte b, long j, long j2) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        if (j < 0 || j2 < j) {
            throw new IllegalArgumentException(String.format("fromIndex=%s toIndex=%s", Long.valueOf(j), Long.valueOf(j2)));
        }
        while (j < j2) {
            long jIndexOf = this.i.indexOf(b, j, j2);
            if (jIndexOf != -1) {
                return jIndexOf;
            }
            Buffer buffer = this.i;
            long j3 = buffer.size;
            if (j3 >= j2 || this.j.read(buffer, 8192L) == -1) {
                break;
            }
            j = Math.max(j, j3);
        }
        return -1L;
    }

    @Override
    public byte[] readByteArray(long j) throws EOFException {
        require(j);
        return this.i.readByteArray(j);
    }

    @Override
    public ByteString readByteString(long j) throws EOFException {
        require(j);
        return this.i.readByteString(j);
    }

    @Override
    public String readUtf8(long j) throws EOFException {
        require(j);
        return this.i.readUtf8(j);
    }

    @Override
    public String readString(long j, Charset charset) throws EOFException {
        require(j);
        if (charset != null) {
            return this.i.readString(j, charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    class a extends InputStream {
        a() {
        }

        @Override
        public int available() throws IOException {
            e eVar = e.this;
            if (eVar.k) {
                throw new IOException("closed");
            }
            return (int) Math.min(eVar.i.size, 2147483647L);
        }

        @Override
        public void close() {
            e.this.close();
        }

        @Override
        public int read() throws IOException {
            e eVar = e.this;
            if (eVar.k) {
                throw new IOException("closed");
            }
            Buffer buffer = eVar.i;
            if (buffer.size == 0 && eVar.j.read(buffer, 8192L) == -1) {
                return -1;
            }
            return e.this.i.readByte() & 255;
        }

        public String toString() {
            return e.this + ".inputStream()";
        }

        @Override
        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (!e.this.k) {
                i.b(bArr.length, i, i2);
                e eVar = e.this;
                Buffer buffer = eVar.i;
                if (buffer.size == 0 && eVar.j.read(buffer, 8192L) == -1) {
                    return -1;
                }
                return e.this.i.read(bArr, i, i2);
            }
            throw new IOException("closed");
        }
    }

    @Override
    public void readFully(Buffer buffer, long j) throws EOFException {
        try {
            require(j);
            this.i.readFully(buffer, j);
        } catch (EOFException e) {
            buffer.writeAll(this.i);
            throw e;
        }
    }

    @Override
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override
    public int read(byte[] bArr, int i, int i2) {
        long j = i2;
        i.b(bArr.length, i, j);
        Buffer buffer = this.i;
        if (buffer.size == 0 && this.j.read(buffer, 8192L) == -1) {
            return -1;
        }
        return this.i.read(bArr, i, (int) Math.min(j, this.i.size));
    }

    @Override
    public long indexOf(ByteString byteString) {
        return indexOf(byteString, 0L);
    }

    @Override
    public long indexOf(ByteString byteString, long j) {
        if (this.k) {
            throw new IllegalStateException("closed");
        }
        while (true) {
            long jIndexOf = this.i.indexOf(byteString, j);
            if (jIndexOf != -1) {
                return jIndexOf;
            }
            Buffer buffer = this.i;
            long j2 = buffer.size;
            if (this.j.read(buffer, 8192L) == -1) {
                return -1L;
            }
            j = Math.max(j, (j2 - ((long) byteString.size())) + 1);
        }
    }

    @Override
    public int read(ByteBuffer byteBuffer) {
        Buffer buffer = this.i;
        if (buffer.size == 0 && this.j.read(buffer, 8192L) == -1) {
            return -1;
        }
        return this.i.read(byteBuffer);
    }
}
