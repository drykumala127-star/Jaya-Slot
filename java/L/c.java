package l;

import l.a;

final class c extends l.a {
    private final Integer a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String f;
    private final String g;
    private final String h;
    private final String i;
    private final String j;
    private final String k;
    private final String l;

    static final class b extends a.a {
        private Integer a;
        private String b;
        private String c;
        private String d;
        private String e;
        private String f;
        private String g;
        private String h;
        private String i;
        private String j;
        private String k;
        private String l;

        b() {
        }

        @Override
        public l.a a() {
            return new c(this.a, this.b, this.c, this.d, this.e, this.f, this.g, this.h, this.i, this.j, this.k, this.l, null);
        }

        @Override
        public a.a b(String str) {
            this.l = str;
            return this;
        }

        @Override
        public a.a c(String str) {
            this.j = str;
            return this;
        }

        @Override
        public a.a d(String str) {
            this.d = str;
            return this;
        }

        @Override
        public a.a e(String str) {
            this.h = str;
            return this;
        }

        @Override
        public a.a f(String str) {
            this.c = str;
            return this;
        }

        @Override
        public a.a g(String str) {
            this.i = str;
            return this;
        }

        @Override
        public a.a h(String str) {
            this.g = str;
            return this;
        }

        @Override
        public a.a i(String str) {
            this.k = str;
            return this;
        }

        @Override
        public a.a j(String str) {
            this.b = str;
            return this;
        }

        @Override
        public a.a k(String str) {
            this.f = str;
            return this;
        }

        @Override
        public a.a l(String str) {
            this.e = str;
            return this;
        }

        @Override
        public a.a m(Integer num) {
            this.a = num;
            return this;
        }
    }

    c(Integer num, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, a aVar) {
        this(num, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11);
    }

    @Override
    public String b() {
        return this.l;
    }

    @Override
    public String c() {
        return this.j;
    }

    @Override
    public String d() {
        return this.d;
    }

    @Override
    public String e() {
        return this.h;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof l.a)) {
            return false;
        }
        l.a aVar = (l.a) obj;
        Integer num = this.a;
        if (num != null ? num.equals(aVar.m()) : aVar.m() == null) {
            String str = this.b;
            if (str != null ? str.equals(aVar.j()) : aVar.j() == null) {
                String str2 = this.c;
                if (str2 != null ? str2.equals(aVar.f()) : aVar.f() == null) {
                    String str3 = this.d;
                    if (str3 != null ? str3.equals(aVar.d()) : aVar.d() == null) {
                        String str4 = this.e;
                        if (str4 != null ? str4.equals(aVar.l()) : aVar.l() == null) {
                            String str5 = this.f;
                            if (str5 != null ? str5.equals(aVar.k()) : aVar.k() == null) {
                                String str6 = this.g;
                                if (str6 != null ? str6.equals(aVar.h()) : aVar.h() == null) {
                                    String str7 = this.h;
                                    if (str7 != null ? str7.equals(aVar.e()) : aVar.e() == null) {
                                        String str8 = this.i;
                                        if (str8 != null ? str8.equals(aVar.g()) : aVar.g() == null) {
                                            String str9 = this.j;
                                            if (str9 != null ? str9.equals(aVar.c()) : aVar.c() == null) {
                                                String str10 = this.k;
                                                if (str10 != null ? str10.equals(aVar.i()) : aVar.i() == null) {
                                                    String str11 = this.l;
                                                    if (str11 == null) {
                                                        if (aVar.b() == null) {
                                                            return true;
                                                        }
                                                    } else if (str11.equals(aVar.b())) {
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String f() {
        return this.c;
    }

    @Override
    public String g() {
        return this.i;
    }

    @Override
    public String h() {
        return this.g;
    }

    public int hashCode() {
        Integer num = this.a;
        int iHashCode = ((num == null ? 0 : num.hashCode()) ^ 1000003) * 1000003;
        String str = this.b;
        int iHashCode2 = (iHashCode ^ (str == null ? 0 : str.hashCode())) * 1000003;
        String str2 = this.c;
        int iHashCode3 = (iHashCode2 ^ (str2 == null ? 0 : str2.hashCode())) * 1000003;
        String str3 = this.d;
        int iHashCode4 = (iHashCode3 ^ (str3 == null ? 0 : str3.hashCode())) * 1000003;
        String str4 = this.e;
        int iHashCode5 = (iHashCode4 ^ (str4 == null ? 0 : str4.hashCode())) * 1000003;
        String str5 = this.f;
        int iHashCode6 = (iHashCode5 ^ (str5 == null ? 0 : str5.hashCode())) * 1000003;
        String str6 = this.g;
        int iHashCode7 = (iHashCode6 ^ (str6 == null ? 0 : str6.hashCode())) * 1000003;
        String str7 = this.h;
        int iHashCode8 = (iHashCode7 ^ (str7 == null ? 0 : str7.hashCode())) * 1000003;
        String str8 = this.i;
        int iHashCode9 = (iHashCode8 ^ (str8 == null ? 0 : str8.hashCode())) * 1000003;
        String str9 = this.j;
        int iHashCode10 = (iHashCode9 ^ (str9 == null ? 0 : str9.hashCode())) * 1000003;
        String str10 = this.k;
        int iHashCode11 = (iHashCode10 ^ (str10 == null ? 0 : str10.hashCode())) * 1000003;
        String str11 = this.l;
        return iHashCode11 ^ (str11 != null ? str11.hashCode() : 0);
    }

    @Override
    public String i() {
        return this.k;
    }

    @Override
    public String j() {
        return this.b;
    }

    @Override
    public String k() {
        return this.f;
    }

    @Override
    public String l() {
        return this.e;
    }

    @Override
    public Integer m() {
        return this.a;
    }

    public String toString() {
        return "AndroidClientInfo{sdkVersion=" + this.a + ", model=" + this.b + ", hardware=" + this.c + ", device=" + this.d + ", product=" + this.e + ", osBuild=" + this.f + ", manufacturer=" + this.g + ", fingerprint=" + this.h + ", locale=" + this.i + ", country=" + this.j + ", mccMnc=" + this.k + ", applicationBuild=" + this.l + "}";
    }

    private c(Integer num, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11) {
        this.a = num;
        this.b = str;
        this.c = str2;
        this.d = str3;
        this.e = str4;
        this.f = str5;
        this.g = str6;
        this.h = str7;
        this.i = str8;
        this.j = str9;
        this.k = str10;
        this.l = str11;
    }
}
