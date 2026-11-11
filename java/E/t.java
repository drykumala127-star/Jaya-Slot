package E;

final class t implements Runnable {
    final X.l i;
    final v j;

    t(v vVar, X.l lVar) {
        this.j = vVar;
        this.i = lVar;
    }

    @Override
    public final void run() {
        v.N(this.j, this.i);
    }
}
