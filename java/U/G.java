package u;

public final class g implements o.b {

    private static final class a {
        private static final g a = new g();

        static g a() {
            return a;
        }
    }

    public static g a() {
        return a.a();
    }

    public static String b() {
        return (String) o.d.c(f.a(), "Cannot return null from a non-@Nullable @Provides method");
    }

    @Override
    public String get() {
        return b();
    }
}
