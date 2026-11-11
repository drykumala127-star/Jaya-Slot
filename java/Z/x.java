package Z;

final class x implements Runnable {
    final i i;
    final y j;

    x(y yVar, i iVar) {
        this.j = yVar;
        this.i = iVar;
    }

    @Override
    public final void run() {
        synchronized (y.c(this.j)) {
            try {
                y yVar = this.j;
                if (y.b(yVar) != null) {
                    y.b(yVar).c((Exception) F.n.i(this.i.h()));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
