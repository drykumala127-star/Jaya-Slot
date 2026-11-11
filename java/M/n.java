package m;

import java.util.concurrent.Executor;

class n implements Executor {
    private final Executor a;

    static class a implements Runnable {
        private final Runnable i;

        a(Runnable runnable) {
            this.i = runnable;
        }

        @Override
        public void run() {
            try {
                this.i.run();
            } catch (Exception e) {
                q.a.d("Executor", "Background execution failure.", e);
            }
        }
    }

    n(Executor executor) {
        this.a = executor;
    }

    @Override
    public void execute(Runnable runnable) {
        this.a.execute(new a(runnable));
    }
}
