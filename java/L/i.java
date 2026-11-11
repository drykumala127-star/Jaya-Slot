package l;

import l.o;

final class i extends o {
    private final o.c a;
    private final o.b b;

    static final class b extends o.a {
        private o.c a;
        private o.b b;

        b() {
        }

        @Override
        public o a() {
            return new i(this.a, this.b, null);
        }

        @Override
        public o.a b(o.b bVar) {
            this.b = bVar;
            return this;
        }

        @Override
        public o.a c(o.c cVar) {
            this.a = cVar;
            return this;
        }
    }

    i(o.c cVar, o.b bVar, a aVar) {
        this(cVar, bVar);
    }

    @Override
    public o.b b() {
        return this.b;
    }

    @Override
    public o.c c() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof o)) {
            return false;
        }
        o oVar = (o) obj;
        o.c cVar = this.a;
        if (cVar != null ? cVar.equals(oVar.c()) : oVar.c() == null) {
            o.b bVar = this.b;
            if (bVar == null) {
                if (oVar.b() == null) {
                    return true;
                }
            } else if (bVar.equals(oVar.b())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        o.c cVar = this.a;
        int iHashCode = ((cVar == null ? 0 : cVar.hashCode()) ^ 1000003) * 1000003;
        o.b bVar = this.b;
        return iHashCode ^ (bVar != null ? bVar.hashCode() : 0);
    }

    public String toString() {
        return "NetworkConnectionInfo{networkType=" + this.a + ", mobileSubtype=" + this.b + "}";
    }

    private i(o.c cVar, o.b bVar) {
        this.a = cVar;
        this.b = bVar;
    }
}
