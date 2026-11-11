package o;

public final class a implements y0.a {
    private static final Object c = new Object();
    private volatile y0.a a;
    private volatile Object b = c;

    private a(y0.a aVar) {
        this.a = aVar;
    }

    public static y0.a a(y0.a aVar) {
        d.b(aVar);
        return aVar instanceof a ? aVar : new a(aVar);
    }

    public static Object b(Object obj, Object obj2) {
        if (obj == c || obj == obj2) {
            return obj2;
        }
        throw new IllegalStateException("Scoped provider was invoked recursively returning different results: " + obj + " & " + obj2 + ". This is likely due to a circular dependency.");
    }

    @Override
    public Object get() {
        Object obj = this.b;
        Object obj2 = c;
        if (obj == obj2) {
            synchronized (this) {
                try {
                    obj = this.b;
                    if (obj == obj2) {
                        obj = this.a.get();
                        this.b = b(this.b, obj);
                        this.a = null;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return obj;
    }
}
