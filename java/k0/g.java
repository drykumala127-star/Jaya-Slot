package k0;

import android.content.Context;
import android.util.Base64OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;
import k0.k;
import org.json.JSONArray;
import org.json.JSONObject;

public class g implements j, k {
    private static final ThreadFactory f = new c();
    private final m0.b a;
    private final Context b;
    private final m0.b c;
    private final Set d;
    private final Executor e;

    private g(Context context, String str, Set set, m0.b bVar) {
        this(new d(context, str), set, new ThreadPoolExecutor(0, 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue(), f), bVar, context);
    }

    public static String c(g gVar) {
        return gVar.j();
    }

    public static r d(Context context, String str) {
        return k(context, str);
    }

    public static g e(c0.e eVar) {
        return i(eVar);
    }

    public static Thread f(Runnable runnable) {
        return m(runnable);
    }

    public static Void g(g gVar) {
        return gVar.l();
    }

    public static c0.d h() {
        return c0.d.d(g.class, j.class, k.class).b(c0.r.i(Context.class)).b(c0.r.i(a0.d.class)).b(c0.r.k(h.class)).b(c0.r.j(u0.i.class)).f(new b()).d();
    }

    private static g i(c0.e eVar) {
        return new g((Context) eVar.a(Context.class), ((a0.d) eVar.a(a0.d.class)).n(), eVar.c(h.class), eVar.b(u0.i.class));
    }

    private String j() {
        String string;
        synchronized (this) {
            try {
                r rVar = (r) this.a.get();
                List listC = rVar.c();
                rVar.b();
                JSONArray jSONArray = new JSONArray();
                for (int i = 0; i < listC.size(); i++) {
                    s sVar = (s) listC.get(i);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("agent", sVar.c());
                    jSONObject.put("dates", new JSONArray((Collection) sVar.b()));
                    jSONArray.put(jSONObject);
                }
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("heartbeats", jSONArray);
                jSONObject2.put("version", "2");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 11);
                try {
                    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(base64OutputStream);
                    try {
                        gZIPOutputStream.write(jSONObject2.toString().getBytes("UTF-8"));
                        gZIPOutputStream.close();
                        base64OutputStream.close();
                        string = byteArrayOutputStream.toString("UTF-8");
                    } catch (Throwable th) {
                        try {
                            gZIPOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    try {
                        base64OutputStream.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                    throw th3;
                }
            } catch (Throwable th5) {
                throw th5;
            }
        }
        return string;
    }

    private static r k(Context context, String str) {
        return new r(context, str);
    }

    private Void l() {
        synchronized (this) {
            ((r) this.a.get()).k(System.currentTimeMillis(), ((u0.i) this.c.get()).a());
        }
        return null;
    }

    private static Thread m(Runnable runnable) {
        return new Thread(runnable, "heartbeat-information-executor");
    }

    @Override
    public Z.i a() {
        return !f.m.a(this.b) ? Z.l.e("") : Z.l.c(this.e, new e(this));
    }

    @Override
    public synchronized k.a b(String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        r rVar = (r) this.a.get();
        if (!rVar.i(jCurrentTimeMillis)) {
            return k.a.j;
        }
        rVar.g();
        return k.a.l;
    }

    public Z.i n() {
        return this.d.size() <= 0 ? Z.l.e(null) : !f.m.a(this.b) ? Z.l.e(null) : Z.l.c(this.e, new f(this));
    }

    g(m0.b bVar, Set set, Executor executor, m0.b bVar2, Context context) {
        this.a = bVar;
        this.d = set;
        this.e = executor;
        this.c = bVar2;
        this.b = context;
    }
}
