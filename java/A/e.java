package a;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

abstract class e {
    b a;
    c b;
    e c;

    final class a implements Iterator {
        final int a;
        int b;
        int c;
        boolean d = false;

        a(int i) {
            this.a = i;
            this.b = e.this.d();
        }

        @Override
        public boolean hasNext() {
            return this.c < this.b;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Object objB = e.this.b(this.c, this.a);
            this.c++;
            this.d = true;
            return objB;
        }

        @Override
        public void remove() {
            if (!this.d) {
                throw new IllegalStateException();
            }
            int i = this.c - 1;
            this.c = i;
            this.b--;
            this.d = false;
            e.this.h(i);
        }
    }

    final class b implements Set {
        b() {
        }

        @Override
        public boolean addAll(Collection collection) {
            int iD = e.this.d();
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                e.this.g(entry.getKey(), entry.getValue());
            }
            return iD != e.this.d();
        }

        @Override
        public void clear() {
            e.this.a();
        }

        @Override
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            int iE = e.this.e(entry.getKey());
            if (iE < 0) {
                return false;
            }
            return a.c.b(e.this.b(iE, 1), entry.getValue());
        }

        @Override
        public boolean containsAll(Collection collection) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean add(Map.Entry entry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object obj) {
            return e.k(this, obj);
        }

        @Override
        public int hashCode() {
            int iHashCode = 0;
            for (int iD = e.this.d() - 1; iD >= 0; iD--) {
                Object objB = e.this.b(iD, 0);
                Object objB2 = e.this.b(iD, 1);
                iHashCode += (objB == null ? 0 : objB.hashCode()) ^ (objB2 == null ? 0 : objB2.hashCode());
            }
            return iHashCode;
        }

        @Override
        public boolean isEmpty() {
            return e.this.d() == 0;
        }

        @Override
        public Iterator iterator() {
            return e.this.new d();
        }

        @Override
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return e.this.d();
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object[] toArray(Object[] objArr) {
            throw new UnsupportedOperationException();
        }
    }

    final class c implements Set {
        c() {
        }

        @Override
        public boolean add(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            e.this.a();
        }

        @Override
        public boolean contains(Object obj) {
            return e.this.e(obj) >= 0;
        }

        @Override
        public boolean containsAll(Collection collection) {
            return e.j(e.this.c(), collection);
        }

        @Override
        public boolean equals(Object obj) {
            return e.k(this, obj);
        }

        @Override
        public int hashCode() {
            int iHashCode = 0;
            for (int iD = e.this.d() - 1; iD >= 0; iD--) {
                Object objB = e.this.b(iD, 0);
                iHashCode += objB == null ? 0 : objB.hashCode();
            }
            return iHashCode;
        }

        @Override
        public boolean isEmpty() {
            return e.this.d() == 0;
        }

        @Override
        public Iterator iterator() {
            return e.this.new a(0);
        }

        @Override
        public boolean remove(Object obj) {
            int iE = e.this.e(obj);
            if (iE < 0) {
                return false;
            }
            e.this.h(iE);
            return true;
        }

        @Override
        public boolean removeAll(Collection collection) {
            return e.o(e.this.c(), collection);
        }

        @Override
        public boolean retainAll(Collection collection) {
            return e.p(e.this.c(), collection);
        }

        @Override
        public int size() {
            return e.this.d();
        }

        @Override
        public Object[] toArray() {
            return e.this.q(0);
        }

        @Override
        public Object[] toArray(Object[] objArr) {
            return e.this.r(objArr, 0);
        }
    }

    final class d implements Iterator, Map.Entry {
        int a;
        boolean c = false;
        int b = -1;

        d() {
            this.a = e.this.d() - 1;
        }

        @Override
        public Map.Entry next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.b++;
            this.c = true;
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (!this.c) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return a.c.b(entry.getKey(), e.this.b(this.b, 0)) && a.c.b(entry.getValue(), e.this.b(this.b, 1));
        }

        @Override
        public Object getKey() {
            if (this.c) {
                return e.this.b(this.b, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override
        public Object getValue() {
            if (this.c) {
                return e.this.b(this.b, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override
        public boolean hasNext() {
            return this.b < this.a;
        }

        @Override
        public int hashCode() {
            if (!this.c) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            Object objB = e.this.b(this.b, 0);
            Object objB2 = e.this.b(this.b, 1);
            return (objB == null ? 0 : objB.hashCode()) ^ (objB2 != null ? objB2.hashCode() : 0);
        }

        @Override
        public void remove() {
            if (!this.c) {
                throw new IllegalStateException();
            }
            e.this.h(this.b);
            this.b--;
            this.a--;
            this.c = false;
        }

        @Override
        public Object setValue(Object obj) {
            if (this.c) {
                return e.this.i(this.b, obj);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    final class e implements Collection {
        e() {
        }

        @Override
        public boolean add(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            e.this.a();
        }

        @Override
        public boolean contains(Object obj) {
            return e.this.f(obj) >= 0;
        }

        @Override
        public boolean containsAll(Collection collection) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean isEmpty() {
            return e.this.d() == 0;
        }

        @Override
        public Iterator iterator() {
            return e.this.new a(1);
        }

        @Override
        public boolean remove(Object obj) {
            int iF = e.this.f(obj);
            if (iF < 0) {
                return false;
            }
            e.this.h(iF);
            return true;
        }

        @Override
        public boolean removeAll(Collection collection) {
            int iD = e.this.d();
            int i = 0;
            boolean z = false;
            while (i < iD) {
                if (collection.contains(e.this.b(i, 1))) {
                    e.this.h(i);
                    i--;
                    iD--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        @Override
        public boolean retainAll(Collection collection) {
            int iD = e.this.d();
            int i = 0;
            boolean z = false;
            while (i < iD) {
                if (!collection.contains(e.this.b(i, 1))) {
                    e.this.h(i);
                    i--;
                    iD--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        @Override
        public int size() {
            return e.this.d();
        }

        @Override
        public Object[] toArray() {
            return e.this.q(1);
        }

        @Override
        public Object[] toArray(Object[] objArr) {
            return e.this.r(objArr, 1);
        }
    }

    e() {
    }

    public static boolean j(Map map, Collection collection) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (!map.containsKey(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static boolean k(Set set, Object obj) {
        if (set == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set2 = (Set) obj;
            try {
                return set.size() == set2.size() && set.containsAll(set2);
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    public static boolean o(Map map, Collection collection) {
        int size = map.size();
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            map.remove(it.next());
        }
        return size != map.size();
    }

    public static boolean p(Map map, Collection collection) {
        int size = map.size();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    protected abstract void a();

    protected abstract Object b(int i, int i2);

    protected abstract Map c();

    protected abstract int d();

    protected abstract int e(Object obj);

    protected abstract int f(Object obj);

    protected abstract void g(Object obj, Object obj2);

    protected abstract void h(int i);

    protected abstract Object i(int i, Object obj);

    public Set l() {
        if (this.a == null) {
            this.a = new b();
        }
        return this.a;
    }

    public Set m() {
        if (this.b == null) {
            this.b = new c();
        }
        return this.b;
    }

    public Collection n() {
        if (this.c == null) {
            this.c = new e();
        }
        return this.c;
    }

    public Object[] q(int i) {
        int iD = d();
        Object[] objArr = new Object[iD];
        for (int i2 = 0; i2 < iD; i2++) {
            objArr[i2] = b(i2, i);
        }
        return objArr;
    }

    public Object[] r(Object[] objArr, int i) {
        int iD = d();
        if (objArr.length < iD) {
            objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), iD);
        }
        for (int i2 = 0; i2 < iD; i2++) {
            objArr[i2] = b(i2, i);
        }
        if (objArr.length > iD) {
            objArr[iD] = null;
        }
        return objArr;
    }
}
