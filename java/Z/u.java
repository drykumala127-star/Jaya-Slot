package Z;

import java.util.concurrent.Executor;

final class u implements D {
    private final Executor a;
    private final Object b = new Object();
    private c c;

    public u(Executor executor, c cVar) {
        this.a = executor;
        this.c = cVar;
    }

    static c b(u uVar) {
        return uVar.c;
    }

    static Object c(u uVar) {
        return uVar.b;
    }

    @Override
    public final void a(i iVar) {
        if (iVar.k()) {
            synchronized (this.b) {
                try {
                    if (this.c == null) {
                        return;
                    }
                    this.a.execute(new t(this));
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }
}
