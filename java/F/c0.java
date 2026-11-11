package F;

import android.os.Parcel;
import android.os.Parcelable;

public final class c0 implements Parcelable.Creator {
    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        p pVar = null;
        int[] iArrB = null;
        int[] iArrB2 = null;
        boolean zJ = false;
        boolean zJ2 = false;
        int iP = 0;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            switch (G.b.i(iN)) {
                case 1:
                    pVar = (p) G.b.c(parcel, iN, p.CREATOR);
                    break;
                case 2:
                    zJ = G.b.j(parcel, iN);
                    break;
                case 3:
                    zJ2 = G.b.j(parcel, iN);
                    break;
                case 4:
                    iArrB = G.b.b(parcel, iN);
                    break;
                case 5:
                    iP = G.b.p(parcel, iN);
                    break;
                case 6:
                    iArrB2 = G.b.b(parcel, iN);
                    break;
                default:
                    G.b.t(parcel, iN);
                    break;
            }
        }
        G.b.h(parcel, iU);
        return new e(pVar, zJ, zJ2, iArrB, iP, iArrB2);
    }

    @Override
    public final Object[] newArray(int i) {
        return new e[i];
    }
}
