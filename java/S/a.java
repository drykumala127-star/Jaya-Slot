package s;

import m.j;
import m.p;

public final class a implements Runnable {
    public final c i;
    public final p j;
    public final k.h k;
    public final j l;

    public a(c cVar, p pVar, k.h hVar, j jVar) {
        this.i = cVar;
        this.j = pVar;
        this.k = hVar;
        this.l = jVar;
    }

    @Override
    public final void run() {
        c.c(this.i, this.j, this.k, this.l);
    }
}
