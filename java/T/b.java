package t;

import java.util.Map;

final class b extends f {
    private final w.a a;
    private final Map b;

    b(w.a aVar, Map map) {
        if (aVar == null) {
            throw new NullPointerException("Null clock");
        }
        this.a = aVar;
        if (map == null) {
            throw new NullPointerException("Null values");
        }
        this.b = map;
    }

    @Override
    w.a e() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof f)) {
            return false;
        }
        f fVar = (f) obj;
        return this.a.equals(fVar.e()) && this.b.equals(fVar.h());
    }

    @Override
    Map h() {
        return this.b;
    }

    public int hashCode() {
        return ((this.a.hashCode() ^ 1000003) * 1000003) ^ this.b.hashCode();
    }

    public String toString() {
        return "SchedulerConfig{clock=" + this.a + ", values=" + this.b + "}";
    }
}
