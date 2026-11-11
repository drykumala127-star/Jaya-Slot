package m;

import m.o;

final class d extends o {
    private final p a;
    private final String b;
    private final k.c c;
    private final k.e d;
    private final k.b e;

    static final class b extends o.a {
        private p a;
        private String b;
        private k.c c;
        private k.e d;
        private k.b e;

        b() {
        }

        @Override
        public o a() {
            String str = "";
            if (this.a == null) {
                str = " transportContext";
            }
            if (this.b == null) {
                str = str + " transportName";
            }
            if (this.c == null) {
                str = str + " event";
            }
            if (this.d == null) {
                str = str + " transformer";
            }
            if (this.e == null) {
                str = str + " encoding";
            }
            if (str.isEmpty()) {
                return new d(this.a, this.b, this.c, this.d, this.e, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        o.a b(k.b bVar) {
            if (bVar == null) {
                throw new NullPointerException("Null encoding");
            }
            this.e = bVar;
            return this;
        }

        @Override
        o.a c(k.c cVar) {
            if (cVar == null) {
                throw new NullPointerException("Null event");
            }
            this.c = cVar;
            return this;
        }

        @Override
        o.a d(k.e eVar) {
            if (eVar == null) {
                throw new NullPointerException("Null transformer");
            }
            this.d = eVar;
            return this;
        }

        @Override
        public o.a e(p pVar) {
            if (pVar == null) {
                throw new NullPointerException("Null transportContext");
            }
            this.a = pVar;
            return this;
        }

        @Override
        public o.a f(String str) {
            if (str == null) {
                throw new NullPointerException("Null transportName");
            }
            this.b = str;
            return this;
        }
    }

    d(p pVar, String str, k.c cVar, k.e eVar, k.b bVar, a aVar) {
        this(pVar, str, cVar, eVar, bVar);
    }

    @Override
    public k.b b() {
        return this.e;
    }

    @Override
    k.c c() {
        return this.c;
    }

    @Override
    k.e e() {
        return this.d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof o)) {
            return false;
        }
        o oVar = (o) obj;
        return this.a.equals(oVar.f()) && this.b.equals(oVar.g()) && this.c.equals(oVar.c()) && this.d.equals(oVar.e()) && this.e.equals(oVar.b());
    }

    @Override
    public p f() {
        return this.a;
    }

    @Override
    public String g() {
        return this.b;
    }

    public int hashCode() {
        return ((((((((this.a.hashCode() ^ 1000003) * 1000003) ^ this.b.hashCode()) * 1000003) ^ this.c.hashCode()) * 1000003) ^ this.d.hashCode()) * 1000003) ^ this.e.hashCode();
    }

    public String toString() {
        return "SendRequest{transportContext=" + this.a + ", transportName=" + this.b + ", event=" + this.c + ", transformer=" + this.d + ", encoding=" + this.e + "}";
    }

    private d(p pVar, String str, k.c cVar, k.e eVar, k.b bVar) {
        this.a = pVar;
        this.b = str;
        this.c = cVar;
        this.d = eVar;
        this.e = bVar;
    }
}
