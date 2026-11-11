package F;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class r extends G.a {
    public static final Parcelable.Creator CREATOR = new v();
    private final int i;
    private List j;

    public r(int i, List list) {
        this.i = i;
        this.j = list;
    }

    public final int d() {
        return this.i;
    }

    public final List g() {
        return this.j;
    }

    public final void h(l lVar) {
        if (this.j == null) {
            this.j = new ArrayList();
        }
        this.j.add(lVar);
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.i(parcel, 1, this.i);
        G.c.q(parcel, 2, this.j, false);
        G.c.b(parcel, iA);
    }
}
