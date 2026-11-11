package l;

import l.k;

final class e extends k {
    private final k.b a;
    private final l.a b;

    static final class b extends k.a {
        private k.b a;
        private l.a b;

        b() {
        }

        @Override
        public k a() {
            return new e(this.a, this.b, null);
        }

        @Override
        public k.a b(l.a aVar) {
            this.b = aVar;
            return this;
        }

        @Override
        public k.a c(k.b bVar) {
            this.a = bVar;
            return this;
        }
    }

    e(k.b bVar, l.a aVar, a aVar2) {
        this(bVar, aVar);
    }

    @Override
    public l.a b() {
        return this.b;
    }

    @Override
    public k.b c() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        k.b bVar = this.a;
        if (bVar != null ? bVar.equals(kVar.c()) : kVar.c() == null) {
            l.a aVar = this.b;
            if (aVar == null) {
                if (kVar.b() == null) {
                    return true;
                }
            } else if (aVar.equals(kVar.b())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        k.b bVar = this.a;
        int iHashCode = ((bVar == null ? 0 : bVar.hashCode()) ^ 1000003) * 1000003;
        l.a aVar = this.b;
        return iHashCode ^ (aVar != null ? aVar.hashCode() : 0);
    }

    public String toString() {
        return "ClientInfo{clientType=" + this.a + ", androidClientInfo=" + this.b + "}";
    }

    private e(k.b bVar, l.a aVar) {
        this.a = bVar;
        this.b = aVar;
    }
}
