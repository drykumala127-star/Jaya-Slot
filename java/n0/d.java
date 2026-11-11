package n0;

public final class d implements Runnable {
    public final com.google.firebase.installations.c i;
    public final boolean j;

    public d(com.google.firebase.installations.c cVar, boolean z) {
        this.i = cVar;
        this.j = z;
    }

    @Override
    public final void run() {
        com.google.firebase.installations.c.c(this.i, this.j);
    }
}
