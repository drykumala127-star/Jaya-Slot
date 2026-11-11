package Z;

import java.util.concurrent.Executor;

final class C implements f, e, c, D {
    private final Executor a;
    private final h b;
    private final H c;

    public C(Executor executor, h hVar, H h) {
        this.a = executor;
        this.b = hVar;
        this.c = h;
    }

    static h e(C c) {
        return c.b;
    }

    @Override
    public final void a(i iVar) {
        this.a.execute(new B(this, iVar));
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
