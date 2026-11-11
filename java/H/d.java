package H;

import D.a;
import D.d;
import F.r;
import F.t;
import F.u;
import Z.i;
import android.content.Context;
import com.google.android.gms.common.api.internal.c;

public final class d extends D.d implements t {
    private static final a.g k;
    private static final a.a l;
    private static final D.a m;
    public static final int n = 0;

    static {
        a.g gVar = new a.g();
        k = gVar;
        c cVar = new c();
        l = cVar;
        m = new D.a("ClientTelemetry.API", cVar, gVar);
    }

    public d(Context context, u uVar) {
        super(context, m, uVar, d.a.c);
    }

    @Override
    public final i a(r rVar) {
        c.a aVarA = com.google.android.gms.common.api.internal.c.a();
        aVarA.d(new C.d[]{P.d.a});
        aVarA.c(false);
        aVarA.b(new b(rVar));
        return c(aVarA.a());
    }
}
