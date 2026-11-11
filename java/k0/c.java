package k0;

import java.util.concurrent.ThreadFactory;

public final class c implements ThreadFactory {
    @Override
    public final Thread newThread(Runnable runnable) {
        return g.f(runnable);
    }
}
