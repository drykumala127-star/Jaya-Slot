package o0;

import o0.a;
import o0.c;

public abstract class d {
    public static d a = a().a();

    public static abstract class a {
        public abstract d a();

        public abstract a b(String str);

        public abstract a c(long j);

        public abstract a d(String str);

        public abstract a e(String str);

        public abstract a f(String str);

        public abstract a g(c.a aVar);

        public abstract a h(long j);
    }

    public static a a() {
        return new a.b().h(0L).g(c.a.ATTEMPT_MIGRATION).c(0L);
    }

    public abstract String b();

    public abstract long c();

    public abstract String d();

    public abstract String e();

    public abstract String f();

    public abstract c.a g();

    public abstract long h();

    public boolean i() {
        return g() == c.a.REGISTER_ERROR;
    }

    public boolean j() {
        return g() == c.a.NOT_GENERATED || g() == c.a.ATTEMPT_MIGRATION;
    }

    public boolean k() {
        return g() == c.a.REGISTERED;
    }

    public boolean l() {
        return g() == c.a.UNREGISTERED;
    }

    public boolean m() {
        return g() == c.a.ATTEMPT_MIGRATION;
    }

    public abstract a n();

    public d o(String str, long j, long j2) {
        return n().b(str).c(j).h(j2).a();
    }

    public d p() {
        return n().b(null).a();
    }

    public d q(String str) {
        return n().e(str).g(c.a.REGISTER_ERROR).a();
    }

    public d r() {
        return n().g(c.a.NOT_GENERATED).a();
    }

    public d s(String str, String str2, long j, String str3, long j2) {
        return n().d(str).g(c.a.REGISTERED).b(str3).f(str2).c(j2).h(j).a();
    }

    public d t(String str) {
        return n().d(str).g(c.a.UNREGISTERED).a();
    }
}
