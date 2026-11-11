package X;

import F.I;
import android.os.Parcel;
import android.os.Parcelable;

public final class m implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        int iP = 0;
        C.b bVar = null;
        I i = null;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            int i2 = G.b.i(iN);
            if (i2 == 1) {
                iP = G.b.p(parcel, iN);
            } else if (i2 == 2) {
                bVar = (C.b) G.b.c(parcel, iN, C.b.CREATOR);
            } else if (i2 != 3) {
                G.b.t(parcel, iN);
            } else {
                i = (I) G.b.c(parcel, iN, I.CREATOR);
            }
        }
        G.b.h(parcel, iU);
        return new l(iP, bVar, i);
    }

    @Override
    public final Object[] newArray(int i) {
        return new l[i];
    }
}
