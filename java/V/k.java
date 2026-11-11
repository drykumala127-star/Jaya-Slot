package V;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.internal.measurement.q0;
import com.google.android.gms.measurement.internal.K1;
import com.google.android.gms.measurement.internal.g1;

public final class k {
    private final j a;

    public k(j jVar) {
        F.n.i(jVar);
        this.a = jVar;
    }

    public final void a(Context context, Intent intent) {
        K1 k1H = K1.H(context, (q0) null, (Long) null);
        g1 g1VarF = k1H.f();
        if (intent == null) {
            g1VarF.w().a("Receiver called with null intent");
            return;
        }
        k1H.e();
        String action = intent.getAction();
        g1VarF.v().b("Local receiver got", action);
        if (!"com.google.android.gms.measurement.UPLOAD".equals(action)) {
            if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
                g1VarF.w().a("Install Referrer Broadcasts are deprecated");
            }
        } else {
            Intent className = new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementService");
            className.setAction("com.google.android.gms.measurement.UPLOAD");
            g1VarF.v().a("Starting wakeful intent.");
            this.a.a(context, className);
        }
    }
}
