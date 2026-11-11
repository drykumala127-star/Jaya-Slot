package Z;

final class z implements Runnable {
    final i i;
    final A j;

    z(A a, i iVar) {
        this.j = a;
        this.i = iVar;
    }

    @Override
    public final void run() {
        synchronized (A.c(this.j)) {
            try {
                A a = this.j;
                if (A.b(a) != null) {
                    A.b(a).d(this.i.i());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
