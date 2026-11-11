package F;

public final class o {
    private static o b;
    private static final p c = new p(0, false, false, 0, 0);
    private p a;

    private o() {
    }

    public static synchronized o b() {
        try {
            if (b == null) {
                b = new o();
            }
        } catch (Throwable th) {
            throw th;
        }
        return b;
    }

    public p a() {
        return this.a;
    }

    public final synchronized void c(p pVar) {
        try {
            if (pVar == null) {
                this.a = c;
                return;
            }
            p pVar2 = this.a;
            if (pVar2 == null || pVar2.k() < pVar.k()) {
                this.a = pVar;
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
