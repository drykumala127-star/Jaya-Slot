package Z;

final class p implements Runnable {
    final i i;
    final q j;

    p(q qVar, i iVar) {
        this.j = qVar;
        this.i = iVar;
    }

    @Override
    public final void run() {
        if (this.i.k()) {
            q.c(this.j).r();
            return;
        }
        try {
            q.c(this.j).q(q.b(this.j).a(this.i));
        } catch (g e) {
            if (e.getCause() instanceof Exception) {
                q.c(this.j).p((Exception) e.getCause());
            } else {
                q.c(this.j).p(e);
            }
        } catch (Exception e2) {
            q.c(this.j).p(e2);
        }
    }
}
