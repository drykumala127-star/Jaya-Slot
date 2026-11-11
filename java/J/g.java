package J;

import android.os.SystemClock;

public class g implements d {
    private static final g a = new g();

    private g() {
    }

    public static d d() {
        return a;
    }

    @Override
    public final long a() {
        return System.currentTimeMillis();
    }

    @Override
    public final long b() {
        return SystemClock.elapsedRealtime();
    }

    @Override
    public final long c() {
        return System.nanoTime();
    }
}
