package c0;

import java.util.Map;

public final class u implements Runnable {
    public final Map.Entry i;

    public u(Map.Entry entry, j0.a aVar) {
        this.i = entry;
    }

    @Override
    public final void run() {
        v.c(this.i, null);
    }
}
