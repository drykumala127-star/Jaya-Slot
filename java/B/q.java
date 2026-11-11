package B;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

final class q implements ServiceConnection {
    r c;
    final w f;
    int a = 0;
    final Messenger b = new Messenger(new Q.f(Looper.getMainLooper(), new j(this)));
    final Queue d = new ArrayDeque();
    final SparseArray e = new SparseArray();

    q(w wVar, p pVar) {
        this.f = wVar;
    }

    final synchronized void a(int i, String str) {
        b(i, str, null);
    }

    final synchronized void b(int i, String str, Throwable th) {
        try {
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                String strValueOf = String.valueOf(str);
                Log.d("MessengerIpcClient", strValueOf.length() != 0 ? "Disconnected: ".concat(strValueOf) : new String("Disconnected: "));
            }
            int i2 = this.a;
            if (i2 == 0) {
                throw new IllegalStateException();
            }
            if (i2 != 1 && i2 != 2) {
                if (i2 != 3) {
                    return;
                }
                this.a = 4;
                return;
            }
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Unbinding service");
            }
            this.a = 4;
            I.b.b().c(w.a(this.f), this);
            u uVar = new u(i, str, th);
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                ((t) it.next()).c(uVar);
            }
            this.d.clear();
            for (int i3 = 0; i3 < this.e.size(); i3++) {
                ((t) this.e.valueAt(i3)).c(uVar);
            }
            this.e.clear();
        } catch (Throwable th2) {
            throw th2;
        }
    }

    final void c() {
        w.e(this.f).execute(new l(this));
    }

    final synchronized void d() {
        if (this.a == 1) {
            a(1, "Timed out while binding");
        }
    }

    final synchronized void e(int i) {
        t tVar = (t) this.e.get(i);
        if (tVar != null) {
            StringBuilder sb = new StringBuilder(31);
            sb.append("Timing out request: ");
            sb.append(i);
            Log.w("MessengerIpcClient", sb.toString());
            this.e.remove(i);
            tVar.c(new u(3, "Timed out waiting for response", null));
            f();
        }
    }

    final synchronized void f() {
        try {
            if (this.a == 2 && this.d.isEmpty() && this.e.size() == 0) {
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
                }
                this.a = 3;
                I.b.b().c(w.a(this.f), this);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    final synchronized boolean g(t tVar) {
        int i = this.a;
        if (i != 0) {
            if (i == 1) {
                this.d.add(tVar);
                return true;
            }
            if (i != 2) {
                return false;
            }
            this.d.add(tVar);
            c();
            return true;
        }
        this.d.add(tVar);
        F.n.l(this.a == 0);
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Starting bind to GmsCore");
        }
        this.a = 1;
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage("com.google.android.gms");
        try {
            if (I.b.b().a(w.a(this.f), intent, this, 1)) {
                w.e(this.f).schedule(new m(this), 30L, TimeUnit.SECONDS);
            } else {
                a(0, "Unable to bind to service");
            }
        } catch (SecurityException e) {
            b(0, "Unable to bind to service", e);
        }
        return true;
    }

    @Override
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        w.e(this.f).execute(new n(this, iBinder));
    }

    @Override
    public final void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        w.e(this.f).execute(new k(this));
    }
}
