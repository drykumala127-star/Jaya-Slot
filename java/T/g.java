package t;

public final class g implements Runnable {
    public final r i;
    public final m.p j;
    public final int k;
    public final Runnable l;

    public g(r rVar, m.p pVar, int i, Runnable runnable) {
        this.i = rVar;
        this.j = pVar;
        this.k = i;
        this.l = runnable;
    }

    @Override
    public final void run() {
        r.i(this.i, this.j, this.k, this.l);
    }
}
