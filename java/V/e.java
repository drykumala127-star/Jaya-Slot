package V;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.internal.measurement.Q;
import com.google.android.gms.internal.measurement.S;
import com.google.android.gms.measurement.internal.e4;
import com.google.android.gms.measurement.internal.o4;
import java.util.List;

public abstract class e extends Q implements f {
    public e() {
        super("com.google.android.gms.measurement.internal.IMeasurementService");
    }

    protected final boolean a(int i, Parcel parcel, Parcel parcel2, int i2) {
        switch (i) {
            case 1:
                com.google.android.gms.measurement.internal.v vVar = (com.google.android.gms.measurement.internal.v) S.a(parcel, com.google.android.gms.measurement.internal.v.CREATOR);
                o4 o4Var = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                C(vVar, o4Var);
                parcel2.writeNoException();
                return true;
            case 2:
                e4 e4Var = (e4) S.a(parcel, e4.CREATOR);
                o4 o4Var2 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                n(e4Var, o4Var2);
                parcel2.writeNoException();
                return true;
            case 3:
            case 8:
            default:
                return false;
            case 4:
                o4 o4Var3 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                G(o4Var3);
                parcel2.writeNoException();
                return true;
            case 5:
                com.google.android.gms.measurement.internal.v vVar2 = (com.google.android.gms.measurement.internal.v) S.a(parcel, com.google.android.gms.measurement.internal.v.CREATOR);
                String string = parcel.readString();
                String string2 = parcel.readString();
                S.c(parcel);
                l(vVar2, string, string2);
                parcel2.writeNoException();
                return true;
            case 6:
                o4 o4Var4 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                m(o4Var4);
                parcel2.writeNoException();
                return true;
            case 7:
                o4 o4Var5 = (o4) S.a(parcel, o4.CREATOR);
                boolean zG = S.g(parcel);
                S.c(parcel);
                List listW = w(o4Var5, zG);
                parcel2.writeNoException();
                parcel2.writeTypedList(listW);
                return true;
            case 9:
                com.google.android.gms.measurement.internal.v vVar3 = (com.google.android.gms.measurement.internal.v) S.a(parcel, com.google.android.gms.measurement.internal.v.CREATOR);
                String string3 = parcel.readString();
                S.c(parcel);
                byte[] bArrX = x(vVar3, string3);
                parcel2.writeNoException();
                parcel2.writeByteArray(bArrX);
                return true;
            case 10:
                long j = parcel.readLong();
                String string4 = parcel.readString();
                String string5 = parcel.readString();
                String string6 = parcel.readString();
                S.c(parcel);
                i(j, string4, string5, string6);
                parcel2.writeNoException();
                return true;
            case 11:
                o4 o4Var6 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                String strB = B(o4Var6);
                parcel2.writeNoException();
                parcel2.writeString(strB);
                return true;
            case 12:
                com.google.android.gms.measurement.internal.d dVar = (com.google.android.gms.measurement.internal.d) S.a(parcel, com.google.android.gms.measurement.internal.d.CREATOR);
                o4 o4Var7 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                p(dVar, o4Var7);
                parcel2.writeNoException();
                return true;
            case 13:
                com.google.android.gms.measurement.internal.d dVar2 = (com.google.android.gms.measurement.internal.d) S.a(parcel, com.google.android.gms.measurement.internal.d.CREATOR);
                S.c(parcel);
                u(dVar2);
                parcel2.writeNoException();
                return true;
            case 14:
                String string7 = parcel.readString();
                String string8 = parcel.readString();
                boolean zG2 = S.g(parcel);
                o4 o4Var8 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                List listA = A(string7, string8, zG2, o4Var8);
                parcel2.writeNoException();
                parcel2.writeTypedList(listA);
                return true;
            case 15:
                String string9 = parcel.readString();
                String string10 = parcel.readString();
                String string11 = parcel.readString();
                boolean zG3 = S.g(parcel);
                S.c(parcel);
                List listS = s(string9, string10, string11, zG3);
                parcel2.writeNoException();
                parcel2.writeTypedList(listS);
                return true;
            case 16:
                String string12 = parcel.readString();
                String string13 = parcel.readString();
                o4 o4Var9 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                List listH = H(string12, string13, o4Var9);
                parcel2.writeNoException();
                parcel2.writeTypedList(listH);
                return true;
            case 17:
                String string14 = parcel.readString();
                String string15 = parcel.readString();
                String string16 = parcel.readString();
                S.c(parcel);
                List listK = K(string14, string15, string16);
                parcel2.writeNoException();
                parcel2.writeTypedList(listK);
                return true;
            case 18:
                o4 o4Var10 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                h(o4Var10);
                parcel2.writeNoException();
                return true;
            case 19:
                Bundle bundle = (Bundle) S.a(parcel, Bundle.CREATOR);
                o4 o4Var11 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                r(bundle, o4Var11);
                parcel2.writeNoException();
                return true;
            case 20:
                o4 o4Var12 = (o4) S.a(parcel, o4.CREATOR);
                S.c(parcel);
                y(o4Var12);
                parcel2.writeNoException();
                return true;
        }
    }
}
