package F;

import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class i0 implements Handler.Callback {
    final j0 a;

    i0(j0 j0Var, h0 h0Var) {
        this.a = j0Var;
    }

    @Override
    public final boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            synchronized (j0.k(this.a)) {
                try {
                    e0 e0Var = (e0) message.obj;
                    g0 g0Var = (g0) j0.k(this.a).get(e0Var);
                    if (g0Var != null && g0Var.i()) {
                        if (g0Var.j()) {
                            g0Var.g("GmsClientSupervisor");
                        }
                        j0.k(this.a).remove(e0Var);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return true;
        }
        if (i != 1) {
            return false;
        }
        synchronized (j0.k(this.a)) {
            try {
                e0 e0Var2 = (e0) message.obj;
                g0 g0Var2 = (g0) j0.k(this.a).get(e0Var2);
                if (g0Var2 != null && g0Var2.a() == 3) {
                    Log.e("GmsClientSupervisor", "Timeout waiting for ServiceConnection callback " + String.valueOf(e0Var2), new Exception());
                    ComponentName componentNameB = g0Var2.b();
                    if (componentNameB == null) {
                        componentNameB = e0Var2.b();
                    }
                    if (componentNameB == null) {
                        String strD = e0Var2.d();
                        n.i(strD);
                        componentNameB = new ComponentName(strD, "unknown");
                    }
                    g0Var2.onServiceDisconnected(componentNameB);
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
        return true;
    }
}
