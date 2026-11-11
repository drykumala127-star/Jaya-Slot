package X;

import F.I;
import android.os.Parcel;
import android.os.Parcelable;

public final class l extends G.a {
    public static final Parcelable.Creator CREATOR = new m();
    final int i;
    private final C.b j;
    private final I k;

    l(int i, C.b bVar, I i2) {
        this.i = i;
        this.j = bVar;
        this.k = i2;
    }

    public final C.b d() {
        return this.j;
    }

    public final I g() {
        return this.k;
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.i(parcel, 1, this.i);
        G.c.m(parcel, 2, this.j, i, false);
        G.c.m(parcel, 3, this.k, i, false);
        G.c.b(parcel, iA);
    }
}
