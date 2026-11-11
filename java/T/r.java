package t;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import n.g;
import p.c;

public class r {
    private final Context a;
    private final n.e b;
    private final u.d c;
    private final x d;
    private final Executor e;
    private final v.b f;
    private final w.a g;
    private final w.a h;
    private final u.c i;

    public r(Context context, n.e eVar, u.d dVar, x xVar, Executor executor, v.b bVar, w.a aVar, w.a aVar2, u.c cVar) {
        this.a = context;
        this.b = eVar;
        this.c = dVar;
        this.d = xVar;
        this.e = executor;
        this.f = bVar;
        this.g = aVar;
        this.h = aVar2;
        this.i = cVar;
    }

    public static Iterable a(r rVar, m.p pVar) {
        return rVar.m(pVar);
    }

    public static Object b(r rVar, Iterable iterable, m.p pVar, long j) {
        return rVar.n(iterable, pVar, j);
    }

    public static Object c(r rVar) {
        return rVar.p();
    }

    public static Boolean d(r rVar, m.p pVar) {
        return rVar.l(pVar);
    }

    public static Object e(r rVar, Iterable iterable) {
        return rVar.o(iterable);
    }

    public static Object f(r rVar, m.p pVar, int i) {
        return rVar.s(pVar, i);
    }

    public static Object g(r rVar, m.p pVar, long j) {
        return rVar.r(pVar, j);
    }

    public static Object h(r rVar, Map map) {
        return rVar.q(map);
    }

    public static void i(r rVar, m.p pVar, int i, Runnable runnable) {
        rVar.t(pVar, i, runnable);
    }

    private Boolean l(m.p pVar) {
        return Boolean.valueOf(this.c.c(pVar));
    }

    private Iterable m(m.p pVar) {
        return this.c.l(pVar);
    }

    private Object n(Iterable iterable, m.p pVar, long j) {
        this.c.i(iterable);
        this.c.g(pVar, this.g.a() + j);
        return null;
    }

    private Object o(Iterable iterable) {
        this.c.d(iterable);
        return null;
    }

    private Object p() {
        this.i.e();
        return null;
    }

    private Object q(Map map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            this.i.f(((Integer) r0.getValue()).intValue(), c.b.INVALID_PAYLOD, (String) ((Map.Entry) it.next()).getKey());
        }
        return null;
    }

    private Object r(m.p pVar, long j) {
        this.c.g(pVar, this.g.a() + j);
        return null;
    }

    private Object s(m.p pVar, int i) {
        this.d.a(pVar, i + 1);
        return null;
    }

    private void t(m.p pVar, int i, Runnable runnable) {
        try {
            try {
                v.b bVar = this.f;
                u.d dVar = this.c;
                Objects.requireNonNull(dVar);
                bVar.a(new i(dVar));
                if (k()) {
                    u(pVar, i);
                } else {
                    this.f.a(new j(this, pVar, i));
                }
            } catch (v.a unused) {
                this.d.a(pVar, i + 1);
            }
        } finally {
            runnable.run();
        }
    }

    public m.j j(n.m mVar) {
        v.b bVar = this.f;
        u.c cVar = this.i;
        Objects.requireNonNull(cVar);
        return mVar.a(m.j.a().i(this.g.a()).k(this.h.a()).j("GDT_CLIENT_METRICS").h(new m.i(k.b.b("proto"), ((p.a) bVar.a(new h(cVar))).f())).d());
    }

    boolean k() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.a.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public n.g u(m.p pVar, int i) {
        n.g gVarB;
        n.m mVarA = this.b.a(pVar.b());
        long jMax = 0;
        n.g gVarE = n.g.e(0L);
        while (true) {
            long j = jMax;
            while (true) {
                if (!((Boolean) this.f.a(new k(this, pVar))).booleanValue()) {
                    this.f.a(new q(this, pVar, j));
                    return gVarE;
                }
                Iterable iterable = (Iterable) this.f.a(new l(this, pVar));
                if (!iterable.iterator().hasNext()) {
                    return gVarE;
                }
                if (mVarA == null) {
                    q.a.b("Uploader", "Unknown backend for %s, deleting event batch for it...", pVar);
                    gVarB = n.g.a();
                } else {
                    ArrayList arrayList = new ArrayList();
                    Iterator it = iterable.iterator();
                    while (it.hasNext()) {
                        arrayList.add(((u.k) it.next()).b());
                    }
                    if (pVar.e()) {
                        arrayList.add(j(mVarA));
                    }
                    gVarB = mVarA.b(n.f.a().b(arrayList).c(pVar.c()).a());
                }
                gVarE = gVarB;
                if (gVarE.c() == g.a.TRANSIENT_ERROR) {
                    this.f.a(new m(this, iterable, pVar, j));
                    this.d.b(pVar, i + 1, true);
                    return gVarE;
                }
                this.f.a(new n(this, iterable));
                if (gVarE.c() == g.a.OK) {
                    break;
                }
                if (gVarE.c() == g.a.INVALID_PAYLOAD) {
                    HashMap map = new HashMap();
                    Iterator it2 = iterable.iterator();
                    while (it2.hasNext()) {
                        String strJ = ((u.k) it2.next()).b().j();
                        if (map.containsKey(strJ)) {
                            map.put(strJ, Integer.valueOf(((Integer) map.get(strJ)).intValue() + 1));
                        } else {
                            map.put(strJ, 1);
                        }
                    }
                    this.f.a(new p(this, map));
                }
            }
            jMax = Math.max(j, gVarE.b());
            if (pVar.e()) {
                this.f.a(new o(this));
            }
        }
    }

    public void v(m.p pVar, int i, Runnable runnable) {
        this.e.execute(new g(this, pVar, i, runnable));
    }
}
