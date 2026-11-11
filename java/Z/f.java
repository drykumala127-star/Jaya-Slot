package Z;

import java.util.concurrent.Executor;

final class F implements Executor {
    F() {
    }

    @Override
    public final void execute(Runnable runnable) {
        runnable.run();
    }
}
