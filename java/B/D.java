package B;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class d implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        Intent intent = null;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            if (G.b.i(iN) != 1) {
                G.b.t(parcel, iN);
            } else {
                intent = (Intent) G.b.c(parcel, iN, Intent.CREATOR);
            }
        }
        G.b.h(parcel, iU);
        return new a(intent);
    }

    @Override
    public final Object[] newArray(int i) {
        return new a[i];
    }
}
