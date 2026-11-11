package Z;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class l {
    public static Object a(i iVar) {
        F.n.g();
        F.n.j(iVar, "Task must not be null");
        if (iVar.l()) {
            return f(iVar);
        }
        n nVar = new n(null);
        g(iVar, nVar);
        nVar.a();
        return f(iVar);
    }

    public static Object b(i iVar, long j, TimeUnit timeUnit) {
        F.n.g();
        F.n.j(iVar, "Task must not be null");
        F.n.j(timeUnit, "TimeUnit must not be null");
        if (iVar.l()) {
            return f(iVar);
        }
        n nVar = new n(null);
        g(iVar, nVar);
        if (nVar.e(j, timeUnit)) {
            return f(iVar);
        }
        throw new TimeoutException("Timed out waiting for Task");
    }

    public static i c(Executor executor, Callable callable) {
        F.n.j(executor, "Executor must not be null");
        F.n.j(callable, "Callback must not be null");
        H h = new H();
        executor.execute(new I(h, callable));
        return h;
    }

    public static i d(Exception exc) {
        H h = new H();
        h.p(exc);
        return h;
    }

    public static i e(Object obj) {
        H h = new H();
        h.q(obj);
        return h;
    }

    private static Object f(i iVar) throws ExecutionException {
        if (iVar.m()) {
            return iVar.i();
        }
        if (iVar.k()) {
            throw new CancellationException("Task is already canceled");
        }
        throw new ExecutionException(iVar.h());
    }

    private static void g(i iVar, o oVar) {
        Executor executor = k.b;
        iVar.e(executor, oVar);
        iVar.d(executor, oVar);
        iVar.a(executor, oVar);
    }
}
