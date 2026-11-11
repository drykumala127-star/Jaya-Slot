package X;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public final class i implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        ArrayList arrayListE = null;
        String strD = null;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            int i = G.b.i(iN);
            if (i == 1) {
                arrayListE = G.b.e(parcel, iN);
            } else if (i != 2) {
                G.b.t(parcel, iN);
            } else {
                strD = G.b.d(parcel, iN);
            }
        }
        G.b.h(parcel, iU);
        return new h(arrayListE, strD);
    }

    @Override
    public final Object[] newArray(int i) {
        return new h[i];
    }
}
