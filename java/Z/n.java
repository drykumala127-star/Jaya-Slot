package Z;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class n implements o {
    private final CountDownLatch a = new CountDownLatch(1);

    n(m mVar) {
    }

    public final void a() throws InterruptedException {
        this.a.await();
    }

    @Override
    public final void b() {
        this.a.countDown();
    }

    @Override
    public final void c(Exception exc) {
        this.a.countDown();
    }

    @Override
    public final void d(Object obj) {
        this.a.countDown();
    }

    public final boolean e(long j, TimeUnit timeUnit) {
        return this.a.await(j, timeUnit);
    }
}
