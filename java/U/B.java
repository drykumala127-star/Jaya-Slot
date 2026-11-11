package u;

final class b extends k {
    private final long a;
    private final m.p b;
    private final m.j c;

    b(long j, m.p pVar, m.j jVar) {
        this.a = j;
        if (pVar == null) {
            throw new NullPointerException("Null transportContext");
        }
        this.b = pVar;
        if (jVar == null) {
            throw new NullPointerException("Null event");
        }
        this.c = jVar;
    }

    @Override
    public m.j b() {
        return this.c;
    }

    @Override
    public long c() {
        return this.a;
    }

    @Override
    public m.p d() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        return this.a == kVar.c() && this.b.equals(kVar.d()) && this.c.equals(kVar.b());
    }

    public int hashCode() {
        long j = this.a;
        return this.c.hashCode() ^ ((((((int) (j ^ (j >>> 32))) ^ 1000003) * 1000003) ^ this.b.hashCode()) * 1000003);
    }

    public String toString() {
        return "PersistedEvent{id=" + this.a + ", transportContext=" + this.b + ", event=" + this.c + "}";
    }
}
