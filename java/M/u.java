package m;

import android.content.Context;
import java.util.Collections;
import java.util.Set;

public class u implements t {
    private static volatile v e;
    private final w.a a;
    private final w.a b;
    private final s.e c;
    private final t.r d;

    u(w.a aVar, w.a aVar2, s.e eVar, t.r rVar, t.v vVar) {
        this.a = aVar;
        this.b = aVar2;
        this.c = eVar;
        this.d = rVar;
        vVar.c();
    }

    private j b(o oVar) {
        return j.a().i(this.a.a()).k(this.b.a()).j(oVar.g()).h(new i(oVar.b(), oVar.d())).g(oVar.c().a()).d();
    }

    public static u c() {
        v vVar = e;
        if (vVar != null) {
            return vVar.e();
        }
        throw new IllegalStateException("Not initialized!");
    }

    private static Set d(g gVar) {
        return gVar instanceof h ? Collections.unmodifiableSet(((h) gVar).b()) : Collections.singleton(k.b.b("proto"));
    }

    public static void f(Context context) {
        if (e == null) {
            synchronized (u.class) {
                try {
                    if (e == null) {
                        e = f.f().b(context).a();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    @Override
    public void a(o oVar, k.h hVar) {
        this.c.a(oVar.f().f(oVar.c().c()), b(oVar), hVar);
    }

    public t.r e() {
        return this.d;
    }

    public k.g g(g gVar) {
        return new q(d(gVar), p.a().b(gVar.c()).c(gVar.a()).a(), this);
    }
}
