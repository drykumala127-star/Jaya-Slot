package n;

import android.content.Context;

public final class l implements o.b {
    private final y0.a a;
    private final y0.a b;

    public l(y0.a aVar, y0.a aVar2) {
        this.a = aVar;
        this.b = aVar2;
    }

    public static l a(y0.a aVar, y0.a aVar2) {
        return new l(aVar, aVar2);
    }

    public static k c(Context context, Object obj) {
        return new k(context, (i) obj);
    }

    @Override
    public k get() {
        return c((Context) this.a.get(), this.b.get());
    }
}
