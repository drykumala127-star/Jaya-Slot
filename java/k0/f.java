package k0;

import java.util.concurrent.Callable;

public final class f implements Callable {
    public final g a;

    public f(g gVar) {
        this.a = gVar;
    }

    @Override
    public final Object call() {
        return g.g(this.a);
    }
}
