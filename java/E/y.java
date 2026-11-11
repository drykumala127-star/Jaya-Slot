package E;

import com.google.android.gms.common.api.internal.LifecycleCallback;

final class y implements Runnable {
    final LifecycleCallback i;
    final String j;
    final z k;

    y(z zVar, LifecycleCallback lifecycleCallback, String str) {
        this.k = zVar;
        this.i = lifecycleCallback;
        this.j = str;
    }

    @Override
    public final void run() {
        z zVar = this.k;
        if (z.d(zVar) > 0) {
            this.i.f(z.e(zVar) != null ? z.e(zVar).getBundle(this.j) : null);
        }
        if (z.d(this.k) >= 2) {
            this.i.j();
        }
        if (z.d(this.k) >= 3) {
            this.i.h();
        }
        if (z.d(this.k) >= 4) {
            this.i.k();
        }
        if (z.d(this.k) >= 5) {
            this.i.g();
        }
    }
}
