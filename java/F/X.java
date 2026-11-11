package F;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;

public final class X implements ServiceConnection {
    private final int a;
    final c b;

    public X(c cVar, int i) {
        this.b = cVar;
        this.a = i;
    }

    @Override
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        c cVar = this.b;
        if (iBinder == null) {
            c.d0(cVar, 16);
            return;
        }
        synchronized (c.W(cVar)) {
            try {
                c cVar2 = this.b;
                IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                c.a0(cVar2, (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof k)) ? new M(iBinder) : (k) iInterfaceQueryLocalInterface);
            } catch (Throwable th) {
                throw th;
            }
        }
        this.b.e0(0, null, this.a);
    }

    @Override
    public final void onServiceDisconnected(ComponentName componentName) {
        synchronized (c.W(this.b)) {
            c.a0(this.b, null);
        }
        Handler handler = this.b.l;
        handler.sendMessage(handler.obtainMessage(6, this.a, 1));
    }
}
