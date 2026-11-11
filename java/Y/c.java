package y;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class c extends Thread {
    private final WeakReference i;
    private final long j;
    final CountDownLatch k = new CountDownLatch(1);
    boolean l = false;

    public c(a aVar, long j) {
        this.i = new WeakReference(aVar);
        this.j = j;
        start();
    }

    private final void a() {
        a aVar = (a) this.i.get();
        if (aVar != null) {
            aVar.c();
            this.l = true;
        }
    }

    @Override
    public final void run() {
        try {
            if (this.k.await(this.j, TimeUnit.MILLISECONDS)) {
                return;
            }
            a();
        } catch (InterruptedException unused) {
            a();
        }
    }
}
