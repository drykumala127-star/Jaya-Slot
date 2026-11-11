package F;

import M.a;
import android.os.IBinder;
import android.os.Parcel;

public final class o0 extends R.a implements q0 {
    o0(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ICertData");
    }

    @Override
    public final int c() {
        Parcel parcelA = a(2, e());
        int i = parcelA.readInt();
        parcelA.recycle();
        return i;
    }

    @Override
    public final M.a d() {
        Parcel parcelA = a(1, e());
        M.a aVarE = a.a.e(parcelA.readStrongBinder());
        parcelA.recycle();
        return aVarE;
    }
}
