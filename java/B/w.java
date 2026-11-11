package B;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class w {
    private static w e;
    private final Context a;
    private final ScheduledExecutorService b;
    private q c = new q(this, null);
    private int d = 1;

    w(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.b = scheduledExecutorService;
        this.a = context.getApplicationContext();
    }

    static Context a(w wVar) {
        return wVar.a;
    }

    public static synchronized w b(Context context) {
        try {
            if (e == null) {
                Q.e.a();
                e = new w(context, Executors.unconfigurableScheduledExecutorService(Executors.newScheduledThreadPool(1, new K.a("MessengerIpcClient"))));
            }
        } catch (Throwable th) {
            throw th;
        }
        return e;
    }

    static ScheduledExecutorService e(w wVar) {
        return wVar.b;
    }

    private final synchronized int f() {
        int i;
        i = this.d;
        this.d = i + 1;
        return i;
    }

    private final synchronized Z.i g(t tVar) {
        try {
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                String strValueOf = String.valueOf(tVar);
                StringBuilder sb = new StringBuilder(strValueOf.length() + 9);
                sb.append("Queueing ");
                sb.append(strValueOf);
                Log.d("MessengerIpcClient", sb.toString());
            }
            if (!this.c.g(tVar)) {
                q qVar = new q(this, null);
                this.c = qVar;
                qVar.g(tVar);
            }
        } catch (Throwable th) {
            throw th;
        }
        return tVar.b.a();
    }

    public final Z.i c(int i, Bundle bundle) {
        return g(new s(f(), 2, bundle));
    }

    public final Z.i d(int i, Bundle bundle) {
        return g(new v(f(), 1, bundle));
    }
}
