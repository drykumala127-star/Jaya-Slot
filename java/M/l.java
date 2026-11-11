package m;

import java.util.concurrent.Executor;

public final class l implements o.b {

    private static final class a {
        private static final l a = new l();

        static l a() {
            return a;
        }
    }

    public static l a() {
        return a.a();
    }

    public static Executor b() {
        return (Executor) o.d.c(k.a(), "Cannot return null from a non-@Nullable @Provides method");
    }

    @Override
    public Executor get() {
        return b();
    }
}
