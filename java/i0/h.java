package i0;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class h {
    private final Map a;
    private final Map b;
    private final f0.d c;

    public static final class a implements g0.b {
        private static final f0.d d = new g();
        private final Map a = new HashMap();
        private final Map b = new HashMap();
        private f0.d c = d;

        public static void b(Object obj, f0.e eVar) {
            e(obj, eVar);
        }

        private static void e(Object obj, f0.e eVar) {
            throw new f0.b("Couldn't find encoder for type " + obj.getClass().getCanonicalName());
        }

        public h c() {
            return new h(new HashMap(this.a), new HashMap(this.b), this.c);
        }

        public a d(g0.a aVar) {
            aVar.a(this);
            return this;
        }

        @Override
        public a a(Class cls, f0.d dVar) {
            this.a.put(cls, dVar);
            this.b.remove(cls);
            return this;
        }
    }

    h(Map map, Map map2, f0.d dVar) {
        this.a = map;
        this.b = map2;
        this.c = dVar;
    }

    public static a a() {
        return new a();
    }

    public void b(Object obj, OutputStream outputStream) {
        new f(outputStream, this.a, this.b, this.c).q(obj);
    }

    public byte[] c(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            b(obj, byteArrayOutputStream);
        } catch (IOException unused) {
        }
        return byteArrayOutputStream.toByteArray();
    }
}
