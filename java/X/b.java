package X;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class b extends G.a {
    public static final Parcelable.Creator CREATOR = new c();
    final int i;
    private int j;
    private Intent k;

    b(int i, int i2, Intent intent) {
        this.i = i;
        this.j = i2;
        this.k = intent;
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.i(parcel, 1, this.i);
        G.c.i(parcel, 2, this.j);
        G.c.m(parcel, 3, this.k, i, false);
        G.c.b(parcel, iA);
    }
}
