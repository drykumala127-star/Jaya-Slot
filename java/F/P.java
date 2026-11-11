package F;

import android.os.Parcel;
import android.os.Parcelable;

public class p extends G.a {
    public static final Parcelable.Creator CREATOR = new T();
    private final int i;
    private final boolean j;
    private final boolean k;
    private final int l;
    private final int m;

    public p(int i, boolean z, boolean z2, int i2, int i3) {
        this.i = i;
        this.j = z;
        this.k = z2;
        this.l = i2;
        this.m = i3;
    }

    public int d() {
        return this.l;
    }

    public int g() {
        return this.m;
    }

    public boolean h() {
        return this.j;
    }

    public boolean j() {
        return this.k;
    }

    public int k() {
        return this.i;
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.i(parcel, 1, k());
        G.c.c(parcel, 2, h());
        G.c.c(parcel, 3, j());
        G.c.i(parcel, 4, d());
        G.c.i(parcel, 5, g());
        G.c.b(parcel, iA);
    }
}
