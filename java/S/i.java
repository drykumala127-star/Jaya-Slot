package s;

import android.content.Context;
import t.x;

public final class i implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;
    private final y0.a d;

    public i(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
        this.d = aVar4;
    }

    public static i a(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4) {
        return new i(aVar, aVar2, aVar3, aVar4);
    }

    public static x c(Context context, u.d dVar, t.f fVar, w.a aVar) {
        return (x) o.d.c(h.a(context, dVar, fVar, aVar), "Cannot return null from a non-@Nullable @Provides method");
    }

    @Override
    public x get() {
        return c((Context) this.a.get(), (u.d) this.b.get(), (t.f) this.c.get(), (w.a) this.d.get());
    }
}
