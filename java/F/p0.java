package F;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public abstract class p0 extends R.b implements q0 {
    public p0() {
        super("com.google.android.gms.common.internal.ICertData");
    }

    public static q0 e(IBinder iBinder) {
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ICertData");
        return iInterfaceQueryLocalInterface instanceof q0 ? (q0) iInterfaceQueryLocalInterface : new o0(iBinder);
    }

    @Override
    protected final boolean a(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i == 1) {
            M.a aVarD = d();
            parcel2.writeNoException();
            R.c.e(parcel2, aVarD);
        } else {
            if (i != 2) {
                return false;
            }
            int iC = c();
            parcel2.writeNoException();
            parcel2.writeInt(iC);
        }
        return true;
    }
}
