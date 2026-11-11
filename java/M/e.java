package m;

import java.util.Arrays;
import m.p;

final class e extends p {
    private final String a;
    private final byte[] b;
    private final k.d c;

    static final class b extends p.a {
        private String a;
        private byte[] b;
        private k.d c;

        b() {
        }

        @Override
        public p a() {
            String str = "";
            if (this.a == null) {
                str = " backendName";
            }
            if (this.c == null) {
                str = str + " priority";
            }
            if (str.isEmpty()) {
                return new e(this.a, this.b, this.c, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        public p.a b(String str) {
            if (str == null) {
                throw new NullPointerException("Null backendName");
            }
            this.a = str;
            return this;
        }

        @Override
        public p.a c(byte[] bArr) {
            this.b = bArr;
            return this;
        }

        @Override
        public p.a d(k.d dVar) {
            if (dVar == null) {
                throw new NullPointerException("Null priority");
            }
            this.c = dVar;
            return this;
        }
    }

    e(String str, byte[] bArr, k.d dVar, a aVar) {
        this(str, bArr, dVar);
    }

    @Override
    public String b() {
        return this.a;
    }

    @Override
    public byte[] c() {
        return this.b;
    }

    @Override
    public k.d d() {
        return this.c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof p)) {
            return false;
        }
        p pVar = (p) obj;
        if (this.a.equals(pVar.b())) {
            if (Arrays.equals(this.b, pVar instanceof e ? ((e) pVar).b : pVar.c()) && this.c.equals(pVar.d())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((((this.a.hashCode() ^ 1000003) * 1000003) ^ Arrays.hashCode(this.b)) * 1000003) ^ this.c.hashCode();
    }

    private e(String str, byte[] bArr, k.d dVar) {
        this.a = str;
        this.b = bArr;
        this.c = dVar;
    }
}
