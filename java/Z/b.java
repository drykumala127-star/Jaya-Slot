package Z;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;

final class B implements Runnable {
    final i i;
    final C j;

    B(C c, i iVar) {
        this.j = c;
        this.i = iVar;
    }

    @Override
    public final void run() {
        try {
            i iVarA = C.e(this.j).a(this.i.i());
            if (iVarA == null) {
                this.j.c(new NullPointerException("Continuation returned null"));
                return;
            }
            Executor executor = k.b;
            iVarA.e(executor, this.j);
            iVarA.d(executor, this.j);
            iVarA.a(executor, this.j);
        } catch (g e) {
            if (e.getCause() instanceof Exception) {
                this.j.c((Exception) e.getCause());
            } else {
                this.j.c(e);
            }
        } catch (CancellationException unused) {
            this.j.b();
        } catch (Exception e2) {
            this.j.c(e2);
        }
    }
}
