package l;

public final class b implements g0.a {
    public static final g0.a a = new b();

    private static final class a implements f0.d {
        static final a a = new a();
        private static final f0.c b = f0.c.d("sdkVersion");
        private static final f0.c c = f0.c.d("model");
        private static final f0.c d = f0.c.d("hardware");
        private static final f0.c e = f0.c.d("device");
        private static final f0.c f = f0.c.d("product");
        private static final f0.c g = f0.c.d("osBuild");
        private static final f0.c h = f0.c.d("manufacturer");
        private static final f0.c i = f0.c.d("fingerprint");
        private static final f0.c j = f0.c.d("locale");
        private static final f0.c k = f0.c.d("country");
        private static final f0.c l = f0.c.d("mccMnc");
        private static final f0.c m = f0.c.d("applicationBuild");

        private a() {
        }

        @Override
        public void a(l.a aVar, f0.e eVar) {
            eVar.d(b, aVar.m());
            eVar.d(c, aVar.j());
            eVar.d(d, aVar.f());
            eVar.d(e, aVar.d());
            eVar.d(f, aVar.l());
            eVar.d(g, aVar.k());
            eVar.d(h, aVar.h());
            eVar.d(i, aVar.e());
            eVar.d(j, aVar.g());
            eVar.d(k, aVar.c());
            eVar.d(l, aVar.i());
            eVar.d(m, aVar.b());
        }
    }

    private static final class b implements f0.d {
        static final b a = new b();
        private static final f0.c b = f0.c.d("logRequest");

        private b() {
        }

        @Override
        public void a(j jVar, f0.e eVar) {
            eVar.d(b, jVar.c());
        }
    }

    private static final class c implements f0.d {
        static final c a = new c();
        private static final f0.c b = f0.c.d("clientType");
        private static final f0.c c = f0.c.d("androidClientInfo");

        private c() {
        }

        @Override
        public void a(k kVar, f0.e eVar) {
            eVar.d(b, kVar.c());
            eVar.d(c, kVar.b());
        }
    }

    private static final class d implements f0.d {
        static final d a = new d();
        private static final f0.c b = f0.c.d("eventTimeMs");
        private static final f0.c c = f0.c.d("eventCode");
        private static final f0.c d = f0.c.d("eventUptimeMs");
        private static final f0.c e = f0.c.d("sourceExtension");
        private static final f0.c f = f0.c.d("sourceExtensionJsonProto3");
        private static final f0.c g = f0.c.d("timezoneOffsetSeconds");
        private static final f0.c h = f0.c.d("networkConnectionInfo");

        private d() {
        }

        @Override
        public void a(l lVar, f0.e eVar) {
            eVar.e(b, lVar.c());
            eVar.d(c, lVar.b());
            eVar.e(d, lVar.d());
            eVar.d(e, lVar.f());
            eVar.d(f, lVar.g());
            eVar.e(g, lVar.h());
            eVar.d(h, lVar.e());
        }
    }

    private static final class e implements f0.d {
        static final e a = new e();
        private static final f0.c b = f0.c.d("requestTimeMs");
        private static final f0.c c = f0.c.d("requestUptimeMs");
        private static final f0.c d = f0.c.d("clientInfo");
        private static final f0.c e = f0.c.d("logSource");
        private static final f0.c f = f0.c.d("logSourceName");
        private static final f0.c g = f0.c.d("logEvent");
        private static final f0.c h = f0.c.d("qosTier");

        private e() {
        }

        @Override
        public void a(m mVar, f0.e eVar) {
            eVar.e(b, mVar.g());
            eVar.e(c, mVar.h());
            eVar.d(d, mVar.b());
            eVar.d(e, mVar.d());
            eVar.d(f, mVar.e());
            eVar.d(g, mVar.c());
            eVar.d(h, mVar.f());
        }
    }

    private static final class f implements f0.d {
        static final f a = new f();
        private static final f0.c b = f0.c.d("networkType");
        private static final f0.c c = f0.c.d("mobileSubtype");

        private f() {
        }

        @Override
        public void a(o oVar, f0.e eVar) {
            eVar.d(b, oVar.c());
            eVar.d(c, oVar.b());
        }
    }

    private b() {
    }

    @Override
    public void a(g0.b bVar) {
        b bVar2 = b.a;
        bVar.a(j.class, bVar2);
        bVar.a(l.d.class, bVar2);
        e eVar = e.a;
        bVar.a(m.class, eVar);
        bVar.a(g.class, eVar);
        c cVar = c.a;
        bVar.a(k.class, cVar);
        bVar.a(l.e.class, cVar);
        a aVar = a.a;
        bVar.a(l.a.class, aVar);
        bVar.a(l.c.class, aVar);
        d dVar = d.a;
        bVar.a(l.class, dVar);
        bVar.a(l.f.class, dVar);
        f fVar = f.a;
        bVar.a(o.class, fVar);
        bVar.a(i.class, fVar);
    }
}
