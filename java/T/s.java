package t;

import android.content.Context;
import java.util.concurrent.Executor;

public final class s implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;
    private final y0.a d;
    private final y0.a e;
    private final y0.a f;
    private final y0.a g;
    private final y0.a h;
    private final y0.a i;

    public s(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5, y0.a aVar6, y0.a aVar7, y0.a aVar8, y0.a aVar9) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
        this.d = aVar4;
        this.e = aVar5;
        this.f = aVar6;
        this.g = aVar7;
        this.h = aVar8;
        this.i = aVar9;
    }

    public static s a(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5, y0.a aVar6, y0.a aVar7, y0.a aVar8, y0.a aVar9) {
        return new s(aVar, aVar2, aVar3, aVar4, aVar5, aVar6, aVar7, aVar8, aVar9);
    }

    public static r c(Context context, n.e eVar, u.d dVar, x xVar, Executor executor, v.b bVar, w.a aVar, w.a aVar2, u.c cVar) {
        return new r(context, eVar, dVar, xVar, executor, bVar, aVar, aVar2, cVar);
    }

    @Override
    public r get() {
        return c((Context) this.a.get(), (n.e) this.b.get(), (u.d) this.c.get(), (x) this.d.get(), (Executor) this.e.get(), (v.b) this.f.get(), (w.a) this.g.get(), (w.a) this.h.get(), (u.c) this.i.get());
    }
}
