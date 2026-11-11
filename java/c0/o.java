package c0;

import android.util.Log;
import com.google.firebase.components.ComponentRegistrar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

public class o extends c0.a implements e0.a {
    private static final m0.b h = new k();
    private final Map a;
    private final Map b;
    private final Map c;
    private final List d;
    private final v e;
    private final AtomicReference f;
    private final j g;

    o(Executor executor, Iterable iterable, Collection collection, j jVar, a aVar) {
        this(executor, iterable, collection, jVar);
    }

    public static Object e(o oVar, d dVar) {
        return oVar.m(dVar);
    }

    public static void f(C c, m0.b bVar) {
        n(c, bVar);
    }

    public static void g(y yVar, m0.b bVar) {
        o(yVar, bVar);
    }

    public static b h(Executor executor) {
        return new b(executor);
    }

    private void i(List list) {
        ArrayList arrayList = new ArrayList();
        synchronized (this) {
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                try {
                    ComponentRegistrar componentRegistrar = (ComponentRegistrar) ((m0.b) it.next()).get();
                    if (componentRegistrar != null) {
                        list.addAll(this.g.a(componentRegistrar));
                        it.remove();
                    }
                } catch (w e) {
                    it.remove();
                    Log.w("ComponentDiscovery", "Invalid component registrar.", e);
                }
            }
            if (this.a.isEmpty()) {
                q.a(list);
            } else {
                ArrayList arrayList2 = new ArrayList(this.a.keySet());
                arrayList2.addAll(list);
                q.a(arrayList2);
            }
            Iterator it2 = list.iterator();
            while (it2.hasNext()) {
                d dVar = (d) it2.next();
                this.a.put(dVar, new x(new l(this, dVar)));
            }
            arrayList.addAll(r(list));
            arrayList.addAll(s());
            q();
        }
        Iterator it3 = arrayList.iterator();
        while (it3.hasNext()) {
            ((Runnable) it3.next()).run();
        }
        p();
    }

    private void j(Map map, boolean z) {
        for (Map.Entry entry : map.entrySet()) {
            d dVar = (d) entry.getKey();
            m0.b bVar = (m0.b) entry.getValue();
            if (dVar.l() || (dVar.m() && z)) {
                bVar.get();
            }
        }
        this.e.d();
    }

    private static List l(Iterable iterable) {
        ArrayList arrayList = new ArrayList();
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    private Object m(d dVar) {
        return dVar.f().a(new E(dVar, this));
    }

    private static void n(C c, m0.b bVar) {
        c.f(bVar);
    }

    private static void o(y yVar, m0.b bVar) {
        yVar.a(bVar);
    }

    private void p() {
        Boolean bool = (Boolean) this.f.get();
        if (bool != null) {
            j(this.a, bool.booleanValue());
        }
    }

    private void q() {
        for (d dVar : this.a.keySet()) {
            for (r rVar : dVar.e()) {
                if (rVar.f() && !this.c.containsKey(rVar.b())) {
                    this.c.put(rVar.b(), y.b(Collections.emptySet()));
                } else if (this.b.containsKey(rVar.b())) {
                    continue;
                } else {
                    if (rVar.e()) {
                        throw new z(String.format("Unsatisfied dependency for component %s: %s", dVar, rVar.b()));
                    }
                    if (!rVar.f()) {
                        this.b.put(rVar.b(), C.c());
                    }
                }
            }
        }
    }

    private List r(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            d dVar = (d) it.next();
            if (dVar.n()) {
                m0.b bVar = (m0.b) this.a.get(dVar);
                for (Class cls : dVar.h()) {
                    if (this.b.containsKey(cls)) {
                        arrayList.add(new m((C) ((m0.b) this.b.get(cls)), bVar));
                    } else {
                        this.b.put(cls, bVar);
                    }
                }
            }
        }
        return arrayList;
    }

    private List s() {
        ArrayList arrayList = new ArrayList();
        HashMap map = new HashMap();
        for (Map.Entry entry : this.a.entrySet()) {
            d dVar = (d) entry.getKey();
            if (!dVar.n()) {
                m0.b bVar = (m0.b) entry.getValue();
                for (Class cls : dVar.h()) {
                    if (!map.containsKey(cls)) {
                        map.put(cls, new HashSet());
                    }
                    ((Set) map.get(cls)).add(bVar);
                }
            }
        }
        for (Map.Entry entry2 : map.entrySet()) {
            if (this.c.containsKey(entry2.getKey())) {
                y yVar = (y) this.c.get(entry2.getKey());
                Iterator it = ((Set) entry2.getValue()).iterator();
                while (it.hasNext()) {
                    arrayList.add(new n(yVar, (m0.b) it.next()));
                }
            } else {
                this.c.put((Class) entry2.getKey(), y.b((Collection) entry2.getValue()));
            }
        }
        return arrayList;
    }

    @Override
    public Object a(Class cls) {
        return super.a(cls);
    }

    @Override
    public synchronized m0.b b(Class cls) {
        D.c(cls, "Null interface requested.");
        return (m0.b) this.b.get(cls);
    }

    @Override
    public Set c(Class cls) {
        return super.c(cls);
    }

    @Override
    public synchronized m0.b d(Class cls) {
        y yVar = (y) this.c.get(cls);
        if (yVar != null) {
            return yVar;
        }
        return h;
    }

    public void k(boolean z) {
        HashMap map;
        if (E.x.a(this.f, null, Boolean.valueOf(z))) {
            synchronized (this) {
                map = new HashMap(this.a);
            }
            j(map, z);
        }
    }

    private o(Executor executor, Iterable iterable, Collection collection, j jVar) {
        this.a = new HashMap();
        this.b = new HashMap();
        this.c = new HashMap();
        this.f = new AtomicReference();
        v vVar = new v(executor);
        this.e = vVar;
        this.g = jVar;
        ArrayList arrayList = new ArrayList();
        arrayList.add(d.q(vVar, v.class, j0.d.class, j0.c.class));
        arrayList.add(d.q(this, e0.a.class, new Class[0]));
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            d dVar = (d) it.next();
            if (dVar != null) {
                arrayList.add(dVar);
            }
        }
        this.d = l(iterable);
        i(arrayList);
    }

    public static final class b {
        private final Executor a;
        private final List b = new ArrayList();
        private final List c = new ArrayList();
        private j d = j.a;

        b(Executor executor) {
            this.a = executor;
        }

        public static ComponentRegistrar a(ComponentRegistrar componentRegistrar) {
            return f(componentRegistrar);
        }

        public b b(d dVar) {
            this.c.add(dVar);
            return this;
        }

        public b c(ComponentRegistrar componentRegistrar) {
            this.b.add(new p(componentRegistrar));
            return this;
        }

        public b d(Collection collection) {
            this.b.addAll(collection);
            return this;
        }

        public o e() {
            return new o(this.a, this.b, this.c, this.d, null);
        }

        public b g(j jVar) {
            this.d = jVar;
            return this;
        }

        private static ComponentRegistrar f(ComponentRegistrar componentRegistrar) {
            return componentRegistrar;
        }
    }
}
