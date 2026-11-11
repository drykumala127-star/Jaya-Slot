package t;

public final class t implements Runnable {
    public final v i;

    public t(v vVar) {
        this.i = vVar;
    }

    @Override
    public final void run() {
        v.b(this.i);
    }
}
