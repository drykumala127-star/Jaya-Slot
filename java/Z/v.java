package Z;

final class v implements Runnable {
    final i i;
    final w j;

    v(w wVar, i iVar) {
        this.j = wVar;
        this.i = iVar;
    }

    @Override
    public final void run() {
        synchronized (w.c(this.j)) {
            try {
                w wVar = this.j;
                if (w.b(wVar) != null) {
                    w.b(wVar).a(this.i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
