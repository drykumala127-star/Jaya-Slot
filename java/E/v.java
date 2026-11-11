package E;

import D.a;
import D.e;
import F.I;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.Set;

public final class v extends X.d implements e.a, e.b {
    private static final a.a h = W.d.c;
    private final Context a;
    private final Handler b;
    private final a.a c;
    private final Set d;
    private final F.d e;
    private W.e f;
    private u g;

    public v(Context context, Handler handler, F.d dVar) {
        a.a aVar = h;
        this.a = context;
        this.b = handler;
        this.e = (F.d) F.n.j(dVar, "ClientSettings must not be null");
        this.d = dVar.e();
        this.c = aVar;
    }

    static u M(v vVar) {
        return vVar.g;
    }

    static void N(v vVar, X.l lVar) {
        C.b bVarD = lVar.d();
        if (bVarD.k()) {
            I i = (I) F.n.i(lVar.g());
            C.b bVarD2 = i.d();
            if (!bVarD2.k()) {
                String strValueOf = String.valueOf(bVarD2);
                Log.wtf("SignInCoordinator", "Sign-in succeeded with resolve account failure: ".concat(strValueOf), new Exception());
                vVar.g.c(bVarD2);
                vVar.f.j();
                return;
            }
            vVar.g.a(i.g(), vVar.d);
        } else {
            vVar.g.c(bVarD);
        }
        vVar.f.j();
    }

    @Override
    public final void D(X.l lVar) {
        this.b.post(new t(this, lVar));
    }

    public final void O(u uVar) {
        W.e eVar = this.f;
        if (eVar != null) {
            eVar.j();
        }
        this.e.i(Integer.valueOf(System.identityHashCode(this)));
        a.a aVar = this.c;
        Context context = this.a;
        Looper looper = this.b.getLooper();
        F.d dVar = this.e;
        this.f = aVar.a(context, looper, dVar, dVar.f(), this, this);
        this.g = uVar;
        Set set = this.d;
        if (set == null || set.isEmpty()) {
            this.b.post(new s(this));
        } else {
            this.f.n();
        }
    }

    public final void P() {
        W.e eVar = this.f;
        if (eVar != null) {
            eVar.j();
        }
    }

    @Override
    public final void a(int i) {
        this.f.j();
    }

    @Override
    public final void e(C.b bVar) {
        this.g.c(bVar);
    }

    @Override
    public final void f(Bundle bundle) {
        this.f.e(this);
    }
}
