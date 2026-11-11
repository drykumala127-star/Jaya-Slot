package X;

import F.G;
import android.os.Parcel;
import android.os.Parcelable;

public final class k implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        int iP = 0;
        G g = null;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            int i = G.b.i(iN);
            if (i == 1) {
                iP = G.b.p(parcel, iN);
            } else if (i != 2) {
                G.b.t(parcel, iN);
            } else {
                g = (G) G.b.c(parcel, iN, G.CREATOR);
            }
        }
        G.b.h(parcel, iU);
        return new j(iP, g);
    }

    @Override
    public final Object[] newArray(int i) {
        return new j[i];
    }
}
