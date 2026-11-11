package F;

import android.accounts.Account;
import android.os.IBinder;
import android.os.Parcel;

public final class n0 extends R.a implements i {
    n0(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IAccountAccessor");
    }

    @Override
    public final Account b() {
        Parcel parcelA = a(2, e());
        Account account = (Account) R.c.a(parcelA, Account.CREATOR);
        parcelA.recycle();
        return account;
    }
}
