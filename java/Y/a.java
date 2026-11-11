package y;

import C.k;
import C.l;
import F.n;
import O.e;
import O.f;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class a {
    C.a a;
    f b;
    boolean c;
    final Object d = new Object();
    c e;
    private final Context f;
    final long g;

    public static final class a {
        private final String a;
        private final boolean b;

        public a(String str, boolean z) {
            this.a = str;
            this.b = z;
        }

        public String a() {
            return this.a;
        }

        public boolean b() {
            return this.b;
        }

        public String toString() {
            String str = this.a;
            boolean z = this.b;
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 7);
            sb.append("{");
            sb.append(str);
            sb.append("}");
            sb.append(z);
            return sb.toString();
        }
    }

    public a(Context context, long j, boolean z, boolean z2) {
        Context applicationContext;
        n.i(context);
        if (z && (applicationContext = context.getApplicationContext()) != null) {
            context = applicationContext;
        }
        this.f = context;
        this.c = false;
        this.g = j;
    }

    public static a a(Context context) {
        a aVar = new a(context, -1L, true, false);
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            aVar.d(false);
            a aVarF = aVar.f(-1);
            aVar.e(aVarF, true, 0.0f, SystemClock.elapsedRealtime() - jElapsedRealtime, "", null);
            aVar.c();
            return aVarF;
        } catch (Throwable th) {
            try {
                aVar.e(null, true, 0.0f, -1L, "", th);
                throw th;
            } catch (Throwable th2) {
                aVar.c();
                throw th2;
            }
        }
    }

    private final a f(int i) {
        a aVar;
        n.h("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            try {
                if (!this.c) {
                    synchronized (this.d) {
                        c cVar = this.e;
                        if (cVar == null || !cVar.l) {
                            throw new IOException("AdvertisingIdClient is not connected.");
                        }
                    }
                    try {
                        d(false);
                        if (!this.c) {
                            throw new IOException("AdvertisingIdClient cannot reconnect.");
                        }
                    } catch (Exception e) {
                        throw new IOException("AdvertisingIdClient cannot reconnect.", e);
                    }
                }
                n.i(this.a);
                n.i(this.b);
                try {
                    aVar = new a(this.b.c(), this.b.z(true));
                } catch (RemoteException e2) {
                    Log.i("AdvertisingIdClient", "GMS remote exception ", e2);
                    throw new IOException("Remote exception");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        g();
        return aVar;
    }

    private final void g() {
        synchronized (this.d) {
            c cVar = this.e;
            if (cVar != null) {
                cVar.k.countDown();
                try {
                    this.e.join();
                } catch (InterruptedException unused) {
                }
            }
            long j = this.g;
            if (j > 0) {
                this.e = new c(this, j);
            }
        }
    }

    public final void c() {
        n.h("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            try {
                if (this.f == null || this.a == null) {
                    return;
                }
                try {
                    if (this.c) {
                        I.b.b().c(this.f, this.a);
                    }
                } catch (Throwable th) {
                    Log.i("AdvertisingIdClient", "AdvertisingIdClient unbindService failed.", th);
                }
                this.c = false;
                this.b = null;
                this.a = null;
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }

    protected final void d(boolean z) {
        n.h("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            try {
                if (this.c) {
                    c();
                }
                Context context = this.f;
                try {
                    context.getPackageManager().getPackageInfo("com.android.vending", 0);
                    int iH = k.f().h(context, 12451000);
                    if (iH != 0 && iH != 2) {
                        throw new IOException("Google Play services not available");
                    }
                    C.a aVar = new C.a();
                    Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                    intent.setPackage("com.google.android.gms");
                    try {
                        if (!I.b.b().a(context, intent, aVar, 1)) {
                            throw new IOException("Connection failure");
                        }
                        this.a = aVar;
                        try {
                            this.b = e.a(aVar.a(10000L, TimeUnit.MILLISECONDS));
                            this.c = true;
                            if (z) {
                                g();
                            }
                        } catch (InterruptedException unused) {
                            throw new IOException("Interrupted exception");
                        } catch (Throwable th) {
                            throw new IOException(th);
                        }
                    } catch (Throwable th2) {
                        throw new IOException(th2);
                    }
                } catch (PackageManager.NameNotFoundException unused2) {
                    throw new l(9);
                }
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    final boolean e(a aVar, boolean z, float f, long j, String str, Throwable th) {
        if (Math.random() > 0.0d) {
            return false;
        }
        HashMap map = new HashMap();
        map.put("app_context", "1");
        if (aVar != null) {
            map.put("limit_ad_tracking", true != aVar.b() ? "0" : "1");
            String strA = aVar.a();
            if (strA != null) {
                map.put("ad_id_size", Integer.toString(strA.length()));
            }
        }
        if (th != null) {
            map.put("error", th.getClass().getName());
        }
        map.put("tag", "AdvertisingIdClient");
        map.put("time_spent", Long.toString(j));
        new b(this, map).start();
        return true;
    }

    protected final void finalize() throws Throwable {
        c();
        super.finalize();
    }

    public static void b(boolean z) {
    }
}
