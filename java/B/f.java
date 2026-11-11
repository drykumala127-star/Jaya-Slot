package B;

import android.os.Parcel;
import android.os.Parcelable;

final class f implements Parcelable.Creator {
    f() {
    }

    @Override
    public final Object createFromParcel(Parcel parcel) {
        return new h(parcel.readStrongBinder());
    }

    @Override
    public final Object[] newArray(int i) {
        return new h[i];
    }
}
