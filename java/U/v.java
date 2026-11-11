package u;

import android.content.Context;

public final class V implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;

    public V(y0.a aVar, y0.a aVar2, y0.a aVar3) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
    }

    public static V a(y0.a aVar, y0.a aVar2, y0.a aVar3) {
        return new V(aVar, aVar2, aVar3);
    }

    public static U c(Context context, String str, int i) {
        return new U(context, str, i);
    }

    @Override
    public U get() {
        return c((Context) this.a.get(), (String) this.b.get(), ((Integer) this.c.get()).intValue());
    }
}
