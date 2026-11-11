package F;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public final class W extends L {
    private c a;
    private final int b;

    public W(c cVar, int i) {
        this.a = cVar;
        this.b = i;
    }

    @Override
    public final void E(int i, IBinder iBinder, a0 a0Var) {
        c cVar = this.a;
        n.j(cVar, "onPostInitCompleteWithConnectionInfo can be called only once per call togetRemoteService");
        n.i(a0Var);
        c.c0(cVar, a0Var);
        I(i, iBinder, a0Var.i);
    }

    @Override
    public final void I(int i, IBinder iBinder, Bundle bundle) {
        n.j(this.a, "onPostInitComplete can be called only once per call to getRemoteService");
        this.a.N(i, iBinder, bundle, this.b);
        this.a = null;
    }

    @Override
    public final void k(int i, Bundle bundle) {
        Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
    }
}
