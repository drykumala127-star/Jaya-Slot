package t;

import java.util.Set;
import t.f;

final class c extends f.b {
    private final long a;
    private final long b;
    private final Set c;

    static final class b extends f.b.a {
        private Long a;
        private Long b;
        private Set c;

        b() {
        }

        @Override
        public f.b a() {
            String str = "";
            if (this.a == null) {
                str = " delta";
            }
            if (this.b == null) {
                str = str + " maxAllowedDelay";
            }
            if (this.c == null) {
                str = str + " flags";
            }
            if (str.isEmpty()) {
                return new c(this.a.longValue(), this.b.longValue(), this.c, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        public f.b.a b(long j) {
            this.a = Long.valueOf(j);
            return this;
        }

        @Override
        public f.b.a c(Set set) {
            if (set == null) {
                throw new NullPointerException("Null flags");
            }
            this.c = set;
            return this;
        }

        @Override
        public f.b.a d(long j) {
            this.b = Long.valueOf(j);
            return this;
        }
    }

    c(long j, long j2, Set set, a aVar) {
        this(j, j2, set);
    }

    @Override
    long b() {
        return this.a;
    }

    @Override
    Set c() {
        return this.c;
    }

    @Override
    long d() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof f.b)) {
            return false;
        }
        f.b bVar = (f.b) obj;
        return this.a == bVar.b() && this.b == bVar.d() && this.c.equals(bVar.c());
    }

    public int hashCode() {
        long j = this.a;
        int i = (((int) (j ^ (j >>> 32))) ^ 1000003) * 1000003;
        long j2 = this.b;
        return this.c.hashCode() ^ ((i ^ ((int) ((j2 >>> 32) ^ j2))) * 1000003);
    }

    public String toString() {
        return "ConfigValue{delta=" + this.a + ", maxAllowedDelay=" + this.b + ", flags=" + this.c + "}";
    }

    private c(long j, long j2, Set set) {
        this.a = j;
        this.b = j2;
        this.c = set;
    }
}
