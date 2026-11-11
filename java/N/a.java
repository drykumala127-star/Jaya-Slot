package n;

import java.util.Arrays;
import n.f;

final class a extends f {
    private final Iterable a;
    private final byte[] b;

    static final class b extends f.a {
        private Iterable a;
        private byte[] b;

        b() {
        }

        @Override
        public f a() {
            String str = "";
            if (this.a == null) {
                str = " events";
            }
            if (str.isEmpty()) {
                return new a(this.a, this.b, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        public f.a b(Iterable iterable) {
            if (iterable == null) {
                throw new NullPointerException("Null events");
            }
            this.a = iterable;
            return this;
        }

        @Override
        public f.a c(byte[] bArr) {
            this.b = bArr;
            return this;
        }
    }

    a(Iterable iterable, byte[] bArr, a aVar) {
        this(iterable, bArr);
    }

    @Override
    public Iterable b() {
        return this.a;
    }

    @Override
    public byte[] c() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof f)) {
            return false;
        }
        f fVar = (f) obj;
        if (this.a.equals(fVar.b())) {
            if (Arrays.equals(this.b, fVar instanceof a ? ((a) fVar).b : fVar.c())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((this.a.hashCode() ^ 1000003) * 1000003) ^ Arrays.hashCode(this.b);
    }

    public String toString() {
        return "BackendRequest{events=" + this.a + ", extras=" + Arrays.toString(this.b) + "}";
    }

    private a(Iterable iterable, byte[] bArr) {
        this.a = iterable;
        this.b = bArr;
    }
}
