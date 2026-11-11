package l;

import java.util.List;
import l.m;

final class g extends m {
    private final long a;
    private final long b;
    private final k c;
    private final Integer d;
    private final String e;
    private final List f;
    private final p g;

    static final class b extends m.a {
        private Long a;
        private Long b;
        private k c;
        private Integer d;
        private String e;
        private List f;
        private p g;

        b() {
        }

        @Override
        public m a() {
            String str = "";
            if (this.a == null) {
                str = " requestTimeMs";
            }
            if (this.b == null) {
                str = str + " requestUptimeMs";
            }
            if (str.isEmpty()) {
                return new g(this.a.longValue(), this.b.longValue(), this.c, this.d, this.e, this.f, this.g, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        public m.a b(k kVar) {
            this.c = kVar;
            return this;
        }

        @Override
        public m.a c(List list) {
            this.f = list;
            return this;
        }

        @Override
        m.a d(Integer num) {
            this.d = num;
            return this;
        }

        @Override
        m.a e(String str) {
            this.e = str;
            return this;
        }

        @Override
        public m.a f(p pVar) {
            this.g = pVar;
            return this;
        }

        @Override
        public m.a g(long j) {
            this.a = Long.valueOf(j);
            return this;
        }

        @Override
        public m.a h(long j) {
            this.b = Long.valueOf(j);
            return this;
        }
    }

    g(long j, long j2, k kVar, Integer num, String str, List list, p pVar, a aVar) {
        this(j, j2, kVar, num, str, list, pVar);
    }

    @Override
    public k b() {
        return this.c;
    }

    @Override
    public List c() {
        return this.f;
    }

    @Override
    public Integer d() {
        return this.d;
    }

    @Override
    public String e() {
        return this.e;
    }

    public boolean equals(Object obj) {
        k kVar;
        Integer num;
        String str;
        List list;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof m)) {
            return false;
        }
        m mVar = (m) obj;
        if (this.a == mVar.g() && this.b == mVar.h() && ((kVar = this.c) != null ? kVar.equals(mVar.b()) : mVar.b() == null) && ((num = this.d) != null ? num.equals(mVar.d()) : mVar.d() == null) && ((str = this.e) != null ? str.equals(mVar.e()) : mVar.e() == null) && ((list = this.f) != null ? list.equals(mVar.c()) : mVar.c() == null)) {
            p pVar = this.g;
            if (pVar == null) {
                if (mVar.f() == null) {
                    return true;
                }
            } else if (pVar.equals(mVar.f())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public p f() {
        return this.g;
    }

    @Override
    public long g() {
        return this.a;
    }

    @Override
    public long h() {
        return this.b;
    }

    public int hashCode() {
        long j = this.a;
        long j2 = this.b;
        int i = (((((int) (j ^ (j >>> 32))) ^ 1000003) * 1000003) ^ ((int) ((j2 >>> 32) ^ j2))) * 1000003;
        k kVar = this.c;
        int iHashCode = (i ^ (kVar == null ? 0 : kVar.hashCode())) * 1000003;
        Integer num = this.d;
        int iHashCode2 = (iHashCode ^ (num == null ? 0 : num.hashCode())) * 1000003;
        String str = this.e;
        int iHashCode3 = (iHashCode2 ^ (str == null ? 0 : str.hashCode())) * 1000003;
        List list = this.f;
        int iHashCode4 = (iHashCode3 ^ (list == null ? 0 : list.hashCode())) * 1000003;
        p pVar = this.g;
        return iHashCode4 ^ (pVar != null ? pVar.hashCode() : 0);
    }

    public String toString() {
        return "LogRequest{requestTimeMs=" + this.a + ", requestUptimeMs=" + this.b + ", clientInfo=" + this.c + ", logSource=" + this.d + ", logSourceName=" + this.e + ", logEvents=" + this.f + ", qosTier=" + this.g + "}";
    }

    private g(long j, long j2, k kVar, Integer num, String str, List list, p pVar) {
        this.a = j;
        this.b = j2;
        this.c = kVar;
        this.d = num;
        this.e = str;
        this.f = list;
        this.g = pVar;
    }
}
