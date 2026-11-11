package a0;

import F.n;
import J.l;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import c0.o;
import c0.x;
import com.google.android.gms.common.api.internal.a;
import com.google.firebase.FirebaseCommonRegistrar;
import com.google.firebase.components.ComponentDiscoveryService;
import f.m;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class d {
    private static final Object k = new Object();
    private static final Executor l = new d(null);
    static final Map m = new a.a();
    private final Context a;
    private final String b;
    private final k c;
    private final o d;
    private final x g;
    private final m0.b h;
    private final AtomicBoolean e = new AtomicBoolean(false);
    private final AtomicBoolean f = new AtomicBoolean();
    private final List i = new CopyOnWriteArrayList();
    private final List j = new CopyOnWriteArrayList();

    public interface b {
        void a(boolean z);
    }

    private static class c implements a.a {
        private static AtomicReference a = new AtomicReference();

        private c() {
        }

        static void b(Context context) {
            c(context);
        }

        private static void c(Context context) {
            if (J.k.a() && (context.getApplicationContext() instanceof Application)) {
                Application application = (Application) context.getApplicationContext();
                if (a.get() == null) {
                    c cVar = new c();
                    if (E.x.a(a, null, cVar)) {
                        com.google.android.gms.common.api.internal.a.c(application);
                        com.google.android.gms.common.api.internal.a.b().a(cVar);
                    }
                }
            }
        }

        public void a(boolean z) {
            synchronized (d.c()) {
                try {
                    for (d dVar : new ArrayList(d.m.values())) {
                        if (d.e(dVar).get()) {
                            d.f(dVar, z);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private static class d implements Executor {
        private static final Handler a = new Handler(Looper.getMainLooper());

        private d() {
        }

        @Override
        public void execute(Runnable runnable) {
            a.post(runnable);
        }

        d(a aVar) {
            this();
        }
    }

    private static class e extends BroadcastReceiver {
        private static AtomicReference b = new AtomicReference();
        private final Context a;

        public e(Context context) {
            this.a = context;
        }

        static void a(Context context) {
            b(context);
        }

        private static void b(Context context) {
            if (b.get() == null) {
                e eVar = new e(context);
                if (E.x.a(b, null, eVar)) {
                    context.registerReceiver(eVar, new IntentFilter("android.intent.action.USER_UNLOCKED"));
                }
            }
        }

        public void c() {
            this.a.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (d.c()) {
                try {
                    Iterator it = d.m.values().iterator();
                    while (it.hasNext()) {
                        d.d((d) it.next());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            c();
        }
    }

    protected d(Context context, String str, k kVar) {
        this.a = (Context) n.i(context);
        this.b = n.e(str);
        this.c = (k) n.i(kVar);
        v0.c.b("Firebase");
        v0.c.b("ComponentDiscovery");
        List listB = c0.g.c(context, ComponentDiscoveryService.class).b();
        v0.c.a();
        v0.c.b("Runtime");
        o oVarE = o.h(l).d(listB).c(new FirebaseCommonRegistrar()).b(c0.d.q(context, Context.class, new Class[0])).b(c0.d.q(this, d.class, new Class[0])).b(c0.d.q(kVar, k.class, new Class[0])).g(new v0.b()).e();
        this.d = oVarE;
        v0.c.a();
        this.g = new x(new a0.b(this, context));
        this.h = oVarE.b(k0.g.class);
        g(new a0.c(this));
        v0.c.a();
    }

    public static void a(d dVar, boolean z) {
        dVar.v(z);
    }

    public static r0.a b(d dVar, Context context) {
        return dVar.u(context);
    }

    static Object c() {
        return k;
    }

    static void d(d dVar) {
        dVar.o();
    }

    static AtomicBoolean e(d dVar) {
        return dVar.e;
    }

    static void f(d dVar, boolean z) {
        dVar.x(z);
    }

    private void h() {
        n.m(!this.f.get(), "FirebaseApp was deleted");
    }

    public static d k() {
        d dVar;
        synchronized (k) {
            try {
                dVar = (d) m.get("[DEFAULT]");
                if (dVar == null) {
                    throw new IllegalStateException("Default FirebaseApp is not initialized in this process " + l.a() + ". Make sure to call FirebaseApp.initializeApp(Context) first.");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return dVar;
    }

    private void o() {
        if (!m.a(this.a)) {
            Log.i("FirebaseApp", "Device in Direct Boot Mode: postponing initialization of Firebase APIs for app " + l());
            e.a(this.a);
            return;
        }
        Log.i("FirebaseApp", "Device unlocked: initializing all Firebase APIs for app " + l());
        this.d.k(t());
        ((k0.g) this.h.get()).n();
    }

    public static d p(Context context) {
        synchronized (k) {
            try {
                if (m.containsKey("[DEFAULT]")) {
                    return k();
                }
                k kVarA = k.a(context);
                if (kVarA == null) {
                    Log.w("FirebaseApp", "Default FirebaseApp failed to initialize because no default options were found. This usually means that com.google.gms:google-services was not applied to your gradle project.");
                    return null;
                }
                return q(context, kVarA);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static d q(Context context, k kVar) {
        return r(context, kVar, "[DEFAULT]");
    }

    public static d r(Context context, k kVar, String str) {
        d dVar;
        c.b(context);
        String strW = w(str);
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        synchronized (k) {
            Map map = m;
            n.m(!map.containsKey(strW), "FirebaseApp name " + strW + " already exists!");
            n.j(context, "Application context cannot be null.");
            dVar = new d(context, strW, kVar);
            map.put(strW, dVar);
        }
        dVar.o();
        return dVar;
    }

    private r0.a u(Context context) {
        return new r0.a(context, n(), (j0.c) this.d.a(j0.c.class));
    }

    private void v(boolean z) {
        if (z) {
            return;
        }
        ((k0.g) this.h.get()).n();
    }

    private static String w(String str) {
        return str.trim();
    }

    private void x(boolean z) {
        Log.d("FirebaseApp", "Notifying background state change listeners.");
        Iterator it = this.i.iterator();
        while (it.hasNext()) {
            ((b) it.next()).a(z);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof d) {
            return this.b.equals(((d) obj).l());
        }
        return false;
    }

    public void g(b bVar) {
        h();
        if (this.e.get() && com.google.android.gms.common.api.internal.a.b().d()) {
            bVar.a(true);
        }
        this.i.add(bVar);
    }

    public int hashCode() {
        return this.b.hashCode();
    }

    public Object i(Class cls) {
        h();
        return this.d.a(cls);
    }

    public Context j() {
        h();
        return this.a;
    }

    public String l() {
        h();
        return this.b;
    }

    public k m() {
        h();
        return this.c;
    }

    public String n() {
        return J.c.a(l().getBytes(Charset.defaultCharset())) + "+" + J.c.a(m().c().getBytes(Charset.defaultCharset()));
    }

    public boolean s() {
        h();
        return ((r0.a) this.g.get()).b();
    }

    public boolean t() {
        return "[DEFAULT]".equals(l());
    }

    public String toString() {
        return F.m.c(this).a("name", this.b).a("options", this.c).toString();
    }
}
