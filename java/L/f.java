package l;

import java.util.Arrays;
import l.l;

final class f extends l {
    private final long a;
    private final Integer b;
    private final long c;
    private final byte[] d;
    private final String e;
    private final long f;
    private final o g;

    static final class b extends l.a {
        private Long a;
        private Integer b;
        private Long c;
        private byte[] d;
        private String e;
        private Long f;
        private o g;

        b() {
        }

        @Override
        public l a() {
            String str = "";
            if (this.a == null) {
                str = " eventTimeMs";
            }
            if (this.c == null) {
                str = str + " eventUptimeMs";
            }
            if (this.f == null) {
                str = str + " timezoneOffsetSeconds";
            }
            if (str.isEmpty()) {
                return new f(this.a.longValue(), this.b, this.c.longValue(), this.d, this.e, this.f.longValue(), this.g, null);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override
        public l.a b(Integer num) {
            this.b = num;
            return this;
        }

        @Override
        public l.a c(long j) {
            this.a = Long.valueOf(j);
            return this;
        }

        @Override
        public l.a d(long j) {
            this.c = Long.valueOf(j);
            return this;
        }

        @Override
        public l.a e(o oVar) {
            this.g = oVar;
            return this;
        }

        @Override
        l.a f(byte[] bArr) {
            this.d = bArr;
            return this;
        }

        @Override
        l.a g(String str) {
            this.e = str;
            return this;
        }

        @Override
        public l.a h(long j) {
            this.f = Long.valueOf(j);
            return this;
        }
    }

    f(long j, Integer num, long j2, byte[] bArr, String str, long j3, o oVar, a aVar) {
        this(j, num, j2, bArr, str, j3, oVar);
    }

    @Override
    public Integer b() {
        return this.b;
    }

    @Override
    public long c() {
        return this.a;
    }

    @Override
    public long d() {
        return this.c;
    }

    @Override
    public o e() {
        return this.g;
    }

    public boolean equals(Object obj) {
        Integer num;
        String str;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof l)) {
            return false;
        }
        l lVar = (l) obj;
        if (this.a == lVar.c() && ((num = this.b) != null ? num.equals(lVar.b()) : lVar.b() == null) && this.c == lVar.d()) {
            if (Arrays.equals(this.d, lVar instanceof f ? ((f) lVar).d : lVar.f()) && ((str = this.e) != null ? str.equals(lVar.g()) : lVar.g() == null) && this.f == lVar.h()) {
                o oVar = this.g;
                if (oVar == null) {
                    if (lVar.e() == null) {
                        return true;
                    }
                } else if (oVar.equals(lVar.e())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public byte[] f() {
        return this.d;
    }

    @Override
    public String g() {
        return this.e;
    }

    @Override
    public long h() {
        return this.f;
    }

    public int hashCode() {
        long j = this.a;
        int i = (((int) (j ^ (j >>> 32))) ^ 1000003) * 1000003;
        Integer num = this.b;
        int iHashCode = num == null ? 0 : num.hashCode();
        long j2 = this.c;
        int iHashCode2 = (((((i ^ iHashCode) * 1000003) ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003) ^ Arrays.hashCode(this.d)) * 1000003;
        String str = this.e;
        int iHashCode3 = str == null ? 0 : str.hashCode();
        long j3 = this.f;
        int i2 = (((iHashCode2 ^ iHashCode3) * 1000003) ^ ((int) ((j3 >>> 32) ^ j3))) * 1000003;
        o oVar = this.g;
        return i2 ^ (oVar != null ? oVar.hashCode() : 0);
    }

    public String toString() {
        return "LogEvent{eventTimeMs=" + this.a + ", eventCode=" + this.b + ", eventUptimeMs=" + this.c + ", sourceExtension=" + Arrays.toString(this.d) + ", sourceExtensionJsonProto3=" + this.e + ", timezoneOffsetSeconds=" + this.f + ", networkConnectionInfo=" + this.g + "}";
    }

    private f(long j, Integer num, long j2, byte[] bArr, String str, long j3, o oVar) {
        this.a = j;
        this.b = num;
        this.c = j2;
        this.d = bArr;
        this.e = str;
        this.f = j3;
        this.g = oVar;
    }
}
