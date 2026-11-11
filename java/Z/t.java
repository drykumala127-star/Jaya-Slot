package Z;

final class t implements Runnable {
    final u i;

    t(u uVar) {
        this.i = uVar;
    }

    @Override
    public final void run() {
        synchronized (u.c(this.i)) {
            try {
                u uVar = this.i;
                if (u.b(uVar) != null) {
                    u.b(uVar).b();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
