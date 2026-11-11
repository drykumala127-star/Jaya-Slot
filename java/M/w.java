package m;

public final class w implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;
    private final y0.a d;
    private final y0.a e;

    public w(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
        this.d = aVar4;
        this.e = aVar5;
    }

    public static w a(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5) {
        return new w(aVar, aVar2, aVar3, aVar4, aVar5);
    }

    public static u c(w.a aVar, w.a aVar2, s.e eVar, t.r rVar, t.v vVar) {
        return new u(aVar, aVar2, eVar, rVar, vVar);
    }

    @Override
    public u get() {
        return c((w.a) this.a.get(), (w.a) this.b.get(), (s.e) this.c.get(), (t.r) this.d.get(), (t.v) this.e.get());
    }
}
