package X;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public final class h extends G.a {
    public static final Parcelable.Creator CREATOR = new i();
    private final List i;
    private final String j;

    public h(List list, String str) {
        this.i = list;
        this.j = str;
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.o(parcel, 1, this.i, false);
        G.c.n(parcel, 2, this.j, false);
        G.c.b(parcel, iA);
    }
}
