package p;

public final class b {
    private static final b b = new a().a();
    private final e a;

    public static final class a {
        private e a = null;

        a() {
        }

        public b a() {
            return new b(this.a);
        }

        public a b(e eVar) {
            this.a = eVar;
            return this;
        }
    }

    b(e eVar) {
        this.a = eVar;
    }

    public static a b() {
        return new a();
    }

    public e a() {
        return this.a;
    }
}
