package o0;

import o0.c;
import o0.d;

final class a extends d {
    private final String b;
    private final c.a c;
    private final String d;
    private final String e;
    private final long f;
    private final long g;
    private final String h;

    static final class b extends d.a {
        private String a;
        private c.a b;
        private String c;
        private String d;
        private Long e;
        private Long f;
        private String g;

        b(d dVar, a aVar) {
            this(dVar);
        }

        @Override
        public d a() {
            String str = "";
            if (this.b == null) {
                str = " registrationStatus";
            }
            if (this.e == null) {
                str = str + " expiresInSecs";
            }
            if (this.f == null) {
                str = str + " tokenCreationEpochInSecs";
            }
            if (str.isEmpty()) {
                return new a(this.a, this.b, this.c, this.d, this.e.longValue(), this.f.longValue(), this.g, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        public d.a b(String str) {
            this.c = str;
            return this;
        }

        @Override
        public d.a c(long j) {
            this.e = Long.valueOf(j);
            return this;
        }

        @Override
        public d.a d(String str) {
            this.a = str;
            return this;
        }

        @Override
        public d.a e(String str) {
            this.g = str;
            return this;
        }

        @Override
        public d.a f(String str) {
            this.d = str;
            return this;
        }

        @Override
        public d.a g(c.a aVar) {
            if (aVar == null) {
                throw new NullPointerException("Null registrationStatus");
            }
            this.b = aVar;
            return this;
        }

        @Override
        public d.a h(long j) {
            this.f = Long.valueOf(j);
            return this;
        }

        b() {
        }

        private b(d dVar) {
            this.a = dVar.d();
            this.b = dVar.g();
            this.c = dVar.b();
            this.d = dVar.f();
            this.e = Long.valueOf(dVar.c());
            this.f = Long.valueOf(dVar.h());
            this.g = dVar.e();
        }
    }

    a(String str, c.a aVar, String str2, String str3, long j, long j2, String str4, a aVar2) {
        this(str, aVar, str2, str3, j, j2, str4);
    }

    @Override
    public String b() {
        return this.d;
    }

    @Override
    public long c() {
        return this.f;
    }

    @Override
    public String d() {
        return this.b;
    }

    @Override
    public String e() {
        return this.h;
    }

    public boolean equals(Object obj) {
        String str;
        String str2;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof d)) {
            return false;
        }
        d dVar = (d) obj;
        String str3 = this.b;
        if (str3 != null ? str3.equals(dVar.d()) : dVar.d() == null) {
            if (this.c.equals(dVar.g()) && ((str = this.d) != null ? str.equals(dVar.b()) : dVar.b() == null) && ((str2 = this.e) != null ? str2.equals(dVar.f()) : dVar.f() == null) && this.f == dVar.c() && this.g == dVar.h()) {
                String str4 = this.h;
                if (str4 == null) {
                    if (dVar.e() == null) {
                        return true;
                    }
                } else if (str4.equals(dVar.e())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String f() {
        return this.e;
    }

    @Override
    public c.a g() {
        return this.c;
    }

    @Override
    public long h() {
        return this.g;
    }

    public int hashCode() {
        String str = this.b;
        int iHashCode = ((((str == null ? 0 : str.hashCode()) ^ 1000003) * 1000003) ^ this.c.hashCode()) * 1000003;
        String str2 = this.d;
        int iHashCode2 = (iHashCode ^ (str2 == null ? 0 : str2.hashCode())) * 1000003;
        String str3 = this.e;
        int iHashCode3 = (iHashCode2 ^ (str3 == null ? 0 : str3.hashCode())) * 1000003;
        long j = this.f;
        int i = (iHashCode3 ^ ((int) (j ^ (j >>> 32)))) * 1000003;
        long j2 = this.g;
        int i2 = (i ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003;
        String str4 = this.h;
        return i2 ^ (str4 != null ? str4.hashCode() : 0);
    }

    @Override
    public d.a n() {
        return new b(this, null);
    }

    public String toString() {
        return "PersistedInstallationEntry{firebaseInstallationId=" + this.b + ", registrationStatus=" + this.c + ", authToken=" + this.d + ", refreshToken=" + this.e + ", expiresInSecs=" + this.f + ", tokenCreationEpochInSecs=" + this.g + ", fisError=" + this.h + "}";
    }

    private a(String str, c.a aVar, String str2, String str3, long j, long j2, String str4) {
        this.b = str;
        this.c = aVar;
        this.d = str2;
        this.e = str3;
        this.f = j;
        this.g = j2;
        this.h = str4;
    }
}
