package p;

public final class c {
    private static final c c = new a().a();
    private final long a;
    private final b b;

    public static final class a {
        private long a = 0;
        private b b = b.REASON_UNKNOWN;

        a() {
        }

        public c a() {
            return new c(this.a, this.b);
        }

        public a b(long j) {
            this.a = j;
            return this;
        }

        public a c(b bVar) {
            this.b = bVar;
            return this;
        }
    }

    public enum b implements i0.c {
        REASON_UNKNOWN(0),
        MESSAGE_TOO_OLD(1),
        CACHE_FULL(2),
        PAYLOAD_TOO_BIG(3),
        MAX_RETRIES_REACHED(4),
        INVALID_PAYLOD(5),
        SERVER_ERROR(6);

        private final int i;

        b(int i) {
            this.i = i;
        }

        @Override
        public int a() {
            return this.i;
        }
    }

    c(long j, b bVar) {
        this.a = j;
        this.b = bVar;
    }

    public static a c() {
        return new a();
    }

    public long a() {
        return this.a;
    }

    public b b() {
        return this.b;
    }
}
