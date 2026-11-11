package o;

public final class c implements b {
    private static final c b = new c(null);
    private final Object a;

    private c(Object obj) {
        this.a = obj;
    }

    public static b a(Object obj) {
        return new c(d.c(obj, "instance cannot be null"));
    }

    @Override
    public Object get() {
        return this.a;
    }
}
