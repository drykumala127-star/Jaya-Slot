package B;

import android.os.IBinder;
import android.os.RemoteException;

public final class n implements Runnable {
    public final q i;
    public final IBinder j;

    public n(q qVar, IBinder iBinder) {
        this.i = qVar;
        this.j = iBinder;
    }

    @Override
    public final void run() {
        q qVar = this.i;
        IBinder iBinder = this.j;
        synchronized (qVar) {
            try {
                if (iBinder == null) {
                    qVar.a(0, "Null service connection");
                    return;
                }
                try {
                    qVar.c = new r(iBinder);
                    qVar.a = 2;
                    qVar.c();
                } catch (RemoteException e) {
                    qVar.a(0, e.getMessage());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
