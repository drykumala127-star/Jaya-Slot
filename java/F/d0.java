package F;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;

public final class d0 implements Parcelable.Creator {
    static void a(f fVar, Parcel parcel, int i) {
        int iA = G.c.a(parcel);
        G.c.i(parcel, 1, fVar.i);
        G.c.i(parcel, 2, fVar.j);
        G.c.i(parcel, 3, fVar.k);
        G.c.n(parcel, 4, fVar.l, false);
        G.c.h(parcel, 5, fVar.m, false);
        G.c.p(parcel, 6, fVar.n, i, false);
        G.c.e(parcel, 7, fVar.o, false);
        G.c.m(parcel, 8, fVar.p, i, false);
        G.c.p(parcel, 10, fVar.q, i, false);
        G.c.p(parcel, 11, fVar.r, i, false);
        G.c.c(parcel, 12, fVar.s);
        G.c.i(parcel, 13, fVar.t);
        G.c.c(parcel, 14, fVar.u);
        G.c.n(parcel, 15, fVar.d(), false);
        G.c.b(parcel, iA);
    }

    @Override
    public final Object createFromParcel(Parcel parcel) {
        int iU = G.b.u(parcel);
        Scope[] scopeArr = f.w;
        Bundle bundle = new Bundle();
        C.d[] dVarArr = f.x;
        C.d[] dVarArr2 = dVarArr;
        String strD = null;
        IBinder iBinderO = null;
        Account account = null;
        String strD2 = null;
        int iP = 0;
        int iP2 = 0;
        int iP3 = 0;
        boolean zJ = false;
        int iP4 = 0;
        boolean zJ2 = false;
        while (parcel.dataPosition() < iU) {
            int iN = G.b.n(parcel);
            switch (G.b.i(iN)) {
                case 1:
                    iP = G.b.p(parcel, iN);
                    break;
                case 2:
                    iP2 = G.b.p(parcel, iN);
                    break;
                case 3:
                    iP3 = G.b.p(parcel, iN);
                    break;
                case 4:
                    strD = G.b.d(parcel, iN);
                    break;
                case 5:
                    iBinderO = G.b.o(parcel, iN);
                    break;
                case 6:
                    scopeArr = (Scope[]) G.b.f(parcel, iN, Scope.CREATOR);
                    break;
                case 7:
                    bundle = G.b.a(parcel, iN);
                    break;
                case 8:
                    account = (Account) G.b.c(parcel, iN, Account.CREATOR);
                    break;
                case 9:
                default:
                    G.b.t(parcel, iN);
                    break;
                case 10:
                    dVarArr = (C.d[]) G.b.f(parcel, iN, C.d.CREATOR);
                    break;
                case 11:
                    dVarArr2 = (C.d[]) G.b.f(parcel, iN, C.d.CREATOR);
                    break;
                case 12:
                    zJ = G.b.j(parcel, iN);
                    break;
                case 13:
                    iP4 = G.b.p(parcel, iN);
                    break;
                case 14:
                    zJ2 = G.b.j(parcel, iN);
                    break;
                case 15:
                    strD2 = G.b.d(parcel, iN);
                    break;
            }
        }
        G.b.h(parcel, iU);
        return new f(iP, iP2, iP3, strD, iBinderO, scopeArr, bundle, account, dVarArr, dVarArr2, zJ, iP4, zJ2, strD2);
    }

    @Override
    public final Object[] newArray(int i) {
        return new f[i];
    }
}
