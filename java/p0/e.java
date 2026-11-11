package p0;

import com.google.firebase.installations.i;
import java.util.concurrent.TimeUnit;

class e {
    private static final long d = TimeUnit.HOURS.toMillis(24);
    private static final long e = TimeUnit.MINUTES.toMillis(30);
    private final i a = i.c();
    private long b;
    private int c;

    e() {
    }

    private synchronized long a(int i) {
        if (c(i)) {
            return (long) Math.min(Math.pow(2.0d, this.c) + this.a.e(), e);
        }
        return d;
    }

    private static boolean c(int i) {
        return i == 429 || (i >= 500 && i < 600);
    }

    private static boolean d(int i) {
        return (i >= 200 && i < 300) || i == 401 || i == 404;
    }

    private synchronized void e() {
        this.c = 0;
    }

    public synchronized boolean b() {
        return this.c == 0 || this.a.a() > this.b;
    }

    public synchronized void f(int i) {
        if (d(i)) {
            e();
            return;
        }
        this.c++;
        this.b = this.a.a() + a(i);
    }
}
