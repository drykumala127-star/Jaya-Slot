package t;

import java.util.Iterator;
import java.util.concurrent.Executor;

public class v {
    private final Executor a;
    private final u.d b;
    private final x c;
    private final v.b d;

    v(Executor executor, u.d dVar, x xVar, v.b bVar) {
        this.a = executor;
        this.b = dVar;
        this.c = xVar;
        this.d = bVar;
    }

    public static Object a(v vVar) {
        return vVar.d();
    }

    public static void b(v vVar) {
        vVar.e();
    }

    private Object d() {
        Iterator it = this.b.m().iterator();
        while (it.hasNext()) {
            this.c.a((m.p) it.next(), 1);
        }
        return null;
    }

    private void e() {
        this.d.a(new u(this));
    }

    public void c() {
        this.a.execute(new t(this));
    }
}
