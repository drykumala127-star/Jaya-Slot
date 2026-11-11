package c0;

public final class r {
    private final Class a;
    private final int b;
    private final int c;

    private r(Class cls, int i, int i2) {
        this.a = (Class) D.c(cls, "Null dependency anInterface.");
        this.b = i;
        this.c = i2;
    }

    private static String a(int i) {
        if (i == 0) {
            return "direct";
        }
        if (i == 1) {
            return "provider";
        }
        if (i == 2) {
            return "deferred";
        }
        throw new AssertionError("Unsupported injection: " + i);
    }

    public static r g(Class cls) {
        return new r(cls, 0, 0);
    }

    public static r h(Class cls) {
        return new r(cls, 0, 1);
    }

    public static r i(Class cls) {
        return new r(cls, 1, 0);
    }

    public static r j(Class cls) {
        return new r(cls, 1, 1);
    }

    public static r k(Class cls) {
        return new r(cls, 2, 0);
    }

    public Class b() {
        return this.a;
    }

    public boolean c() {
        return this.c == 2;
    }

    public boolean d() {
        return this.c == 0;
    }

    public boolean e() {
        return this.b == 1;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof r)) {
            return false;
        }
        r rVar = (r) obj;
        return this.a == rVar.a && this.b == rVar.b && this.c == rVar.c;
    }

    public boolean f() {
        return this.b == 2;
    }

    public int hashCode() {
        return ((((this.a.hashCode() ^ 1000003) * 1000003) ^ this.b) * 1000003) ^ this.c;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Dependency{anInterface=");
        sb.append(this.a);
        sb.append(", type=");
        int i = this.b;
        sb.append(i == 1 ? "required" : i == 0 ? "optional" : "set");
        sb.append(", injection=");
        sb.append(a(this.c));
        sb.append("}");
        return sb.toString();
    }
}
