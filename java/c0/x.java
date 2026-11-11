package c0;

public class x implements m0.b {
    private static final Object c = new Object();
    private volatile Object a = c;
    private volatile m0.b b;

    public x(m0.b bVar) {
        this.b = bVar;
    }

    @Override
    public Object get() {
        Object obj = this.a;
        Object obj2 = c;
        if (obj == obj2) {
            synchronized (this) {
                try {
                    obj = this.a;
                    if (obj == obj2) {
                        obj = this.b.get();
                        this.a = obj;
                        this.b = null;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return obj;
    }
}
