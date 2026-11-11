package org.cocos2dx.okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

final class h extends ByteString {
    final transient byte[][] i;
    final transient int[] j;

    h(Buffer buffer, int i) {
        super(null);
        i.b(buffer.size, 0L, i);
        f fVar = buffer.head;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            int i5 = fVar.c;
            int i6 = fVar.b;
            if (i5 == i6) {
                throw new AssertionError("s.limit == s.pos");
            }
            i3 += i5 - i6;
            i4++;
            fVar = fVar.f;
        }
        this.i = new byte[i4][];
        this.j = new int[i4 * 2];
        f fVar2 = buffer.head;
        int i7 = 0;
        while (i2 < i) {
            byte[][] bArr = this.i;
            bArr[i7] = fVar2.a;
            int i8 = fVar2.c;
            int i9 = fVar2.b;
            i2 += i8 - i9;
            if (i2 > i) {
                i2 = i;
            }
            int[] iArr = this.j;
            iArr[i7] = i2;
            iArr[bArr.length + i7] = i9;
            fVar2.d = true;
            i7++;
            fVar2 = fVar2.f;
        }
    }

    private int a(int i) {
        int iBinarySearch = Arrays.binarySearch(this.j, 0, this.i.length, i + 1);
        return iBinarySearch >= 0 ? iBinarySearch : ~iBinarySearch;
    }

    private ByteString b() {
        return new ByteString(toByteArray());
    }

    @Override
    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
    }

    @Override
    public String base64() {
        return b().base64();
    }

    @Override
    public String base64Url() {
        return b().base64Url();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ByteString) {
            ByteString byteString = (ByteString) obj;
            if (byteString.size() == size() && rangeEquals(0, byteString, 0, size())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte getByte(int i) {
        i.b(this.j[this.i.length - 1], i, 1L);
        int iA = a(i);
        int i2 = iA == 0 ? 0 : this.j[iA - 1];
        int[] iArr = this.j;
        byte[][] bArr = this.i;
        return bArr[iA][(i - i2) + iArr[bArr.length + iA]];
    }

    @Override
    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        int length = this.i.length;
        int i2 = 0;
        int i3 = 0;
        int i4 = 1;
        while (i2 < length) {
            byte[] bArr = this.i[i2];
            int[] iArr = this.j;
            int i5 = iArr[length + i2];
            int i6 = iArr[i2];
            int i7 = (i6 - i3) + i5;
            while (i5 < i7) {
                i4 = (i4 * 31) + bArr[i5];
                i5++;
            }
            i2++;
            i3 = i6;
        }
        this.hashCode = i4;
        return i4;
    }

    @Override
    public String hex() {
        return b().hex();
    }

    @Override
    public ByteString hmacSha1(ByteString byteString) {
        return b().hmacSha1(byteString);
    }

    @Override
    public ByteString hmacSha256(ByteString byteString) {
        return b().hmacSha256(byteString);
    }

    @Override
    public int indexOf(byte[] bArr, int i) {
        return b().indexOf(bArr, i);
    }

    @Override
    byte[] internalArray() {
        return toByteArray();
    }

    @Override
    public int lastIndexOf(byte[] bArr, int i) {
        return b().lastIndexOf(bArr, i);
    }

    @Override
    public ByteString md5() {
        return b().md5();
    }

    @Override
    public boolean rangeEquals(int i, ByteString byteString, int i2, int i3) {
        if (i < 0 || i > size() - i3) {
            return false;
        }
        int iA = a(i);
        while (i3 > 0) {
            int i4 = iA == 0 ? 0 : this.j[iA - 1];
            int iMin = Math.min(i3, ((this.j[iA] - i4) + i4) - i);
            int[] iArr = this.j;
            byte[][] bArr = this.i;
            if (!byteString.rangeEquals(i2, bArr[iA], (i - i4) + iArr[bArr.length + iA], iMin)) {
                return false;
            }
            i += iMin;
            i2 += iMin;
            i3 -= iMin;
            iA++;
        }
        return true;
    }

    @Override
    public ByteString sha1() {
        return b().sha1();
    }

    @Override
    public ByteString sha256() {
        return b().sha256();
    }

    @Override
    public int size() {
        return this.j[this.i.length - 1];
    }

    @Override
    public String string(Charset charset) {
        return b().string(charset);
    }

    @Override
    public ByteString substring(int i) {
        return b().substring(i);
    }

    @Override
    public ByteString toAsciiLowercase() {
        return b().toAsciiLowercase();
    }

    @Override
    public ByteString toAsciiUppercase() {
        return b().toAsciiUppercase();
    }

    @Override
    public byte[] toByteArray() {
        int[] iArr = this.j;
        byte[][] bArr = this.i;
        byte[] bArr2 = new byte[iArr[bArr.length - 1]];
        int length = bArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int[] iArr2 = this.j;
            int i3 = iArr2[length + i];
            int i4 = iArr2[i];
            System.arraycopy(this.i[i], i3, bArr2, i2, i4 - i2);
            i++;
            i2 = i4;
        }
        return bArr2;
    }

    @Override
    public String toString() {
        return b().toString();
    }

    @Override
    public String utf8() {
        return b().utf8();
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        int length = this.i.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int[] iArr = this.j;
            int i3 = iArr[length + i];
            int i4 = iArr[i];
            outputStream.write(this.i[i], i3, i4 - i2);
            i++;
            i2 = i4;
        }
    }

    @Override
    public ByteString substring(int i, int i2) {
        return b().substring(i, i2);
    }

    @Override
    void write(Buffer buffer) {
        int length = this.i.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int[] iArr = this.j;
            int i3 = iArr[length + i];
            int i4 = iArr[i];
            f fVar = new f(this.i[i], i3, (i3 + i4) - i2, true, false);
            f fVar2 = buffer.head;
            if (fVar2 == null) {
                fVar.g = fVar;
                fVar.f = fVar;
                buffer.head = fVar;
            } else {
                fVar2.g.c(fVar);
            }
            i++;
            i2 = i4;
        }
        buffer.size += (long) i2;
    }

    @Override
    public boolean rangeEquals(int i, byte[] bArr, int i2, int i3) {
        if (i < 0 || i > size() - i3 || i2 < 0 || i2 > bArr.length - i3) {
            return false;
        }
        int iA = a(i);
        while (i3 > 0) {
            int i4 = iA == 0 ? 0 : this.j[iA - 1];
            int iMin = Math.min(i3, ((this.j[iA] - i4) + i4) - i);
            int[] iArr = this.j;
            byte[][] bArr2 = this.i;
            if (!i.a(bArr2[iA], (i - i4) + iArr[bArr2.length + iA], bArr, i2, iMin)) {
                return false;
            }
            i += iMin;
            i2 += iMin;
            i3 -= iMin;
            iA++;
        }
        return true;
    }
}
