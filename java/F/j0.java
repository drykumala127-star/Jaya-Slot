package F;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.concurrent.Executor;

final class j0 extends h {
    private final HashMap f = new HashMap();
    private final Context g;
    private volatile Handler h;
    private final i0 i;
    private final I.b j;
    private final long k;
    private final long l;

    j0(Context context, Looper looper) {
        i0 i0Var = new i0(this, null);
        this.i = i0Var;
        this.g = context.getApplicationContext();
        this.h = new R.e(looper, i0Var);
        this.j = I.b.b();
        this.k = 5000L;
        this.l = 300000L;
    }

    static long g(j0 j0Var) {
        return j0Var.l;
    }

    static Context h(j0 j0Var) {
        return j0Var.g;
    }

    static Handler i(j0 j0Var) {
        return j0Var.h;
    }

    static I.b j(j0 j0Var) {
        return j0Var.j;
    }

    static HashMap k(j0 j0Var) {
        return j0Var.f;
    }

    @Override
    protected final void d(e0 e0Var, ServiceConnection serviceConnection, String str) {
        n.j(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.f) {
            try {
                g0 g0Var = (g0) this.f.get(e0Var);
                if (g0Var == null) {
                    throw new IllegalStateException("Nonexistent connection status for service config: " + e0Var.toString());
                }
                if (!g0Var.h(serviceConnection)) {
                    throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  config=" + e0Var.toString());
                }
                g0Var.f(serviceConnection, str);
                if (g0Var.i()) {
                    this.h.sendMessageDelayed(this.h.obtainMessage(0, e0Var), this.k);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override
    protected final boolean f(e0 e0Var, ServiceConnection serviceConnection, String str, Executor executor) {
        boolean zJ;
        n.j(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.f) {
            try {
                g0 g0Var = (g0) this.f.get(e0Var);
                if (g0Var == null) {
                    g0Var = new g0(this, e0Var);
                    g0Var.d(serviceConnection, serviceConnection, str);
                    g0Var.e(str, executor);
                    this.f.put(e0Var, g0Var);
                } else {
                    this.h.removeMessages(0, e0Var);
                    if (g0Var.h(serviceConnection)) {
                        throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  config=" + e0Var.toString());
                    }
                    g0Var.d(serviceConnection, serviceConnection, str);
                    int iA = g0Var.a();
                    if (iA == 1) {
                        serviceConnection.onServiceConnected(g0Var.b(), g0Var.c());
                    } else if (iA == 2) {
                        g0Var.e(str, executor);
                    }
                }
                zJ = g0Var.j();
            } catch (Throwable th) {
                throw th;
            }
        }
        return zJ;
    }
}
