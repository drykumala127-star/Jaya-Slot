package i0;

import i0.d;

public final class a {
    private int a;
    private d.a b = d.a.i;

    private static final class a implements d {
        private final int a;
        private final d.a b;

        a(int i, d.a aVar) {
            this.a = i;
            this.b = aVar;
        }

        @Override
        public Class annotationType() {
            return d.class;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof d)) {
                return false;
            }
            d dVar = (d) obj;
            return this.a == dVar.tag() && this.b.equals(dVar.intEncoding());
        }

        @Override
        public int hashCode() {
            return (14552422 ^ this.a) + (this.b.hashCode() ^ 2041407134);
        }

        @Override
        public d.a intEncoding() {
            return this.b;
        }

        @Override
        public int tag() {
            return this.a;
        }

        @Override
        public String toString() {
            return "@com.google.firebase.encoders.proto.Protobuf(tag=" + this.a + "intEncoding=" + this.b + ')';
        }
    }

    public static a b() {
        return new a();
    }

    public d a() {
        return new a(this.a, this.b);
    }

    public a c(int i) {
        this.a = i;
        return this;
    }
}
