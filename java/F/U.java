package F;

import D.a;
import android.os.Bundle;

public class u implements a.d {
    public static final u b = a().a();
    private final String a;

    public static class a {
        private String a;

        a(w wVar) {
        }

        public u a() {
            return new u(this.a, null);
        }
    }

    u(String str, x xVar) {
        this.a = str;
    }

    public static a a() {
        return new a(null);
    }

    public final Bundle b() {
        Bundle bundle = new Bundle();
        String str = this.a;
        if (str != null) {
            bundle.putString("api", str);
        }
        return bundle;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof u) {
            return m.a(this.a, ((u) obj).a);
        }
        return false;
    }

    public final int hashCode() {
        return m.b(this.a);
    }
}
