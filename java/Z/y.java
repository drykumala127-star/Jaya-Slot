package Z;

import java.util.concurrent.Executor;

final class y implements D {
    private final Executor a;
    private final Object b = new Object();
    private e c;

    public y(Executor executor, e eVar) {
        this.a = executor;
        this.c = eVar;
    }

    static e b(y yVar) {
        return yVar.c;
    }

    static Object c(y yVar) {
        return yVar.b;
    }

    @Override
    public final void a(i iVar) {
        if (iVar.m() || iVar.k()) {
            return;
        }
        synchronized (this.b) {
            try {
                if (this.c == null) {
                    return;
                }
                this.a.execute(new x(this, iVar));
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
