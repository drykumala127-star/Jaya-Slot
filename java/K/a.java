package k;

final class a extends c {
    private final Integer a;
    private final Object b;
    private final d c;

    a(Integer num, Object obj, d dVar) {
        this.a = num;
        if (obj == null) {
            throw new NullPointerException("Null payload");
        }
        this.b = obj;
        if (dVar == null) {
            throw new NullPointerException("Null priority");
        }
        this.c = dVar;
    }

    @Override
    public Integer a() {
        return this.a;
    }

    @Override
    public Object b() {
        return this.b;
    }

    @Override
    public d c() {
        return this.c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        c cVar = (c) obj;
        Integer num = this.a;
        if (num != null ? num.equals(cVar.a()) : cVar.a() == null) {
            if (this.b.equals(cVar.b()) && this.c.equals(cVar.c())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        Integer num = this.a;
        return (((((num == null ? 0 : num.hashCode()) ^ 1000003) * 1000003) ^ this.b.hashCode()) * 1000003) ^ this.c.hashCode();
    }

    public String toString() {
        return "Event{code=" + this.a + ", payload=" + this.b + ", priority=" + this.c + "}";
    }
}
