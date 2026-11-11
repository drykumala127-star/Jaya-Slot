package X;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class c implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        int iP = 0;
        Intent intent = null;
        int iP2 = 0;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            int i = G.b.i(iN);
            if (i == 1) {
                iP = G.b.p(parcel, iN);
            } else if (i == 2) {
                iP2 = G.b.p(parcel, iN);
            } else if (i != 3) {
                G.b.t(parcel, iN);
            } else {
                intent = (Intent) G.b.c(parcel, iN, Intent.CREATOR);
            }
        }
        G.b.h(parcel, iU);
        return new b(iP, iP2, intent);
    }

    @Override
    public final Object[] newArray(int i) {
        return new b[i];
    }
}
