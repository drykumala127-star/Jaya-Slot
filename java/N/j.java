package n;

import android.content.Context;

public final class j implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;

    public j(y0.a aVar, y0.a aVar2, y0.a aVar3) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
    }

    public static j a(y0.a aVar, y0.a aVar2, y0.a aVar3) {
        return new j(aVar, aVar2, aVar3);
    }

    public static i c(Context context, w.a aVar, w.a aVar2) {
        return new i(context, aVar, aVar2);
    }

    @Override
    public i get() {
        return c((Context) this.a.get(), (w.a) this.b.get(), (w.a) this.c.get());
    }
}
