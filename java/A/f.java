package a;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class f {
    static Object[] l;
    static int m;
    static Object[] n;
    static int o;
    int[] i;
    Object[] j;
    int k;

    public f() {
        this.i = c.a;
        this.j = c.c;
        this.k = 0;
    }

    private void a(int i) {
        if (i == 8) {
            synchronized (a.class) {
                try {
                    Object[] objArr = n;
                    if (objArr != null) {
                        this.j = objArr;
                        n = (Object[]) objArr[0];
                        this.i = (int[]) objArr[1];
                        objArr[1] = null;
                        objArr[0] = null;
                        o--;
                        return;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } else if (i == 4) {
            synchronized (a.class) {
                try {
                    Object[] objArr2 = l;
                    if (objArr2 != null) {
                        this.j = objArr2;
                        l = (Object[]) objArr2[0];
                        this.i = (int[]) objArr2[1];
                        objArr2[1] = null;
                        objArr2[0] = null;
                        m--;
                        return;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        }
        this.i = new int[i];
        this.j = new Object[i << 1];
    }

    private static int b(int[] iArr, int i, int i2) {
        try {
            return c.a(iArr, i, i2);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new ConcurrentModificationException();
        }
    }

    private static void d(int[] iArr, Object[] objArr, int i) {
        if (iArr.length == 8) {
            synchronized (a.class) {
                try {
                    if (o < 10) {
                        objArr[0] = n;
                        objArr[1] = iArr;
                        for (int i2 = (i << 1) - 1; i2 >= 2; i2--) {
                            objArr[i2] = null;
                        }
                        n = objArr;
                        o++;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return;
        }
        if (iArr.length == 4) {
            synchronized (a.class) {
                try {
                    if (m < 10) {
                        objArr[0] = l;
                        objArr[1] = iArr;
                        for (int i3 = (i << 1) - 1; i3 >= 2; i3--) {
                            objArr[i3] = null;
                        }
                        l = objArr;
                        m++;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        }
    }

    public void c(int i) {
        int i2 = this.k;
        int[] iArr = this.i;
        if (iArr.length < i) {
            Object[] objArr = this.j;
            a(i);
            if (this.k > 0) {
                System.arraycopy(iArr, 0, this.i, 0, i2);
                System.arraycopy(objArr, 0, this.j, 0, i2 << 1);
            }
            d(iArr, objArr, i2);
        }
        if (this.k != i2) {
            throw new ConcurrentModificationException();
        }
    }

    public void clear() {
        int i = this.k;
        if (i > 0) {
            int[] iArr = this.i;
            Object[] objArr = this.j;
            this.i = c.a;
            this.j = c.c;
            this.k = 0;
            d(iArr, objArr, i);
        }
        if (this.k > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object obj) {
        return f(obj) >= 0;
    }

    public boolean containsValue(Object obj) {
        return h(obj) >= 0;
    }

    int e(Object obj, int i) {
        int i2 = this.k;
        if (i2 == 0) {
            return -1;
        }
        int iB = b(this.i, i2, i);
        if (iB < 0 || obj.equals(this.j[iB << 1])) {
            return iB;
        }
        int i3 = iB + 1;
        while (i3 < i2 && this.i[i3] == i) {
            if (obj.equals(this.j[i3 << 1])) {
                return i3;
            }
            i3++;
        }
        for (int i4 = iB - 1; i4 >= 0 && this.i[i4] == i; i4--) {
            if (obj.equals(this.j[i4 << 1])) {
                return i4;
            }
        }
        return ~i3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof f) {
            f fVar = (f) obj;
            if (size() != fVar.size()) {
                return false;
            }
            for (int i = 0; i < this.k; i++) {
                try {
                    Object objI = i(i);
                    Object objL = l(i);
                    Object obj2 = fVar.get(objI);
                    if (objL == null) {
                        if (obj2 != null || !fVar.containsKey(objI)) {
                            return false;
                        }
                    } else if (!objL.equals(obj2)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                    return false;
                }
            }
            return true;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (size() != map.size()) {
                return false;
            }
            for (int i2 = 0; i2 < this.k; i2++) {
                try {
                    Object objI2 = i(i2);
                    Object objL2 = l(i2);
                    Object obj3 = map.get(objI2);
                    if (objL2 == null) {
                        if (obj3 != null || !map.containsKey(objI2)) {
                            return false;
                        }
                    } else if (!objL2.equals(obj3)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused2) {
                }
            }
            return true;
        }
        return false;
    }

    public int f(Object obj) {
        return obj == null ? g() : e(obj, obj.hashCode());
    }

    int g() {
        int i = this.k;
        if (i == 0) {
            return -1;
        }
        int iB = b(this.i, i, 0);
        if (iB < 0 || this.j[iB << 1] == null) {
            return iB;
        }
        int i2 = iB + 1;
        while (i2 < i && this.i[i2] == 0) {
            if (this.j[i2 << 1] == null) {
                return i2;
            }
            i2++;
        }
        for (int i3 = iB - 1; i3 >= 0 && this.i[i3] == 0; i3--) {
            if (this.j[i3 << 1] == null) {
                return i3;
            }
        }
        return ~i2;
    }

    public Object get(Object obj) {
        int iF = f(obj);
        if (iF >= 0) {
            return this.j[(iF << 1) + 1];
        }
        return null;
    }

    int h(Object obj) {
        int i = this.k * 2;
        Object[] objArr = this.j;
        if (obj == null) {
            for (int i2 = 1; i2 < i; i2 += 2) {
                if (objArr[i2] == null) {
                    return i2 >> 1;
                }
            }
            return -1;
        }
        for (int i3 = 1; i3 < i; i3 += 2) {
            if (obj.equals(objArr[i3])) {
                return i3 >> 1;
            }
        }
        return -1;
    }

    public int hashCode() {
        int[] iArr = this.i;
        Object[] objArr = this.j;
        int i = this.k;
        int i2 = 1;
        int i3 = 0;
        int iHashCode = 0;
        while (i3 < i) {
            Object obj = objArr[i2];
            iHashCode += (obj == null ? 0 : obj.hashCode()) ^ iArr[i3];
            i3++;
            i2 += 2;
        }
        return iHashCode;
    }

    public Object i(int i) {
        return this.j[i << 1];
    }

    public boolean isEmpty() {
        return this.k <= 0;
    }

    public Object j(int i) {
        Object[] objArr = this.j;
        int i2 = i << 1;
        Object obj = objArr[i2 + 1];
        int i3 = this.k;
        int i4 = 0;
        if (i3 <= 1) {
            d(this.i, objArr, i3);
            this.i = c.a;
            this.j = c.c;
        } else {
            int i5 = i3 - 1;
            int[] iArr = this.i;
            if (iArr.length <= 8 || i3 >= iArr.length / 3) {
                if (i < i5) {
                    int i6 = i + 1;
                    int i7 = i5 - i;
                    System.arraycopy(iArr, i6, iArr, i, i7);
                    Object[] objArr2 = this.j;
                    System.arraycopy(objArr2, i6 << 1, objArr2, i2, i7 << 1);
                }
                Object[] objArr3 = this.j;
                int i8 = i5 << 1;
                objArr3[i8] = null;
                objArr3[i8 + 1] = null;
            } else {
                a(i3 > 8 ? i3 + (i3 >> 1) : 8);
                if (i3 != this.k) {
                    throw new ConcurrentModificationException();
                }
                if (i > 0) {
                    System.arraycopy(iArr, 0, this.i, 0, i);
                    System.arraycopy(objArr, 0, this.j, 0, i2);
                }
                if (i < i5) {
                    int i9 = i + 1;
                    int i10 = i5 - i;
                    System.arraycopy(iArr, i9, this.i, i, i10);
                    System.arraycopy(objArr, i9 << 1, this.j, i2, i10 << 1);
                }
            }
            i4 = i5;
        }
        if (i3 != this.k) {
            throw new ConcurrentModificationException();
        }
        this.k = i4;
        return obj;
    }

    public Object k(int i, Object obj) {
        int i2 = (i << 1) + 1;
        Object[] objArr = this.j;
        Object obj2 = objArr[i2];
        objArr[i2] = obj;
        return obj2;
    }

    public Object l(int i) {
        return this.j[(i << 1) + 1];
    }

    public Object put(Object obj, Object obj2) {
        int i;
        int iE;
        int i2 = this.k;
        if (obj == null) {
            iE = g();
            i = 0;
        } else {
            int iHashCode = obj.hashCode();
            i = iHashCode;
            iE = e(obj, iHashCode);
        }
        if (iE >= 0) {
            int i3 = (iE << 1) + 1;
            Object[] objArr = this.j;
            Object obj3 = objArr[i3];
            objArr[i3] = obj2;
            return obj3;
        }
        int i4 = ~iE;
        int[] iArr = this.i;
        if (i2 >= iArr.length) {
            int i5 = 8;
            if (i2 >= 8) {
                i5 = (i2 >> 1) + i2;
            } else if (i2 < 4) {
                i5 = 4;
            }
            Object[] objArr2 = this.j;
            a(i5);
            if (i2 != this.k) {
                throw new ConcurrentModificationException();
            }
            int[] iArr2 = this.i;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr2, 0, this.j, 0, objArr2.length);
            }
            d(iArr, objArr2, i2);
        }
        if (i4 < i2) {
            int[] iArr3 = this.i;
            int i6 = i4 + 1;
            System.arraycopy(iArr3, i4, iArr3, i6, i2 - i4);
            Object[] objArr3 = this.j;
            System.arraycopy(objArr3, i4 << 1, objArr3, i6 << 1, (this.k - i4) << 1);
        }
        int i7 = this.k;
        if (i2 == i7) {
            int[] iArr4 = this.i;
            if (i4 < iArr4.length) {
                iArr4[i4] = i;
                Object[] objArr4 = this.j;
                int i8 = i4 << 1;
                objArr4[i8] = obj;
                objArr4[i8 + 1] = obj2;
                this.k = i7 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    public Object remove(Object obj) {
        int iF = f(obj);
        if (iF >= 0) {
            return j(iF);
        }
        return null;
    }

    public int size() {
        return this.k;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.k * 28);
        sb.append('{');
        for (int i = 0; i < this.k; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Object objI = i(i);
            if (objI != this) {
                sb.append(objI);
            } else {
                sb.append("(this Map)");
            }
            sb.append('=');
            Object objL = l(i);
            if (objL != this) {
                sb.append(objL);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public f(int i) {
        if (i == 0) {
            this.i = c.a;
            this.j = c.c;
        } else {
            a(i);
        }
        this.k = 0;
    }
}
