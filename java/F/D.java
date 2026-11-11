package f;

import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

public final class d {
    private static final d b = a(new Locale[0]);
    private f a;

    private d(f fVar) {
        this.a = fVar;
    }

    public static d a(Locale... localeArr) {
        return Build.VERSION.SDK_INT >= 24 ? d(c.a(localeArr)) : new d(new e(localeArr));
    }

    static Locale b(String str) {
        if (str.contains("-")) {
            String[] strArrSplit = str.split("-", -1);
            if (strArrSplit.length > 2) {
                return new Locale(strArrSplit[0], strArrSplit[1], strArrSplit[2]);
            }
            if (strArrSplit.length > 1) {
                return new Locale(strArrSplit[0], strArrSplit[1]);
            }
            if (strArrSplit.length == 1) {
                return new Locale(strArrSplit[0]);
            }
        } else {
            if (!str.contains("_")) {
                return new Locale(str);
            }
            String[] strArrSplit2 = str.split("_", -1);
            if (strArrSplit2.length > 2) {
                return new Locale(strArrSplit2[0], strArrSplit2[1], strArrSplit2[2]);
            }
            if (strArrSplit2.length > 1) {
                return new Locale(strArrSplit2[0], strArrSplit2[1]);
            }
            if (strArrSplit2.length == 1) {
                return new Locale(strArrSplit2[0]);
            }
        }
        throw new IllegalArgumentException("Can not parse language tag: [" + str + "]");
    }

    public static d d(LocaleList localeList) {
        return new d(new k(localeList));
    }

    public Locale c(int i) {
        return this.a.get(i);
    }

    public boolean equals(Object obj) {
        return (obj instanceof d) && this.a.equals(((d) obj).a);
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    public String toString() {
        return this.a.toString();
    }
}
