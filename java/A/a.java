package a;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class a extends f implements Map {
    e p;

    class a extends e {
        a() {
        }

        @Override
        protected void a() {
            a.this.clear();
        }

        @Override
        protected Object b(int i, int i2) {
            return a.this.j[(i << 1) + i2];
        }

        @Override
        protected Map c() {
            return a.this;
        }

        @Override
        protected int d() {
            return a.this.k;
        }

        @Override
        protected int e(Object obj) {
            return a.this.f(obj);
        }

        @Override
        protected int f(Object obj) {
            return a.this.h(obj);
        }

        @Override
        protected void g(Object obj, Object obj2) {
            a.this.put(obj, obj2);
        }

        @Override
        protected void h(int i) {
            a.this.j(i);
        }

        @Override
        protected Object i(int i, Object obj) {
            return a.this.k(i, obj);
        }
    }

    public a() {
    }

    private e m() {
        if (this.p == null) {
            this.p = new a();
        }
        return this.p;
    }

    @Override
    public Set entrySet() {
        return m().l();
    }

    @Override
    public Set keySet() {
        return m().m();
    }

    @Override
    public void putAll(Map map) {
        c(this.k + map.size());
        for (Map.Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Collection values() {
        return m().n();
    }

    public a(int i) {
        super(i);
    }
}
