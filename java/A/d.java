package a;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public abstract class d {
    private final LinkedHashMap a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;

    public d(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.c = i;
        this.a = new LinkedHashMap(0, 0.75f, true);
    }

    private int f(Object obj, Object obj2) {
        int iG = g(obj, obj2);
        if (iG >= 0) {
            return iG;
        }
        throw new IllegalStateException("Negative size: " + obj + "=" + obj2);
    }

    protected abstract Object a(Object obj);

    public final Object c(Object obj) {
        Object objPut;
        if (obj == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            try {
                Object obj2 = this.a.get(obj);
                if (obj2 != null) {
                    this.g++;
                    return obj2;
                }
                this.h++;
                Object objA = a(obj);
                if (objA == null) {
                    return null;
                }
                synchronized (this) {
                    try {
                        this.e++;
                        objPut = this.a.put(obj, objA);
                        if (objPut != null) {
                            this.a.put(obj, objPut);
                        } else {
                            this.b += f(obj, objA);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (objPut != null) {
                    b(false, obj, objA, objPut);
                    return objPut;
                }
                i(this.c);
                return objA;
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }

    public final Object d(Object obj, Object obj2) {
        Object objPut;
        if (obj == null || obj2 == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            try {
                this.d++;
                this.b += f(obj, obj2);
                objPut = this.a.put(obj, obj2);
                if (objPut != null) {
                    this.b -= f(obj, objPut);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (objPut != null) {
            b(false, obj, objPut, obj2);
        }
        i(this.c);
        return objPut;
    }

    public final Object e(Object obj) {
        Object objRemove;
        if (obj == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            try {
                objRemove = this.a.remove(obj);
                if (objRemove != null) {
                    this.b -= f(obj, objRemove);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (objRemove != null) {
            b(false, obj, objRemove, null);
        }
        return objRemove;
    }

    protected int g(Object obj, Object obj2) {
        return 1;
    }

    public final synchronized Map h() {
        return new LinkedHashMap(this.a);
    }

    public void i(int i) {
        Object key;
        Object value;
        while (true) {
            synchronized (this) {
                try {
                    if (this.b < 0 || (this.a.isEmpty() && this.b != 0)) {
                        break;
                    }
                    if (this.b > i && !this.a.isEmpty()) {
                        Map.Entry entry = (Map.Entry) this.a.entrySet().iterator().next();
                        key = entry.getKey();
                        value = entry.getValue();
                        this.a.remove(key);
                        this.b -= f(key, value);
                        this.f++;
                    }
                    return;
                } catch (Throwable th) {
                    throw th;
                }
            }
            b(true, key, value, null);
        }
        throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
    }

    public final synchronized String toString() {
        int i;
        int i2;
        try {
            i = this.g;
            i2 = this.h + i;
        } catch (Throwable th) {
            throw th;
        }
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.c), Integer.valueOf(this.g), Integer.valueOf(this.h), Integer.valueOf(i2 != 0 ? (i * 100) / i2 : 0));
    }

    protected void b(boolean z, Object obj, Object obj2, Object obj3) {
    }
}
