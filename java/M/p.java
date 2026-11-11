package m;

import android.util.Base64;
import m.e;

public abstract class p {

    public static abstract class a {
        public abstract p a();

        public abstract a b(String str);

        public abstract a c(byte[] bArr);

        public abstract a d(k.d dVar);
    }

    public static a a() {
        return new e.b().d(k.d.DEFAULT);
    }

    public abstract String b();

    public abstract byte[] c();

    public abstract k.d d();

    public boolean e() {
        return c() != null;
    }

    public p f(k.d dVar) {
        return a().b(b()).d(dVar).c(c()).a();
    }

    public final String toString() {
        return String.format("TransportContext(%s, %s, %s)", b(), d(), c() == null ? "" : Base64.encodeToString(c(), 2));
    }
}
