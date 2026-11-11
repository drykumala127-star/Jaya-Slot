package h0;

import android.util.Base64;
import android.util.JsonWriter;
import f0.f;
import f0.g;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

final class e implements f0.e, g {
    private e a = null;
    private boolean b = true;
    private final JsonWriter c;
    private final Map d;
    private final Map e;
    private final f0.d f;
    private final boolean g;

    e(Writer writer, Map map, Map map2, f0.d dVar, boolean z) {
        this.c = new JsonWriter(writer);
        this.d = map;
        this.e = map2;
        this.f = dVar;
        this.g = z;
    }

    private boolean o(Object obj) {
        return obj == null || obj.getClass().isArray() || (obj instanceof Collection) || (obj instanceof Date) || (obj instanceof Enum) || (obj instanceof Number);
    }

    private e r(String str, Object obj) throws IOException {
        t();
        this.c.name(str);
        if (obj != null) {
            return h(obj, false);
        }
        this.c.nullValue();
        return this;
    }

    private e s(String str, Object obj) throws IOException {
        if (obj == null) {
            return this;
        }
        t();
        this.c.name(str);
        return h(obj, false);
    }

    private void t() throws IOException {
        if (!this.b) {
            throw new IllegalStateException("Parent context used since this context was created. Cannot use this context anymore.");
        }
        e eVar = this.a;
        if (eVar != null) {
            eVar.t();
            this.a.b = false;
            this.a = null;
            this.c.endObject();
        }
    }

    @Override
    public f0.e a(f0.c cVar, int i) {
        return j(cVar.b(), i);
    }

    @Override
    public f0.e d(f0.c cVar, Object obj) {
        return l(cVar.b(), obj);
    }

    @Override
    public f0.e e(f0.c cVar, long j) {
        return k(cVar.b(), j);
    }

    public e f(int i) throws IOException {
        t();
        this.c.value(i);
        return this;
    }

    public e g(long j) throws IOException {
        t();
        this.c.value(j);
        return this;
    }

    e h(Object obj, boolean z) {
        int i = 0;
        if (z && o(obj)) {
            throw new f0.b(String.format("%s cannot be encoded inline", obj == null ? null : obj.getClass()));
        }
        if (obj == null) {
            this.c.nullValue();
            return this;
        }
        if (obj instanceof Number) {
            this.c.value((Number) obj);
            return this;
        }
        if (!obj.getClass().isArray()) {
            if (obj instanceof Collection) {
                this.c.beginArray();
                Iterator it = ((Collection) obj).iterator();
                while (it.hasNext()) {
                    h(it.next(), false);
                }
                this.c.endArray();
                return this;
            }
            if (obj instanceof Map) {
                this.c.beginObject();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    Object key = entry.getKey();
                    try {
                        l((String) key, entry.getValue());
                    } catch (ClassCastException e) {
                        throw new f0.b(String.format("Only String keys are currently supported in maps, got %s of type %s instead.", key, key.getClass()), e);
                    }
                }
                this.c.endObject();
                return this;
            }
            f0.d dVar = (f0.d) this.d.get(obj.getClass());
            if (dVar != null) {
                return q(dVar, obj, z);
            }
            f fVar = (f) this.e.get(obj.getClass());
            if (fVar != null) {
                fVar.a(obj, this);
                return this;
            }
            if (!(obj instanceof Enum)) {
                return q(this.f, obj, z);
            }
            b(((Enum) obj).name());
            return this;
        }
        if (obj instanceof byte[]) {
            return n((byte[]) obj);
        }
        this.c.beginArray();
        if (obj instanceof int[]) {
            int length = ((int[]) obj).length;
            while (i < length) {
                this.c.value(r7[i]);
                i++;
            }
        } else if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            int length2 = jArr.length;
            while (i < length2) {
                g(jArr[i]);
                i++;
            }
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            int length3 = dArr.length;
            while (i < length3) {
                this.c.value(dArr[i]);
                i++;
            }
        } else if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            int length4 = zArr.length;
            while (i < length4) {
                this.c.value(zArr[i]);
                i++;
            }
        } else if (obj instanceof Number[]) {
            for (Number number : (Number[]) obj) {
                h(number, false);
            }
        } else {
            for (Object obj2 : (Object[]) obj) {
                h(obj2, false);
            }
        }
        this.c.endArray();
        return this;
    }

    @Override
    public e b(String str) throws IOException {
        t();
        this.c.value(str);
        return this;
    }

    public e j(String str, int i) throws IOException {
        t();
        this.c.name(str);
        return f(i);
    }

    public e k(String str, long j) throws IOException {
        t();
        this.c.name(str);
        return g(j);
    }

    public e l(String str, Object obj) {
        return this.g ? s(str, obj) : r(str, obj);
    }

    @Override
    public e c(boolean z) throws IOException {
        t();
        this.c.value(z);
        return this;
    }

    public e n(byte[] bArr) throws IOException {
        t();
        if (bArr == null) {
            this.c.nullValue();
        } else {
            this.c.value(Base64.encodeToString(bArr, 2));
        }
        return this;
    }

    void p() {
        t();
        this.c.flush();
    }

    e q(f0.d dVar, Object obj, boolean z) throws IOException {
        if (!z) {
            this.c.beginObject();
        }
        dVar.a(obj, this);
        if (!z) {
            this.c.endObject();
        }
        return this;
    }
}
