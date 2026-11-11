package F;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public final class b0 implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        Bundle bundleA = null;
        C.d[] dVarArr = null;
        e eVar = null;
        int iP = 0;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            int i = G.b.i(iN);
            if (i == 1) {
                bundleA = G.b.a(parcel, iN);
            } else if (i == 2) {
                dVarArr = (C.d[]) G.b.f(parcel, iN, C.d.CREATOR);
            } else if (i == 3) {
                iP = G.b.p(parcel, iN);
            } else if (i != 4) {
                G.b.t(parcel, iN);
            } else {
                eVar = (e) G.b.c(parcel, iN, e.CREATOR);
            }
        }
        G.b.h(parcel, iU);
        return new a0(bundleA, dVarArr, iP, eVar);
    }

    @Override
    public final Object[] newArray(int i) {
        return new a0[i];
    }
}
