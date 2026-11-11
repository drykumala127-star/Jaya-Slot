package X;

import android.os.Parcel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;

public abstract class e extends P.b implements f {
    public e() {
        super("com.google.android.gms.signin.internal.ISignInCallbacks");
    }

    @Override
    protected final boolean L(int i, Parcel parcel, Parcel parcel2, int i2) {
        switch (i) {
            case 3:
                P.c.b(parcel);
                break;
            case 4:
                P.c.a(parcel, Status.CREATOR);
                P.c.b(parcel);
                break;
            case 5:
            default:
                return false;
            case 6:
                P.c.a(parcel, Status.CREATOR);
                P.c.b(parcel);
                break;
            case 7:
                P.c.a(parcel, Status.CREATOR);
                P.c.a(parcel, GoogleSignInAccount.CREATOR);
                P.c.b(parcel);
                break;
            case 8:
                l lVar = (l) P.c.a(parcel, l.CREATOR);
                P.c.b(parcel);
                D(lVar);
                break;
            case 9:
                P.c.b(parcel);
                break;
        }
        parcel2.writeNoException();
        return true;
    }
}
