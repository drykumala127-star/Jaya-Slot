package F;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.StrictMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;

final class g0 implements ServiceConnection, k0 {
    private final Map a = new HashMap();
    private int b = 2;
    private boolean c;
    private IBinder d;
    private final e0 e;
    private ComponentName f;
    final j0 g;

    public g0(j0 j0Var, e0 e0Var) {
        this.g = j0Var;
        this.e = e0Var;
    }

    public final int a() {
        return this.b;
    }

    public final ComponentName b() {
        return this.f;
    }

    public final IBinder c() {
        return this.d;
    }

    public final void d(ServiceConnection serviceConnection, ServiceConnection serviceConnection2, String str) {
        this.a.put(serviceConnection, serviceConnection2);
    }

    public final void e(String str, Executor executor) {
        this.b = 3;
        StrictMode.VmPolicy vmPolicy = StrictMode.getVmPolicy();
        if (J.k.l()) {
            StrictMode.setVmPolicy(f0.a(new StrictMode.VmPolicy.Builder(vmPolicy)).build());
        }
        try {
            j0 j0Var = this.g;
            boolean zD = j0.j(j0Var).d(j0.h(j0Var), str, this.e.c(j0.h(j0Var)), this, this.e.a(), executor);
            this.c = zD;
            if (zD) {
                j0.i(this.g).sendMessageDelayed(j0.i(this.g).obtainMessage(1, this.e), j0.g(this.g));
            } else {
                this.b = 2;
                try {
                    j0 j0Var2 = this.g;
                    j0.j(j0Var2).c(j0.h(j0Var2), this);
                } catch (IllegalArgumentException unused) {
                }
            }
        } finally {
            StrictMode.setVmPolicy(vmPolicy);
        }
    }

    public final void f(ServiceConnection serviceConnection, String str) {
        this.a.remove(serviceConnection);
    }

    public final void g(String str) {
        j0.i(this.g).removeMessages(1, this.e);
        j0 j0Var = this.g;
        j0.j(j0Var).c(j0.h(j0Var), this);
        this.c = false;
        this.b = 2;
    }

    public final boolean h(ServiceConnection serviceConnection) {
        return this.a.containsKey(serviceConnection);
    }

    public final boolean i() {
        return this.a.isEmpty();
    }

    public final boolean j() {
        return this.c;
    }

    @Override
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (j0.k(this.g)) {
            try {
                j0.i(this.g).removeMessages(1, this.e);
                this.d = iBinder;
                this.f = componentName;
                Iterator it = this.a.values().iterator();
                while (it.hasNext()) {
                    ((ServiceConnection) it.next()).onServiceConnected(componentName, iBinder);
                }
                this.b = 1;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override
    public final void onServiceDisconnected(ComponentName componentName) {
        synchronized (j0.k(this.g)) {
            try {
                j0.i(this.g).removeMessages(1, this.e);
                this.d = null;
                this.f = componentName;
                Iterator it = this.a.values().iterator();
                while (it.hasNext()) {
                    ((ServiceConnection) it.next()).onServiceDisconnected(componentName);
                }
                this.b = 2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
