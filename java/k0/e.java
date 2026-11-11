package k0;

import java.util.concurrent.Callable;

public final class e implements Callable {
    public final g a;

    public e(g gVar) {
        this.a = gVar;
    }

    @Override
    public final Object call() {
        return g.c(this.a);
    }
}
