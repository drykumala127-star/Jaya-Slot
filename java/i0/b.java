package i0;

import java.io.OutputStream;

final class b extends OutputStream {
    private long i = 0;

    b() {
    }

    long a() {
        return this.i;
    }

    @Override
    public void write(int i) {
        this.i++;
    }

    @Override
    public void write(byte[] bArr) {
        this.i += (long) bArr.length;
    }

    @Override
    public void write(byte[] bArr, int i, int i2) {
        int i3;
        if (i >= 0 && i <= bArr.length && i2 >= 0 && (i3 = i + i2) <= bArr.length && i3 >= 0) {
            this.i += (long) i2;
            return;
        }
        throw new IndexOutOfBoundsException();
    }
}
