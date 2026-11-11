package b0;

import java.util.concurrent.Executor;

public final class c implements Executor {
    public static final c a = new c();

    private c() {
    }

    @Override
    public final void execute(Runnable runnable) {
        runnable.run();
    }
}
