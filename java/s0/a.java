package s0;

public final class a {
    private static final a p = new a().a();
    private final long a;
    private final String b;
    private final String c;
    private final c d;
    private final d e;
    private final String f;
    private final String g;
    private final int h;
    private final int i;
    private final String j;
    private final long k;
    private final b l;
    private final String m;
    private final long n;
    private final String o;

    public static final class a {
        private long a = 0;
        private String b = "";
        private String c = "";
        private c d = c.UNKNOWN;
        private d e = d.UNKNOWN_OS;
        private String f = "";
        private String g = "";
        private int h = 0;
        private int i = 0;
        private String j = "";
        private long k = 0;
        private b l = b.UNKNOWN_EVENT;
        private String m = "";
        private long n = 0;
        private String o = "";

        a() {
        }

        public a a() {
            return new a(this.a, this.b, this.c, this.d, this.e, this.f, this.g, this.h, this.i, this.j, this.k, this.l, this.m, this.n, this.o);
        }

        public a b(String str) {
            this.m = str;
            return this;
        }

        public a c(String str) {
            this.g = str;
            return this;
        }

        public a d(String str) {
            this.o = str;
            return this;
        }

        public a e(b bVar) {
            this.l = bVar;
            return this;
        }

        public a f(String str) {
            this.c = str;
            return this;
        }

        public a g(String str) {
            this.b = str;
            return this;
        }

        public a h(c cVar) {
            this.d = cVar;
            return this;
        }

        public a i(String str) {
            this.f = str;
            return this;
        }

        public a j(long j) {
            this.a = j;
            return this;
        }

        public a k(d dVar) {
            this.e = dVar;
            return this;
        }

        public a l(String str) {
            this.j = str;
            return this;
        }

        public a m(int i) {
            this.i = i;
            return this;
        }
    }

    public enum b implements i0.c {
        UNKNOWN_EVENT(0),
        MESSAGE_DELIVERED(1),
        MESSAGE_OPEN(2);

        private final int i;

        b(int i) {
            this.i = i;
        }

        @Override
        public int a() {
            return this.i;
        }
    }

    public enum c implements i0.c {
        UNKNOWN(0),
        DATA_MESSAGE(1),
        TOPIC(2),
        DISPLAY_NOTIFICATION(3);

        private final int i;

        c(int i) {
            this.i = i;
        }

        @Override
        public int a() {
            return this.i;
        }
    }

    public enum d implements i0.c {
        UNKNOWN_OS(0),
        ANDROID(1),
        IOS(2),
        WEB(3);

        private final int i;

        d(int i) {
            this.i = i;
        }

        @Override
        public int a() {
            return this.i;
        }
    }

    a(long j, String str, String str2, c cVar, d dVar, String str3, String str4, int i, int i2, String str5, long j2, b bVar, String str6, long j3, String str7) {
        this.a = j;
        this.b = str;
        this.c = str2;
        this.d = cVar;
        this.e = dVar;
        this.f = str3;
        this.g = str4;
        this.h = i;
        this.i = i2;
        this.j = str5;
        this.k = j2;
        this.l = bVar;
        this.m = str6;
        this.n = j3;
        this.o = str7;
    }

    public static a p() {
        return new a();
    }

    public String a() {
        return this.m;
    }

    public long b() {
        return this.k;
    }

    public long c() {
        return this.n;
    }

    public String d() {
        return this.g;
    }

    public String e() {
        return this.o;
    }

    public b f() {
        return this.l;
    }

    public String g() {
        return this.c;
    }

    public String h() {
        return this.b;
    }

    public c i() {
        return this.d;
    }

    public String j() {
        return this.f;
    }

    public int k() {
        return this.h;
    }

    public long l() {
        return this.a;
    }

    public d m() {
        return this.e;
    }

    public String n() {
        return this.j;
    }

    public int o() {
        return this.i;
    }
}
