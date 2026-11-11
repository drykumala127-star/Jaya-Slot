package t;

import java.util.concurrent.Executor;

public final class w implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;
    private final y0.a d;

    public w(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
        this.d = aVar4;
    }

    public static w a(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4) {
        return new w(aVar, aVar2, aVar3, aVar4);
    }

    public static v c(Executor executor, u.d dVar, x xVar, v.b bVar) {
        return new v(executor, dVar, xVar, bVar);
    }

    @Override
    public v get() {
        return c((Executor) this.a.get(), (u.d) this.b.get(), (x) this.c.get(), (v.b) this.d.get());
    }
}
