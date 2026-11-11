package p0;

import p0.f;

final class b extends f {
    private final String a;
    private final long b;
    private final f.b c;

    static final class b extends f.a {
        private String a;
        private Long b;
        private f.b c;

        b() {
        }

        @Override
        public f a() {
            String str = "";
            if (this.b == null) {
                str = " tokenExpirationTimestamp";
            }
            if (str.isEmpty()) {
                return new b(this.a, this.b.longValue(), this.c, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        public f.a b(f.b bVar) {
            this.c = bVar;
            return this;
        }

        @Override
        public f.a c(String str) {
            this.a = str;
            return this;
        }

        @Override
        public f.a d(long j) {
            this.b = Long.valueOf(j);
            return this;
        }
    }

    b(String str, long j, f.b bVar, a aVar) {
        this(str, j, bVar);
    }

    @Override
    public f.b b() {
        return this.c;
    }

    @Override
    public String c() {
        return this.a;
    }

    @Override
    public long d() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof f)) {
            return false;
        }
        f fVar = (f) obj;
        String str = this.a;
        if (str != null ? str.equals(fVar.c()) : fVar.c() == null) {
            if (this.b == fVar.d()) {
                f.b bVar = this.c;
                if (bVar == null) {
                    if (fVar.b() == null) {
                        return true;
                    }
                } else if (bVar.equals(fVar.b())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        String str = this.a;
        int iHashCode = str == null ? 0 : str.hashCode();
        long j = this.b;
        int i = (((iHashCode ^ 1000003) * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003;
        f.b bVar = this.c;
        return i ^ (bVar != null ? bVar.hashCode() : 0);
    }

    public String toString() {
        return "TokenResult{token=" + this.a + ", tokenExpirationTimestamp=" + this.b + ", responseCode=" + this.c + "}";
    }

    private b(String str, long j, f.b bVar) {
        this.a = str;
        this.b = j;
        this.c = bVar;
    }
}
