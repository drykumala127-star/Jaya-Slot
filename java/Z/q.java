package Z;

import java.util.concurrent.Executor;

final class q implements D {
    private final Executor a;
    private final a b;
    private final H c;

    public q(Executor executor, a aVar, H h) {
        this.a = executor;
        this.b = aVar;
        this.c = h;
    }

    static a b(q qVar) {
        return qVar.b;
    }

    static H c(q qVar) {
        return qVar.c;
    }

    @Override
    public final void a(i iVar) {
        this.a.execute(new p(this, iVar));
    }
}
