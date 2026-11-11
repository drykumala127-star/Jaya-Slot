package s;

public final class g implements o.b {
    private final y0.a a;

    public g(y0.a aVar) {
        this.a = aVar;
    }

    public static t.f a(w.a aVar) {
        return (t.f) o.d.c(f.a(aVar), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static g b(y0.a aVar) {
        return new g(aVar);
    }

    @Override
    public t.f get() {
        return a((w.a) this.a.get());
    }
}
