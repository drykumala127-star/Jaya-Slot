package Z;

import java.util.concurrent.Executor;

final class r implements Runnable {
    final i i;
    final s j;

    r(s sVar, i iVar) {
        this.j = sVar;
        this.i = iVar;
    }

    @Override
    public final void run() {
        try {
            i iVar = (i) s.e(this.j).a(this.i);
            if (iVar == null) {
                this.j.c(new NullPointerException("Continuation returned null"));
                return;
            }
            Executor executor = k.b;
            iVar.e(executor, this.j);
            iVar.d(executor, this.j);
            iVar.a(executor, this.j);
        } catch (g e) {
            if (e.getCause() instanceof Exception) {
                s.f(this.j).p((Exception) e.getCause());
            } else {
                s.f(this.j).p(e);
            }
        } catch (Exception e2) {
            s.f(this.j).p(e2);
        }
    }
}
