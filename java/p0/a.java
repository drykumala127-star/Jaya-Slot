package p0;

import p0.d;

final class a extends d {
    private final String a;
    private final String b;
    private final String c;
    private final f d;
    private final d.b e;

    static final class b extends d.a {
        private String a;
        private String b;
        private String c;
        private f d;
        private d.b e;

        b() {
        }

        @Override
        public d a() {
            return new a(this.a, this.b, this.c, this.d, this.e, null);
        }

        @Override
        public d.a b(f fVar) {
            this.d = fVar;
            return this;
        }

        @Override
        public d.a c(String str) {
            this.b = str;
            return this;
        }

        @Override
        public d.a d(String str) {
            this.c = str;
            return this;
        }

        @Override
        public d.a e(d.b bVar) {
            this.e = bVar;
            return this;
        }

        @Override
        public d.a f(String str) {
            this.a = str;
            return this;
        }
    }

    a(String str, String str2, String str3, f fVar, d.b bVar, a aVar) {
        this(str, str2, str3, fVar, bVar);
    }

    @Override
    public f b() {
        return this.d;
    }

    @Override
    public String c() {
        return this.b;
    }

    @Override
    public String d() {
        return this.c;
    }

    @Override
    public d.b e() {
        return this.e;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof d)) {
            return false;
        }
        d dVar = (d) obj;
        String str = this.a;
        if (str != null ? str.equals(dVar.f()) : dVar.f() == null) {
            String str2 = this.b;
            if (str2 != null ? str2.equals(dVar.c()) : dVar.c() == null) {
                String str3 = this.c;
                if (str3 != null ? str3.equals(dVar.d()) : dVar.d() == null) {
                    f fVar = this.d;
                    if (fVar != null ? fVar.equals(dVar.b()) : dVar.b() == null) {
                        d.b bVar = this.e;
                        if (bVar == null) {
                            if (dVar.e() == null) {
                                return true;
                            }
                        } else if (bVar.equals(dVar.e())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String f() {
        return this.a;
    }

    public int hashCode() {
        String str = this.a;
        int iHashCode = ((str == null ? 0 : str.hashCode()) ^ 1000003) * 1000003;
        String str2 = this.b;
        int iHashCode2 = (iHashCode ^ (str2 == null ? 0 : str2.hashCode())) * 1000003;
        String str3 = this.c;
        int iHashCode3 = (iHashCode2 ^ (str3 == null ? 0 : str3.hashCode())) * 1000003;
        f fVar = this.d;
        int iHashCode4 = (iHashCode3 ^ (fVar == null ? 0 : fVar.hashCode())) * 1000003;
        d.b bVar = this.e;
        return iHashCode4 ^ (bVar != null ? bVar.hashCode() : 0);
    }

    public String toString() {
        return "InstallationResponse{uri=" + this.a + ", fid=" + this.b + ", refreshToken=" + this.c + ", authToken=" + this.d + ", responseCode=" + this.e + "}";
    }

    private a(String str, String str2, String str3, f fVar, d.b bVar) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = fVar;
        this.e = bVar;
    }
}
