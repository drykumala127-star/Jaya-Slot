package c0;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class y implements m0.b {
    private volatile Set b = null;
    private volatile Set a = Collections.newSetFromMap(new ConcurrentHashMap());

    y(Collection collection) {
        this.a.addAll(collection);
    }

    static y b(Collection collection) {
        return new y((Set) collection);
    }

    private synchronized void d() {
        try {
            Iterator it = this.a.iterator();
            while (it.hasNext()) {
                this.b.add(((m0.b) it.next()).get());
            }
            this.a = null;
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized void a(m0.b bVar) {
        try {
            if (this.b == null) {
                this.a.add(bVar);
            } else {
                this.b.add(bVar.get());
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override
    public Set get() {
        if (this.b == null) {
            synchronized (this) {
                try {
                    if (this.b == null) {
                        this.b = Collections.newSetFromMap(new ConcurrentHashMap());
                        d();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return Collections.unmodifiableSet(this.b);
    }
}
