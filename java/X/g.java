package X;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public final class g extends P.a implements IInterface {
    g(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.signin.internal.ISignInService");
    }

    public final void L(j jVar, f fVar) {
        Parcel parcelA = a();
        P.c.c(parcelA, jVar);
        P.c.d(parcelA, fVar);
        e(12, parcelA);
    }
}
