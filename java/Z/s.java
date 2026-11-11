package Z;

import java.util.concurrent.Executor;

final class s implements f, e, c, D {
    private final Executor a;
    private final a b;
    private final H c;

    public s(Executor executor, a aVar, H h) {
        this.a = executor;
        this.b = aVar;
        this.c = h;
    }

    static a e(s sVar) {
        return sVar.b;
    }

    static H f(s sVar) {
        return sVar.c;
    }

    @Override
    public final void a(i iVar) {
        this.a.execute(new r(this, iVar));
    }

    @Override
    public final void b() {
        this.c.r();
    }

    @Override
    public final void c(Exception exc) {
        this.c.p(exc);
    }

    @Override
    public final void d(Object obj) {
        this.c.q(obj);
    }
}
