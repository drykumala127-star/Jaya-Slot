package c0;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class d {
    private final String a;
    private final Set b;
    private final Set c;
    private final int d;
    private final int e;
    private final h f;
    private final Set g;

    public static class b {
        private String a;
        private final Set b;
        private final Set c;
        private int d;
        private int e;
        private h f;
        private Set g;

        b(Class cls, Class[] clsArr, a aVar) {
            this(cls, clsArr);
        }

        static b a(b bVar) {
            return bVar.g();
        }

        private b g() {
            this.e = 1;
            return this;
        }

        private b i(int i) {
            D.d(this.d == 0, "Instantiation type has already been set.");
            this.d = i;
            return this;
        }

        private void j(Class cls) {
            D.a(!this.b.contains(cls), "Components are not allowed to depend on interfaces they themselves provide.");
        }

        public b b(r rVar) {
            D.c(rVar, "Null dependency");
            j(rVar.b());
            this.c.add(rVar);
            return this;
        }

        public b c() {
            return i(1);
        }

        public d d() {
            D.d(this.f != null, "Missing required property: factory.");
            return new d(this.a, new HashSet(this.b), new HashSet(this.c), this.d, this.e, this.f, this.g, null);
        }

        public b e() {
            return i(2);
        }

        public b f(h hVar) {
            this.f = (h) D.c(hVar, "Null factory");
            return this;
        }

        public b h(String str) {
            this.a = str;
            return this;
        }

        private b(Class cls, Class... clsArr) {
            this.a = null;
            HashSet hashSet = new HashSet();
            this.b = hashSet;
            this.c = new HashSet();
            this.d = 0;
            this.e = 0;
            this.g = new HashSet();
            D.c(cls, "Null interface");
            hashSet.add(cls);
            for (Class cls2 : clsArr) {
                D.c(cls2, "Null interface");
            }
            Collections.addAll(this.b, clsArr);
        }
    }

    d(String str, Set set, Set set2, int i, int i2, h hVar, Set set3, a aVar) {
        this(str, set, set2, i, i2, hVar, set3);
    }

    public static Object a(Object obj, e eVar) {
        return p(obj, eVar);
    }

    public static Object b(Object obj, e eVar) {
        return o(obj, eVar);
    }

    public static b c(Class cls) {
        return new b(cls, new Class[0], null);
    }

    public static b d(Class cls, Class... clsArr) {
        return new b(cls, clsArr, null);
    }

    public static d j(Object obj, Class cls) {
        return k(cls).f(new c0.b(obj)).d();
    }

    public static b k(Class cls) {
        return b.a(c(cls));
    }

    public static d q(Object obj, Class cls, Class... clsArr) {
        return d(cls, clsArr).f(new c(obj)).d();
    }

    public Set e() {
        return this.c;
    }

    public h f() {
        return this.f;
    }

    public String g() {
        return this.a;
    }

    public Set h() {
        return this.b;
    }

    public Set i() {
        return this.g;
    }

    public boolean l() {
        return this.d == 1;
    }

    public boolean m() {
        return this.d == 2;
    }

    public boolean n() {
        return this.e == 0;
    }

    public d r(h hVar) {
        return new d(this.a, this.b, this.c, this.d, this.e, hVar, this.g);
    }

    public String toString() {
        return "Component<" + Arrays.toString(this.b.toArray()) + ">{" + this.d + ", type=" + this.e + ", deps=" + Arrays.toString(this.c.toArray()) + "}";
    }

    private d(String str, Set set, Set set2, int i, int i2, h hVar, Set set3) {
        this.a = str;
        this.b = Collections.unmodifiableSet(set);
        this.c = Collections.unmodifiableSet(set2);
        this.d = i;
        this.e = i2;
        this.f = hVar;
        this.g = Collections.unmodifiableSet(set3);
    }

    private static Object o(Object obj, e eVar) {
        return obj;
    }

    private static Object p(Object obj, e eVar) {
        return obj;
    }
}
