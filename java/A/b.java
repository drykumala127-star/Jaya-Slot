package a;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class b implements Collection, Set {
    private static final int[] m = new int[0];
    private static final Object[] n = new Object[0];
    private static Object[] o;
    private static int p;
    private static Object[] q;
    private static int r;
    private int[] i;
    Object[] j;
    int k;
    private e l;

    class a extends e {
        a() {
        }

        @Override
        protected void a() {
            b.this.clear();
        }

        @Override
        protected Object b(int i, int i2) {
            return b.this.j[i];
        }

        @Override
        protected Map c() {
            throw new UnsupportedOperationException("not a map");
        }

        @Override
        protected int d() {
            return b.this.k;
        }

        @Override
        protected int e(Object obj) {
            return b.this.indexOf(obj);
        }

        @Override
        protected int f(Object obj) {
            return b.this.indexOf(obj);
        }

        @Override
        protected void g(Object obj, Object obj2) {
            b.this.add(obj);
        }

        @Override
        protected void h(int i) {
            b.this.p(i);
        }

        @Override
        protected Object i(int i, Object obj) {
            throw new UnsupportedOperationException("not a map");
        }
    }

    public b() {
        this(0);
    }

    private void d(int i) {
        if (i == 8) {
            synchronized (b.class) {
                try {
                    Object[] objArr = q;
                    if (objArr != null) {
                        this.j = objArr;
                        q = (Object[]) objArr[0];
                        this.i = (int[]) objArr[1];
                        objArr[1] = null;
                        objArr[0] = null;
                        r--;
                        return;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } else if (i == 4) {
            synchronized (b.class) {
                try {
                    Object[] objArr2 = o;
                    if (objArr2 != null) {
                        this.j = objArr2;
                        o = (Object[]) objArr2[0];
                        this.i = (int[]) objArr2[1];
                        objArr2[1] = null;
                        objArr2[0] = null;
                        p--;
                        return;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        }
        this.i = new int[i];
        this.j = new Object[i];
    }

    private static void h(int[] iArr, Object[] objArr, int i) {
        if (iArr.length == 8) {
            synchronized (b.class) {
                try {
                    if (r < 10) {
                        objArr[0] = q;
                        objArr[1] = iArr;
                        for (int i2 = i - 1; i2 >= 2; i2--) {
                            objArr[i2] = null;
                        }
                        q = objArr;
                        r++;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return;
        }
        if (iArr.length == 4) {
            synchronized (b.class) {
                try {
                    if (p < 10) {
                        objArr[0] = o;
                        objArr[1] = iArr;
                        for (int i3 = i - 1; i3 >= 2; i3--) {
                            objArr[i3] = null;
                        }
                        o = objArr;
                        p++;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        }
    }

    private e j() {
        if (this.l == null) {
            this.l = new a();
        }
        return this.l;
    }

    private int k(Object obj, int i) {
        int i2 = this.k;
        if (i2 == 0) {
            return -1;
        }
        int iA = c.a(this.i, i2, i);
        if (iA < 0 || obj.equals(this.j[iA])) {
            return iA;
        }
        int i3 = iA + 1;
        while (i3 < i2 && this.i[i3] == i) {
            if (obj.equals(this.j[i3])) {
                return i3;
            }
            i3++;
        }
        for (int i4 = iA - 1; i4 >= 0 && this.i[i4] == i; i4--) {
            if (obj.equals(this.j[i4])) {
                return i4;
            }
        }
        return ~i3;
    }

    private int l() {
        int i = this.k;
        if (i == 0) {
            return -1;
        }
        int iA = c.a(this.i, i, 0);
        if (iA < 0 || this.j[iA] == null) {
            return iA;
        }
        int i2 = iA + 1;
        while (i2 < i && this.i[i2] == 0) {
            if (this.j[i2] == null) {
                return i2;
            }
            i2++;
        }
        for (int i3 = iA - 1; i3 >= 0 && this.i[i3] == 0; i3--) {
            if (this.j[i3] == null) {
                return i3;
            }
        }
        return ~i2;
    }

    @Override
    public boolean add(Object obj) {
        int i;
        int iK;
        if (obj == null) {
            iK = l();
            i = 0;
        } else {
            int iHashCode = obj.hashCode();
            i = iHashCode;
            iK = k(obj, iHashCode);
        }
        if (iK >= 0) {
            return false;
        }
        int i2 = ~iK;
        int i3 = this.k;
        int[] iArr = this.i;
        if (i3 >= iArr.length) {
            int i4 = 8;
            if (i3 >= 8) {
                i4 = (i3 >> 1) + i3;
            } else if (i3 < 4) {
                i4 = 4;
            }
            Object[] objArr = this.j;
            d(i4);
            int[] iArr2 = this.i;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr, 0, this.j, 0, objArr.length);
            }
            h(iArr, objArr, this.k);
        }
        int i5 = this.k;
        if (i2 < i5) {
            int[] iArr3 = this.i;
            int i6 = i2 + 1;
            System.arraycopy(iArr3, i2, iArr3, i6, i5 - i2);
            Object[] objArr2 = this.j;
            System.arraycopy(objArr2, i2, objArr2, i6, this.k - i2);
        }
        this.i[i2] = i;
        this.j[i2] = obj;
        this.k++;
        return true;
    }

    @Override
    public boolean addAll(Collection collection) {
        g(this.k + collection.size());
        Iterator it = collection.iterator();
        boolean zAdd = false;
        while (it.hasNext()) {
            zAdd |= add(it.next());
        }
        return zAdd;
    }

    @Override
    public void clear() {
        int i = this.k;
        if (i != 0) {
            h(this.i, this.j, i);
            this.i = m;
            this.j = n;
            this.k = 0;
        }
    }

    @Override
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            if (size() != set.size()) {
                return false;
            }
            for (int i = 0; i < this.k; i++) {
                try {
                    if (!set.contains(r(i))) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                }
            }
            return true;
        }
        return false;
    }

    public void g(int i) {
        int[] iArr = this.i;
        if (iArr.length < i) {
            Object[] objArr = this.j;
            d(i);
            int i2 = this.k;
            if (i2 > 0) {
                System.arraycopy(iArr, 0, this.i, 0, i2);
                System.arraycopy(objArr, 0, this.j, 0, this.k);
            }
            h(iArr, objArr, this.k);
        }
    }

    @Override
    public int hashCode() {
        int[] iArr = this.i;
        int i = this.k;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            i2 += iArr[i3];
        }
        return i2;
    }

    public int indexOf(Object obj) {
        return obj == null ? l() : k(obj, obj.hashCode());
    }

    @Override
    public boolean isEmpty() {
        return this.k <= 0;
    }

    @Override
    public Iterator iterator() {
        return j().m().iterator();
    }

    public Object p(int i) {
        Object[] objArr = this.j;
        Object obj = objArr[i];
        int i2 = this.k;
        if (i2 <= 1) {
            h(this.i, objArr, i2);
            this.i = m;
            this.j = n;
            this.k = 0;
        } else {
            int[] iArr = this.i;
            if (iArr.length <= 8 || i2 >= iArr.length / 3) {
                int i3 = i2 - 1;
                this.k = i3;
                if (i < i3) {
                    int i4 = i + 1;
                    System.arraycopy(iArr, i4, iArr, i, i3 - i);
                    Object[] objArr2 = this.j;
                    System.arraycopy(objArr2, i4, objArr2, i, this.k - i);
                }
                this.j[this.k] = null;
            } else {
                d(i2 > 8 ? i2 + (i2 >> 1) : 8);
                this.k--;
                if (i > 0) {
                    System.arraycopy(iArr, 0, this.i, 0, i);
                    System.arraycopy(objArr, 0, this.j, 0, i);
                }
                int i5 = this.k;
                if (i < i5) {
                    int i6 = i + 1;
                    System.arraycopy(iArr, i6, this.i, i, i5 - i);
                    System.arraycopy(objArr, i6, this.j, i, this.k - i);
                }
            }
        }
        return obj;
    }

    public Object r(int i) {
        return this.j[i];
    }

    @Override
    public boolean remove(Object obj) {
        int iIndexOf = indexOf(obj);
        if (iIndexOf < 0) {
            return false;
        }
        p(iIndexOf);
        return true;
    }

    @Override
    public boolean removeAll(Collection collection) {
        Iterator it = collection.iterator();
        boolean zRemove = false;
        while (it.hasNext()) {
            zRemove |= remove(it.next());
        }
        return zRemove;
    }

    @Override
    public boolean retainAll(Collection collection) {
        boolean z = false;
        for (int i = this.k - 1; i >= 0; i--) {
            if (!collection.contains(this.j[i])) {
                p(i);
                z = true;
            }
        }
        return z;
    }

    @Override
    public int size() {
        return this.k;
    }

    @Override
    public Object[] toArray() {
        int i = this.k;
        Object[] objArr = new Object[i];
        System.arraycopy(this.j, 0, objArr, 0, i);
        return objArr;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.k * 14);
        sb.append('{');
        for (int i = 0; i < this.k; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Object objR = r(i);
            if (objR != this) {
                sb.append(objR);
            } else {
                sb.append("(this Set)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public b(int i) {
        if (i == 0) {
            this.i = m;
            this.j = n;
        } else {
            d(i);
        }
        this.k = 0;
    }

    @Override
    public Object[] toArray(Object[] objArr) {
        if (objArr.length < this.k) {
            objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), this.k);
        }
        System.arraycopy(this.j, 0, objArr, 0, this.k);
        int length = objArr.length;
        int i = this.k;
        if (length > i) {
            objArr[i] = null;
        }
        return objArr;
    }
}
