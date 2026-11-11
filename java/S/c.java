package s;

import java.util.concurrent.Executor;
import java.util.logging.Logger;
import m.j;
import m.p;
import m.u;
import n.m;
import t.x;

public class c implements e {
    private static final Logger f = Logger.getLogger(u.class.getName());
    private final x a;
    private final Executor b;
    private final n.e c;
    private final u.d d;
    private final v.b e;

    public c(Executor executor, n.e eVar, x xVar, u.d dVar, v.b bVar) {
        this.b = executor;
        this.c = eVar;
        this.a = xVar;
        this.d = dVar;
        this.e = bVar;
    }

    public static Object b(c cVar, p pVar, j jVar) {
        return cVar.d(pVar, jVar);
    }

    public static void c(c cVar, p pVar, k.h hVar, j jVar) {
        cVar.e(pVar, hVar, jVar);
    }

    private Object d(p pVar, j jVar) {
        this.d.j(pVar, jVar);
        this.a.a(pVar, 1);
        return null;
    }

    private void e(p pVar, k.h hVar, j jVar) {
        try {
            m mVarA = this.c.a(pVar.b());
            if (mVarA != null) {
                this.e.a(new b(this, pVar, mVarA.a(jVar)));
                hVar.a(null);
            } else {
                String str = String.format("Transport backend '%s' is not registered", pVar.b());
                f.warning(str);
                hVar.a(new IllegalArgumentException(str));
            }
        } catch (Exception e) {
            f.warning("Error scheduling event " + e.getMessage());
            hVar.a(e);
        }
    }

    @Override
    public void a(p pVar, j jVar, k.h hVar) {
        this.b.execute(new a(this, pVar, hVar, jVar));
    }
}
