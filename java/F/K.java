package f;

import android.os.LocaleList;
import java.util.Locale;

final class k implements f {
    private final LocaleList a;

    k(LocaleList localeList) {
        this.a = localeList;
    }

    @Override
    public Object a() {
        return this.a;
    }

    public boolean equals(Object obj) {
        return g.a(this.a, ((f) obj).a());
    }

    @Override
    public Locale get(int i) {
        return i.a(this.a, i);
    }

    public int hashCode() {
        return j.a(this.a);
    }

    public String toString() {
        return h.a(this.a);
    }
}
