package F;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public final class a0 extends G.a {
    public static final Parcelable.Creator CREATOR = new b0();
    Bundle i;
    C.d[] j;
    int k;
    e l;

    a0(Bundle bundle, C.d[] dVarArr, int i, e eVar) {
        this.i = bundle;
        this.j = dVarArr;
        this.k = i;
        this.l = eVar;
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.e(parcel, 1, this.i, false);
        G.c.p(parcel, 2, this.j, i, false);
        G.c.i(parcel, 3, this.k);
        G.c.m(parcel, 4, this.l, i, false);
        G.c.b(parcel, iA);
    }
}
