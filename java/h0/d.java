package h0;

import f0.f;
import f0.g;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class d implements g0.b {
    private static final f0.d e = new h0.a();
    private static final f f = new h0.b();
    private static final f g = new c();
    private static final b h = new b(null);
    private final Map a = new HashMap();
    private final Map b = new HashMap();
    private f0.d c = e;
    private boolean d = false;

    class a implements f0.a {
        a() {
        }

        @Override
        public void a(Object obj, Writer writer) {
            e eVar = new e(writer, d.e(d.this), d.f(d.this), d.g(d.this), d.h(d.this));
            eVar.h(obj, false);
            eVar.p();
        }
    }

    private static final class b implements f {
        private static final DateFormat a;

        static {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            a = simpleDateFormat;
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        private b() {
        }

        @Override
        public void a(Date date, g gVar) {
            gVar.b(a.format(date));
        }

        b(a aVar) {
            this();
        }
    }

    public d() {
        p(String.class, f);
        p(Boolean.class, g);
        p(Date.class, h);
    }

    public static void b(Boolean bool, g gVar) {
        n(bool, gVar);
    }

    public static void c(Object obj, f0.e eVar) {
        l(obj, eVar);
    }

    public static void d(String str, g gVar) {
        m(str, gVar);
    }

    static Map e(d dVar) {
        return dVar.a;
    }

    static Map f(d dVar) {
        return dVar.b;
    }

    static f0.d g(d dVar) {
        return dVar.c;
    }

    static boolean h(d dVar) {
        return dVar.d;
    }

    private static void l(Object obj, f0.e eVar) {
        throw new f0.b("Couldn't find encoder for type " + obj.getClass().getCanonicalName());
    }

    private static void m(String str, g gVar) {
        gVar.b(str);
    }

    private static void n(Boolean bool, g gVar) {
        gVar.c(bool.booleanValue());
    }

    public f0.a i() {
        return new a();
    }

    public d j(g0.a aVar) {
        aVar.a(this);
        return this;
    }

    public d k(boolean z) {
        this.d = z;
        return this;
    }

    @Override
    public d a(Class cls, f0.d dVar) {
        this.a.put(cls, dVar);
        this.b.remove(cls);
        return this;
    }

    public d p(Class cls, f fVar) {
        this.b.put(cls, fVar);
        this.a.remove(cls);
        return this;
    }
}
