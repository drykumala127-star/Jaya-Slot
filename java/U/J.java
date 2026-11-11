package u;

public final class j implements o.b {

    private static final class a {
        private static final j a = new j();

        static j a() {
            return a;
        }
    }

    public static j a() {
        return a.a();
    }

    public static e c() {
        return (e) o.d.c(f.d(), "Cannot return null from a non-@Nullable @Provides method");
    }

    @Override
    public e get() {
        return c();
    }
}
