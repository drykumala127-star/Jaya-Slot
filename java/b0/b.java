package b0;

import F.n;
import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.internal.measurement.V0;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class b implements a {
    private static volatile a c;
    final U.a a;
    final Map b;

    b(U.a aVar) {
        n.i(aVar);
        this.a = aVar;
        this.b = new ConcurrentHashMap();
    }

    public static a c(a0.d dVar, Context context, j0.d dVar2) {
        n.i(dVar);
        n.i(context);
        n.i(dVar2);
        n.i(context.getApplicationContext());
        if (c == null) {
            synchronized (b.class) {
                try {
                    if (c == null) {
                        Bundle bundle = new Bundle(1);
                        if (dVar.t()) {
                            dVar2.b(a0.a.class, c.a, d.a);
                            bundle.putBoolean("dataCollectionDefaultEnabled", dVar.s());
                        }
                        c = new b(V0.q(context, (String) null, (String) null, (String) null, bundle).n());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return c;
    }

    static void d(j0.a aVar) {
        throw null;
    }

    @Override
    public void a(String str, String str2, Object obj) {
        if (com.google.firebase.analytics.connector.internal.b.d(str) && com.google.firebase.analytics.connector.internal.b.e(str, str2)) {
            this.a.b(str, str2, obj);
        }
    }

    @Override
    public void b(String str, String str2, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (com.google.firebase.analytics.connector.internal.b.d(str) && com.google.firebase.analytics.connector.internal.b.c(str2, bundle) && com.google.firebase.analytics.connector.internal.b.b(str, str2, bundle)) {
            com.google.firebase.analytics.connector.internal.b.a(str, str2, bundle);
            this.a.a(str, str2, bundle);
        }
    }
}
