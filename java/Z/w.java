package Z;

import java.util.concurrent.Executor;

final class w implements D {
    private final Executor a;
    private final Object b = new Object();
    private d c;

    public w(Executor executor, d dVar) {
        this.a = executor;
        this.c = dVar;
    }

    static d b(w wVar) {
        return wVar.c;
    }

    static Object c(w wVar) {
        return wVar.b;
    }

    @Override
    public final void a(i iVar) {
        synchronized (this.b) {
            try {
                if (this.c == null) {
                    return;
                }
                this.a.execute(new v(this, iVar));
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
