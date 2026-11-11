package u;

public final class i implements o.b {

    private static final class a {
        private static final i a = new i();

        static i a() {
            return a;
        }
    }

    public static i a() {
        return a.a();
    }

    public static int c() {
        return f.c();
    }

    @Override
    public Integer get() {
        return Integer.valueOf(c());
    }
}
