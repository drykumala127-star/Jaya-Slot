package Z;

import java.util.ArrayDeque;
import java.util.Queue;

final class E {
    private final Object a = new Object();
    private Queue b;
    private boolean c;

    E() {
    }

    public final void a(D d) {
        synchronized (this.a) {
            try {
                if (this.b == null) {
                    this.b = new ArrayDeque();
                }
                this.b.add(d);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void b(i iVar) {
        D d;
        synchronized (this.a) {
            if (this.b != null && !this.c) {
                this.c = true;
                while (true) {
                    synchronized (this.a) {
                        try {
                            d = (D) this.b.poll();
                            if (d == null) {
                                this.c = false;
                                return;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    d.a(iVar);
                }
            }
        }
    }
}
