package H;

import E.h;
import F.g;
import F.u;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;

public final class e extends g {
    private final u I;

    public e(Context context, Looper looper, F.d dVar, u uVar, E.c cVar, h hVar) {
        super(context, looper, 270, dVar, cVar, hVar);
        this.I = uVar;
    }

    @Override
    protected final Bundle A() {
        return this.I.b();
    }

    @Override
    protected final String E() {
        return "com.google.android.gms.common.internal.service.IClientTelemetryService";
    }

    @Override
    protected final String F() {
        return "com.google.android.gms.common.telemetry.service.START";
    }

    @Override
    protected final boolean I() {
        return true;
    }

    @Override
    public final int p() {
        return 203400000;
    }

    @Override
    protected final IInterface s(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.service.IClientTelemetryService");
        return iInterfaceQueryLocalInterface instanceof a ? (a) iInterfaceQueryLocalInterface : new a(iBinder);
    }

    @Override
    public final C.d[] v() {
        return P.d.b;
    }
}
