package i0;

import i0.d;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

final class f implements f0.e {
    private static final Charset f = Charset.forName("UTF-8");
    private static final f0.c g = f0.c.a("key").b(i0.a.b().c(1).a()).a();
    private static final f0.c h = f0.c.a("value").b(i0.a.b().c(2).a()).a();
    private static final f0.d i = new e();
    private OutputStream a;
    private final Map b;
    private final Map c;
    private final f0.d d;
    private final i e = new i(this);

    static class a {
        static final int[] a;

        static {
            int[] iArr = new int[d.a.values().length];
            a = iArr;
            try {
                iArr[d.a.DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[d.a.SIGNED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[d.a.FIXED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    f(OutputStream outputStream, Map map, Map map2, f0.d dVar) {
        this.a = outputStream;
        this.b = map;
        this.c = map2;
        this.d = dVar;
    }

    public static void b(Map.Entry entry, f0.e eVar) {
        t(entry, eVar);
    }

    private static ByteBuffer m(int i2) {
        return ByteBuffer.allocate(i2).order(ByteOrder.LITTLE_ENDIAN);
    }

    private long n(f0.d dVar, Object obj) throws IOException {
        b bVar = new b();
        try {
            OutputStream outputStream = this.a;
            this.a = bVar;
            try {
                dVar.a(obj, this);
                this.a = outputStream;
                long jA = bVar.a();
                bVar.close();
                return jA;
            } catch (Throwable th) {
                this.a = outputStream;
                throw th;
            }
        } catch (Throwable th2) {
            try {
                bVar.close();
            } catch (Throwable th3) {
                th2.addSuppressed(th3);
            }
            throw th2;
        }
    }

    private f o(f0.d dVar, f0.c cVar, Object obj, boolean z) throws IOException {
        long jN = n(dVar, obj);
        if (z && jN == 0) {
            return this;
        }
        u((s(cVar) << 3) | 2);
        v(jN);
        dVar.a(obj, this);
        return this;
    }

    private f p(f0.f fVar, f0.c cVar, Object obj, boolean z) {
        this.e.d(cVar, z);
        fVar.a(obj, this.e);
        return this;
    }

    private static d r(f0.c cVar) {
        d dVar = (d) cVar.c(d.class);
        if (dVar != null) {
            return dVar;
        }
        throw new f0.b("Field has no @Protobuf config");
    }

    private static int s(f0.c cVar) {
        d dVar = (d) cVar.c(d.class);
        if (dVar != null) {
            return dVar.tag();
        }
        throw new f0.b("Field has no @Protobuf config");
    }

    private static void t(Map.Entry entry, f0.e eVar) {
        eVar.d(g, entry.getKey());
        eVar.d(h, entry.getValue());
    }

    private void u(int i2) throws IOException {
        while ((i2 & (-128)) != 0) {
            this.a.write((i2 & 127) | 128);
            i2 >>>= 7;
        }
        this.a.write(i2 & 127);
    }

    private void v(long j) throws IOException {
        while (((-128) & j) != 0) {
            this.a.write((((int) j) & 127) | 128);
            j >>>= 7;
        }
        this.a.write(((int) j) & 127);
    }

    f0.e c(f0.c cVar, double d, boolean z) throws IOException {
        if (z && d == 0.0d) {
            return this;
        }
        u((s(cVar) << 3) | 1);
        this.a.write(m(8).putDouble(d).array());
        return this;
    }

    @Override
    public f0.e d(f0.c cVar, Object obj) {
        return g(cVar, obj, true);
    }

    f0.e f(f0.c cVar, float f2, boolean z) throws IOException {
        if (z && f2 == 0.0f) {
            return this;
        }
        u((s(cVar) << 3) | 5);
        this.a.write(m(4).putFloat(f2).array());
        return this;
    }

    f0.e g(f0.c cVar, Object obj, boolean z) {
        if (obj == null) {
            return this;
        }
        if (obj instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) obj;
            if (z && charSequence.length() == 0) {
                return this;
            }
            u((s(cVar) << 3) | 2);
            byte[] bytes = charSequence.toString().getBytes(f);
            u(bytes.length);
            this.a.write(bytes);
            return this;
        }
        if (obj instanceof Collection) {
            Iterator it = ((Collection) obj).iterator();
            while (it.hasNext()) {
                g(cVar, it.next(), false);
            }
            return this;
        }
        if (obj instanceof Map) {
            Iterator it2 = ((Map) obj).entrySet().iterator();
            while (it2.hasNext()) {
                o(i, cVar, (Map.Entry) it2.next(), false);
            }
            return this;
        }
        if (obj instanceof Double) {
            return c(cVar, ((Double) obj).doubleValue(), z);
        }
        if (obj instanceof Float) {
            return f(cVar, ((Float) obj).floatValue(), z);
        }
        if (obj instanceof Number) {
            return k(cVar, ((Number) obj).longValue(), z);
        }
        if (obj instanceof Boolean) {
            return l(cVar, ((Boolean) obj).booleanValue(), z);
        }
        if (!(obj instanceof byte[])) {
            f0.d dVar = (f0.d) this.b.get(obj.getClass());
            if (dVar != null) {
                return o(dVar, cVar, obj, z);
            }
            f0.f fVar = (f0.f) this.c.get(obj.getClass());
            return fVar != null ? p(fVar, cVar, obj, z) : obj instanceof c ? a(cVar, ((c) obj).a()) : obj instanceof Enum ? a(cVar, ((Enum) obj).ordinal()) : o(this.d, cVar, obj, z);
        }
        byte[] bArr = (byte[]) obj;
        if (z && bArr.length == 0) {
            return this;
        }
        u((s(cVar) << 3) | 2);
        u(bArr.length);
        this.a.write(bArr);
        return this;
    }

    @Override
    public f a(f0.c cVar, int i2) {
        return i(cVar, i2, true);
    }

    f i(f0.c cVar, int i2, boolean z) throws IOException {
        if (z && i2 == 0) {
            return this;
        }
        d dVarR = r(cVar);
        int i3 = a.a[dVarR.intEncoding().ordinal()];
        if (i3 == 1) {
            u(dVarR.tag() << 3);
            u(i2);
        } else if (i3 == 2) {
            u(dVarR.tag() << 3);
            u((i2 << 1) ^ (i2 >> 31));
        } else if (i3 == 3) {
            u((dVarR.tag() << 3) | 5);
            this.a.write(m(4).putInt(i2).array());
        }
        return this;
    }

    @Override
    public f e(f0.c cVar, long j) {
        return k(cVar, j, true);
    }

    f k(f0.c cVar, long j, boolean z) throws IOException {
        if (z && j == 0) {
            return this;
        }
        d dVarR = r(cVar);
        int i2 = a.a[dVarR.intEncoding().ordinal()];
        if (i2 == 1) {
            u(dVarR.tag() << 3);
            v(j);
        } else if (i2 == 2) {
            u(dVarR.tag() << 3);
            v((j >> 63) ^ (j << 1));
        } else if (i2 == 3) {
            u((dVarR.tag() << 3) | 1);
            this.a.write(m(8).putLong(j).array());
        }
        return this;
    }

    f l(f0.c cVar, boolean z, boolean z2) {
        return i(cVar, z ? 1 : 0, z2);
    }

    f q(Object obj) {
        if (obj == null) {
            return this;
        }
        f0.d dVar = (f0.d) this.b.get(obj.getClass());
        if (dVar != null) {
            dVar.a(obj, this);
            return this;
        }
        throw new f0.b("No encoder for " + obj.getClass());
    }
}
