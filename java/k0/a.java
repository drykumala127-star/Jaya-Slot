package k0;

import java.util.List;

final class a extends s {
    private final String a;
    private final List b;

    a(String str, List list) {
        if (str == null) {
            throw new NullPointerException("Null userAgent");
        }
        this.a = str;
        if (list == null) {
            throw new NullPointerException("Null usedDates");
        }
        this.b = list;
    }

    @Override
    public List b() {
        return this.b;
    }

    @Override
    public String c() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof s)) {
            return false;
        }
        s sVar = (s) obj;
        return this.a.equals(sVar.c()) && this.b.equals(sVar.b());
    }

    public int hashCode() {
        return ((this.a.hashCode() ^ 1000003) * 1000003) ^ this.b.hashCode();
    }

    public String toString() {
        return "HeartBeatResult{userAgent=" + this.a + ", usedDates=" + this.b + "}";
    }
}
