package p;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import m.m;

public final class a {
    private static final a e = new a().b();
    private final f a;
    private final List b;
    private final b c;
    private final String d;

    public static final class a {
        private f a = null;
        private List b = new ArrayList();
        private b c = null;
        private String d = "";

        a() {
        }

        public a a(d dVar) {
            this.b.add(dVar);
            return this;
        }

        public a b() {
            return new a(this.a, Collections.unmodifiableList(this.b), this.c, this.d);
        }

        public a c(String str) {
            this.d = str;
            return this;
        }

        public a d(b bVar) {
            this.c = bVar;
            return this;
        }

        public a e(f fVar) {
            this.a = fVar;
            return this;
        }
    }

    a(f fVar, List list, b bVar, String str) {
        this.a = fVar;
        this.b = list;
        this.c = bVar;
        this.d = str;
    }

    public static a e() {
        return new a();
    }

    public String a() {
        return this.d;
    }

    public b b() {
        return this.c;
    }

    public List c() {
        return this.b;
    }

    public f d() {
        return this.a;
    }

    public byte[] f() {
        return m.a(this);
    }
}
