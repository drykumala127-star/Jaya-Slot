package p;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class d {
    private static final d c = new a().a();
    private final String a;
    private final List b;

    public static final class a {
        private String a = "";
        private List b = new ArrayList();

        a() {
        }

        public d a() {
            return new d(this.a, Collections.unmodifiableList(this.b));
        }

        public a b(List list) {
            this.b = list;
            return this;
        }

        public a c(String str) {
            this.a = str;
            return this;
        }
    }

    d(String str, List list) {
        this.a = str;
        this.b = list;
    }

    public static a c() {
        return new a();
    }

    public List a() {
        return this.b;
    }

    public String b() {
        return this.a;
    }
}
