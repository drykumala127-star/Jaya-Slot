package u;

import u.e;

final class a extends e {
    private final long b;
    private final int c;
    private final int d;
    private final long e;
    private final int f;

    static final class b extends e.a {
        private Long a;
        private Integer b;
        private Integer c;
        private Long d;
        private Integer e;

        b() {
        }

        @Override
        e a() {
            String str = "";
            if (this.a == null) {
                str = " maxStorageSizeInBytes";
            }
            if (this.b == null) {
                str = str + " loadBatchSize";
            }
            if (this.c == null) {
                str = str + " criticalSectionEnterTimeoutMs";
            }
            if (this.d == null) {
                str = str + " eventCleanUpAge";
            }
            if (this.e == null) {
                str = str + " maxBlobByteSizePerRow";
            }
            if (str.isEmpty()) {
                return new a(this.a.longValue(), this.b.intValue(), this.c.intValue(), this.d.longValue(), this.e.intValue(), null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        e.a b(int i) {
            this.c = Integer.valueOf(i);
            return this;
        }

        @Override
        e.a c(long j) {
            this.d = Long.valueOf(j);
            return this;
        }

        @Override
        e.a d(int i) {
            this.b = Integer.valueOf(i);
            return this;
        }

        @Override
        e.a e(int i) {
            this.e = Integer.valueOf(i);
            return this;
        }

        @Override
        e.a f(long j) {
            this.a = Long.valueOf(j);
            return this;
        }
    }

    a(long j, int i, int i2, long j2, int i3, a aVar) {
        this(j, i, i2, j2, i3);
    }

    @Override
    int b() {
        return this.d;
    }

    @Override
    long c() {
        return this.e;
    }

    @Override
    int d() {
        return this.c;
    }

    @Override
    int e() {
        return this.f;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof e)) {
            return false;
        }
        e eVar = (e) obj;
        return this.b == eVar.f() && this.c == eVar.d() && this.d == eVar.b() && this.e == eVar.c() && this.f == eVar.e();
    }

    @Override
    long f() {
        return this.b;
    }

    public int hashCode() {
        long j = this.b;
        int i = (((((((int) (j ^ (j >>> 32))) ^ 1000003) * 1000003) ^ this.c) * 1000003) ^ this.d) * 1000003;
        long j2 = this.e;
        return this.f ^ ((i ^ ((int) ((j2 >>> 32) ^ j2))) * 1000003);
    }

    public String toString() {
        return "EventStoreConfig{maxStorageSizeInBytes=" + this.b + ", loadBatchSize=" + this.c + ", criticalSectionEnterTimeoutMs=" + this.d + ", eventCleanUpAge=" + this.e + ", maxBlobByteSizePerRow=" + this.f + "}";
    }

    private a(long j, int i, int i2, long j2, int i3) {
        this.b = j;
        this.c = i;
        this.d = i2;
        this.e = j2;
        this.f = i3;
    }
}
