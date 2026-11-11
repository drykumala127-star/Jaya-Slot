package s;

import java.util.concurrent.Executor;
import t.x;

public final class d implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;
    private final y0.a d;
    private final y0.a e;

    public d(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
        this.d = aVar4;
        this.e = aVar5;
    }

    public static d a(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5) {
        return new d(aVar, aVar2, aVar3, aVar4, aVar5);
    }

    public static c c(Executor executor, n.e eVar, x xVar, u.d dVar, v.b bVar) {
        return new c(executor, eVar, xVar, dVar, bVar);
    }

    @Override
    public c get() {
        return c((Executor) this.a.get(), (n.e) this.b.get(), (x) this.c.get(), (u.d) this.d.get(), (v.b) this.e.get());
    }
}
