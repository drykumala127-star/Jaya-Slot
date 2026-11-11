package u;

import android.content.Context;

public final class h implements o.b {
    private final y0.a a;

    public h(y0.a aVar) {
        this.a = aVar;
    }

    public static h a(y0.a aVar) {
        return new h(aVar);
    }

    public static String c(Context context) {
        return (String) o.d.c(f.b(context), "Cannot return null from a non-@Nullable @Provides method");
    }

    @Override
    public String get() {
        return c((Context) this.a.get());
    }
}
