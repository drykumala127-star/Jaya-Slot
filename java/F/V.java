package F;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public final class v implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        int iP = 0;
        ArrayList arrayListG = null;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            int i = G.b.i(iN);
            if (i == 1) {
                iP = G.b.p(parcel, iN);
            } else if (i != 2) {
                G.b.t(parcel, iN);
            } else {
                arrayListG = G.b.g(parcel, iN, l.CREATOR);
            }
        }
        G.b.h(parcel, iU);
        return new r(iP, arrayListG);
    }

    @Override
    public final Object[] newArray(int i) {
        return new r[i];
    }
}
