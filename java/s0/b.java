package s0;

import com.google.firebase.messaging.K;

public final class b {
    private static final b b = new a().a();
    private final s0.a a;

    public static final class a {
        private s0.a a = null;

        a() {
        }

        public b a() {
            return new b(this.a);
        }

        public a b(s0.a aVar) {
            this.a = aVar;
            return this;
        }
    }

    b(s0.a aVar) {
        this.a = aVar;
    }

    public static a b() {
        return new a();
    }

    public s0.a a() {
        return this.a;
    }

    public byte[] c() {
        return K.a(this);
    }
}
