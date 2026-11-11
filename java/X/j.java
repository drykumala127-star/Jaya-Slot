package X;

import F.G;
import android.os.Parcel;
import android.os.Parcelable;

public final class j extends G.a {
    public static final Parcelable.Creator CREATOR = new k();
    final int i;
    final G j;

    j(int i, G g) {
        this.i = i;
        this.j = g;
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.i(parcel, 1, this.i);
        G.c.m(parcel, 2, this.j, i, false);
        G.c.b(parcel, iA);
    }
}
