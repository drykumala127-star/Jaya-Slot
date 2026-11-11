package m;

import java.util.Map;
import m.j;

final class c extends j {
    private final String a;
    private final Integer b;
    private final i c;
    private final long d;
    private final long e;
    private final Map f;

    static final class b extends j.a {
        private String a;
        private Integer b;
        private i c;
        private Long d;
        private Long e;
        private Map f;

        b() {
        }

        @Override
        public j d() {
            String str = "";
            if (this.a == null) {
                str = " transportName";
            }
            if (this.c == null) {
                str = str + " encodedPayload";
            }
            if (this.d == null) {
                str = str + " eventMillis";
            }
            if (this.e == null) {
                str = str + " uptimeMillis";
            }
            if (this.f == null) {
                str = str + " autoMetadata";
            }
            if (str.isEmpty()) {
                return new c(this.a, this.b, this.c, this.d.longValue(), this.e.longValue(), this.f, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        protected Map e() {
            Map map = this.f;
            if (map != null) {
                return map;
            }
            throw new IllegalStateException("Property \"autoMetadata\" has not been set");
        }

        @Override
        protected j.a f(Map map) {
            if (map == null) {
                throw new NullPointerException("Null autoMetadata");
            }
            this.f = map;
            return this;
        }

        @Override
        public j.a g(Integer num) {
            this.b = num;
            return this;
        }

        @Override
        public j.a h(i iVar) {
            if (iVar == null) {
                throw new NullPointerException("Null encodedPayload");
            }
            this.c = iVar;
            return this;
        }

        @Override
        public j.a i(long j) {
            this.d = Long.valueOf(j);
            return this;
        }

        @Override
        public j.a j(String str) {
            if (str == null) {
                throw new NullPointerException("Null transportName");
            }
            this.a = str;
            return this;
        }

        @Override
        public j.a k(long j) {
            this.e = Long.valueOf(j);
            return this;
        }
    }

    c(String str, Integer num, i iVar, long j, long j2, Map map, a aVar) {
        this(str, num, iVar, j, j2, map);
    }

    @Override
    protected Map c() {
        return this.f;
    }

    @Override
    public Integer d() {
        return this.b;
    }

    @Override
    public i e() {
        return this.c;
    }

    public boolean equals(Object obj) {
        Integer num;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof j)) {
            return false;
        }
        j jVar = (j) obj;
        return this.a.equals(jVar.j()) && ((num = this.b) != null ? num.equals(jVar.d()) : jVar.d() == null) && this.c.equals(jVar.e()) && this.d == jVar.f() && this.e == jVar.k() && this.f.equals(jVar.c());
    }

    @Override
    public long f() {
        return this.d;
    }

    public int hashCode() {
        int iHashCode = (this.a.hashCode() ^ 1000003) * 1000003;
        Integer num = this.b;
        int iHashCode2 = (((iHashCode ^ (num == null ? 0 : num.hashCode())) * 1000003) ^ this.c.hashCode()) * 1000003;
        long j = this.d;
        int i = (iHashCode2 ^ ((int) (j ^ (j >>> 32)))) * 1000003;
        long j2 = this.e;
        return ((i ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003) ^ this.f.hashCode();
    }

    @Override
    public String j() {
        return this.a;
    }

    @Override
    public long k() {
        return this.e;
    }

    public String toString() {
        return "EventInternal{transportName=" + this.a + ", code=" + this.b + ", encodedPayload=" + this.c + ", eventMillis=" + this.d + ", uptimeMillis=" + this.e + ", autoMetadata=" + this.f + "}";
    }

    private c(String str, Integer num, i iVar, long j, long j2, Map map) {
        this.a = str;
        this.b = num;
        this.c = iVar;
        this.d = j;
        this.e = j2;
        this.f = map;
    }
}
