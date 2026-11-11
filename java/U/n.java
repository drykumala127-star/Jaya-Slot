package u;

public final class N implements o.b {
    private final y0.a a;
    private final y0.a b;
    private final y0.a c;
    private final y0.a d;
    private final y0.a e;

    public N(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5) {
        this.a = aVar;
        this.b = aVar2;
        this.c = aVar3;
        this.d = aVar4;
        this.e = aVar5;
    }

    public static N a(y0.a aVar, y0.a aVar2, y0.a aVar3, y0.a aVar4, y0.a aVar5) {
        return new N(aVar, aVar2, aVar3, aVar4, aVar5);
    }

    public static M c(w.a aVar, w.a aVar2, Object obj, Object obj2, y0.a aVar3) {
        return new M(aVar, aVar2, (e) obj, (U) obj2, aVar3);
    }

    @Override
    public M get() {
        return c((w.a) this.a.get(), (w.a) this.b.get(), this.c.get(), this.d.get(), this.e);
    }
}
