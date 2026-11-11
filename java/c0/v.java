package c0;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

class v implements j0.d, j0.c {
    private final Map a = new HashMap();
    private Queue b = new ArrayDeque();
    private final Executor c;

    v(Executor executor) {
        this.c = executor;
    }

    public static void c(Map.Entry entry, j0.a aVar) {
        f(entry, aVar);
    }

    private synchronized Set e(j0.a aVar) {
        throw null;
    }

    private static void f(Map.Entry entry, j0.a aVar) {
        ((j0.b) entry.getKey()).a(aVar);
    }

    @Override
    public void a(Class cls, j0.b bVar) {
        b(cls, this.c, bVar);
    }

    @Override
    public synchronized void b(Class cls, Executor executor, j0.b bVar) {
        try {
            D.b(cls);
            D.b(bVar);
            D.b(executor);
            if (!this.a.containsKey(cls)) {
                this.a.put(cls, new ConcurrentHashMap());
            }
            ((ConcurrentHashMap) this.a.get(cls)).put(bVar, executor);
        } catch (Throwable th) {
            throw th;
        }
    }

    void d() {
        Queue queue;
        synchronized (this) {
            try {
                queue = this.b;
                if (queue != null) {
                    this.b = null;
                } else {
                    queue = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (queue != null) {
            Iterator it = queue.iterator();
            while (it.hasNext()) {
                m.b.a(it.next());
                g(null);
            }
        }
    }

    public void g(j0.a aVar) {
        D.b(aVar);
        synchronized (this) {
            try {
                Queue queue = this.b;
                if (queue != null) {
                    queue.add(aVar);
                    return;
                }
                for (Map.Entry entry : e(aVar)) {
                    ((Executor) entry.getValue()).execute(new u(entry, aVar));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
