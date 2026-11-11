package m;

public final class a implements g0.a {
    public static final g0.a a = new a();

    private static final class a implements f0.d {
        static final a a = new a();
        private static final f0.c b = f0.c.a("window").b(i0.a.b().c(1).a()).a();
        private static final f0.c c = f0.c.a("logSourceMetrics").b(i0.a.b().c(2).a()).a();
        private static final f0.c d = f0.c.a("globalMetrics").b(i0.a.b().c(3).a()).a();
        private static final f0.c e = f0.c.a("appNamespace").b(i0.a.b().c(4).a()).a();

        private a() {
        }

        @Override
        public void a(p.a aVar, f0.e eVar) {
            eVar.d(b, aVar.d());
            eVar.d(c, aVar.c());
            eVar.d(d, aVar.b());
            eVar.d(e, aVar.a());
        }
    }

    private static final class b implements f0.d {
        static final b a = new b();
        private static final f0.c b = f0.c.a("storageMetrics").b(i0.a.b().c(1).a()).a();

        private b() {
        }

        @Override
        public void a(p.b bVar, f0.e eVar) {
            eVar.d(b, bVar.a());
        }
    }

    private static final class c implements f0.d {
        static final c a = new c();
        private static final f0.c b = f0.c.a("eventsDroppedCount").b(i0.a.b().c(1).a()).a();
        private static final f0.c c = f0.c.a("reason").b(i0.a.b().c(3).a()).a();

        private c() {
        }

        @Override
        public void a(p.c cVar, f0.e eVar) {
            eVar.e(b, cVar.a());
            eVar.d(c, cVar.b());
        }
    }

    private static final class d implements f0.d {
        static final d a = new d();
        private static final f0.c b = f0.c.a("logSource").b(i0.a.b().c(1).a()).a();
        private static final f0.c c = f0.c.a("logEventDropped").b(i0.a.b().c(2).a()).a();

        private d() {
        }

        @Override
        public void a(p.d dVar, f0.e eVar) {
            eVar.d(b, dVar.b());
            eVar.d(c, dVar.a());
        }
    }

    private static final class e implements f0.d {
        static final e a = new e();
        private static final f0.c b = f0.c.d("clientMetrics");

        private e() {
        }

        @Override
        public void a(Object obj, Object obj2) {
            m.b.a(obj);
            b(null, (f0.e) obj2);
        }

        public void b(m mVar, f0.e eVar) {
            throw null;
        }
    }

    private static final class f implements f0.d {
        static final f a = new f();
        private static final f0.c b = f0.c.a("currentCacheSizeBytes").b(i0.a.b().c(1).a()).a();
        private static final f0.c c = f0.c.a("maxCacheSizeBytes").b(i0.a.b().c(2).a()).a();

        private f() {
        }

        @Override
        public void a(p.e eVar, f0.e eVar2) {
            eVar2.e(b, eVar.a());
            eVar2.e(c, eVar.b());
        }
    }

    private static final class g implements f0.d {
        static final g a = new g();
        private static final f0.c b = f0.c.a("startMs").b(i0.a.b().c(1).a()).a();
        private static final f0.c c = f0.c.a("endMs").b(i0.a.b().c(2).a()).a();

        private g() {
        }

        @Override
        public void a(p.f fVar, f0.e eVar) {
            eVar.e(b, fVar.b());
            eVar.e(c, fVar.a());
        }
    }

    private a() {
    }

    @Override
    public void a(g0.b bVar) {
        bVar.a(m.class, e.a);
        bVar.a(p.a.class, a.a);
        bVar.a(p.f.class, g.a);
        bVar.a(p.d.class, d.a);
        bVar.a(p.c.class, c.a);
        bVar.a(p.b.class, b.a);
        bVar.a(p.e.class, f.a);
    }
}
