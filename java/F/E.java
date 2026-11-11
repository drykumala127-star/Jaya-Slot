package f;

import java.util.HashSet;
import java.util.Locale;

final class e implements f {
    private static final Locale[] c = new Locale[0];
    private static final Locale d = new Locale("en", "XA");
    private static final Locale e = new Locale("ar", "XB");
    private static final Locale f = d.b("en-Latn");
    private final Locale[] a;
    private final String b;

    e(Locale... localeArr) {
        if (localeArr.length == 0) {
            this.a = c;
            this.b = "";
            return;
        }
        Locale[] localeArr2 = new Locale[localeArr.length];
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < localeArr.length; i++) {
            Locale locale = localeArr[i];
            if (locale == null) {
                throw new NullPointerException("list[" + i + "] is null");
            }
            if (hashSet.contains(locale)) {
                throw new IllegalArgumentException("list[" + i + "] is a repetition");
            }
            Locale locale2 = (Locale) locale.clone();
            localeArr2[i] = locale2;
            b(sb, locale2);
            if (i < localeArr.length - 1) {
                sb.append(',');
            }
            hashSet.add(locale2);
        }
        this.a = localeArr2;
        this.b = sb.toString();
    }

    static void b(StringBuilder sb, Locale locale) {
        sb.append(locale.getLanguage());
        String country = locale.getCountry();
        if (country == null || country.isEmpty()) {
            return;
        }
        sb.append('-');
        sb.append(locale.getCountry());
    }

    @Override
    public Object a() {
        return null;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof e)) {
            return false;
        }
        Locale[] localeArr = ((e) obj).a;
        if (this.a.length != localeArr.length) {
            return false;
        }
        int i = 0;
        while (true) {
            Locale[] localeArr2 = this.a;
            if (i >= localeArr2.length) {
                return true;
            }
            if (!localeArr2[i].equals(localeArr[i])) {
                return false;
            }
            i++;
        }
    }

    @Override
    public Locale get(int i) {
        if (i >= 0) {
            Locale[] localeArr = this.a;
            if (i < localeArr.length) {
                return localeArr[i];
            }
        }
        return null;
    }

    public int hashCode() {
        int iHashCode = 1;
        int i = 0;
        while (true) {
            Locale[] localeArr = this.a;
            if (i >= localeArr.length) {
                return iHashCode;
            }
            iHashCode = (iHashCode * 31) + localeArr[i].hashCode();
            i++;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        while (true) {
            Locale[] localeArr = this.a;
            if (i >= localeArr.length) {
                sb.append("]");
                return sb.toString();
            }
            sb.append(localeArr[i]);
            if (i < this.a.length - 1) {
                sb.append(',');
            }
            i++;
        }
    }
}
