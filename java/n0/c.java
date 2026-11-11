package n0;

public final class c implements Runnable {
    public final com.google.firebase.installations.c i;
    public final boolean j;

    public c(com.google.firebase.installations.c cVar, boolean z) {
        this.i = cVar;
        this.j = z;
    }

    @Override
    public final void run() {
        com.google.firebase.installations.c.d(this.i, this.j);
    }
}
