package x0;

final class d {
    private final Object a;
    private final Object b;

    private d(Object obj, Object obj2) {
        this.a = obj;
        this.b = obj2;
    }

    public static d b(Object obj, Object obj2) {
        return new d(obj, obj2);
    }

    public Object a() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || d.class != obj.getClass()) {
            return false;
        }
        d dVar = (d) obj;
        Object obj2 = this.a;
        if (obj2 == null) {
            if (dVar.a != null) {
                return false;
            }
        } else if (!obj2.equals(dVar.a)) {
            return false;
        }
        Object obj3 = this.b;
        if (obj3 == null) {
            if (dVar.b != null) {
                return false;
            }
        } else if (!obj3.equals(dVar.b)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        Object obj = this.a;
        int iHashCode = ((obj == null ? 0 : obj.hashCode()) + 31) * 31;
        Object obj2 = this.b;
        return iHashCode + (obj2 != null ? obj2.hashCode() : 0);
    }
}
