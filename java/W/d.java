package w;

public final class d implements o.b {

    private static final class a {
        private static final d a = new d();

        static d a() {
            return a;
        }
    }

    public static d a() {
        return a.a();
    }

    public static w.a c() {
        return (w.a) o.d.c(b.b(), "Cannot return null from a non-@Nullable @Provides method");
    }

    @Override
    public w.a get() {
        return c();
    }
}
