package w;

public final class c implements o.b {

    private static final class a {
        private static final c a = new c();

        static c a() {
            return a;
        }
    }

    public static c a() {
        return a.a();
    }

    public static w.a b() {
        return (w.a) o.d.c(b.a(), "Cannot return null from a non-@Nullable @Provides method");
    }

    @Override
    public w.a get() {
        return b();
    }
}
