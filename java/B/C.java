package B;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class c {
    private static int h;
    private static PendingIntent i;
    private static final Executor j = D.a;
    private static final Pattern k = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)");
    private final Context b;
    private final x c;
    private final ScheduledExecutorService d;
    private Messenger f;
    private h g;
    private final a.f a = new a.f();
    private Messenger e = new Messenger(new e(this, Looper.getMainLooper()));

    public c(Context context) {
        this.b = context;
        this.c = new x(context);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.setKeepAliveTime(60L, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.allowCoreThreadTimeOut(true);
        this.d = scheduledThreadPoolExecutor;
    }

    static Z.i b(Bundle bundle) {
        return j(bundle) ? Z.l.e(null) : Z.l.e(bundle);
    }

    static void d(c cVar, Message message) {
        if (message != null) {
            Object obj = message.obj;
            if (obj instanceof Intent) {
                Intent intent = (Intent) obj;
                intent.setExtrasClassLoader(new g());
                if (intent.hasExtra("google.messenger")) {
                    Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
                    if (parcelableExtra instanceof h) {
                        cVar.g = (h) parcelableExtra;
                    }
                    if (parcelableExtra instanceof Messenger) {
                        cVar.f = (Messenger) parcelableExtra;
                    }
                }
                Intent intent2 = (Intent) message.obj;
                String action = intent2.getAction();
                if (!"com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
                    if (Log.isLoggable("Rpc", 3)) {
                        String strValueOf = String.valueOf(action);
                        Log.d("Rpc", strValueOf.length() != 0 ? "Unexpected response action: ".concat(strValueOf) : new String("Unexpected response action: "));
                        return;
                    }
                    return;
                }
                String stringExtra = intent2.getStringExtra("registration_id");
                if (stringExtra == null) {
                    stringExtra = intent2.getStringExtra("unregistered");
                }
                if (stringExtra != null) {
                    Matcher matcher = k.matcher(stringExtra);
                    if (!matcher.matches()) {
                        if (Log.isLoggable("Rpc", 3)) {
                            Log.d("Rpc", stringExtra.length() != 0 ? "Unexpected response string: ".concat(stringExtra) : new String("Unexpected response string: "));
                            return;
                        }
                        return;
                    }
                    String strGroup = matcher.group(1);
                    String strGroup2 = matcher.group(2);
                    if (strGroup != null) {
                        Bundle extras = intent2.getExtras();
                        extras.putString("registration_id", strGroup2);
                        cVar.i(strGroup, extras);
                        return;
                    }
                    return;
                }
                String stringExtra2 = intent2.getStringExtra("error");
                if (stringExtra2 == null) {
                    String strValueOf2 = String.valueOf(intent2.getExtras());
                    StringBuilder sb = new StringBuilder(strValueOf2.length() + 49);
                    sb.append("Unexpected response, no error or registration id ");
                    sb.append(strValueOf2);
                    Log.w("Rpc", sb.toString());
                    return;
                }
                if (Log.isLoggable("Rpc", 3)) {
                    Log.d("Rpc", stringExtra2.length() != 0 ? "Received InstanceID error ".concat(stringExtra2) : new String("Received InstanceID error "));
                }
                if (!stringExtra2.startsWith("|")) {
                    synchronized (cVar.a) {
                        for (int i2 = 0; i2 < cVar.a.size(); i2++) {
                            try {
                                cVar.i((String) cVar.a.i(i2), intent2.getExtras());
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                    return;
                }
                String[] strArrSplit = stringExtra2.split("\\|");
                if (strArrSplit.length <= 2 || !"ID".equals(strArrSplit[1])) {
                    Log.w("Rpc", stringExtra2.length() != 0 ? "Unexpected structured response ".concat(stringExtra2) : new String("Unexpected structured response "));
                    return;
                }
                String str = strArrSplit[2];
                String strSubstring = strArrSplit[3];
                if (strSubstring.startsWith(":")) {
                    strSubstring = strSubstring.substring(1);
                }
                cVar.i(str, intent2.putExtra("error", strSubstring).getExtras());
                return;
            }
        }
        Log.w("Rpc", "Dropping invalid message");
    }

    private final Z.i f(Bundle bundle) {
        String strG = g();
        Z.j jVar = new Z.j();
        synchronized (this.a) {
            this.a.put(strG, jVar);
        }
        Intent intent = new Intent();
        intent.setPackage("com.google.android.gms");
        if (this.c.b() == 2) {
            intent.setAction("com.google.iid.TOKEN_REQUEST");
        } else {
            intent.setAction("com.google.android.c2dm.intent.REGISTER");
        }
        intent.putExtras(bundle);
        h(this.b, intent);
        StringBuilder sb = new StringBuilder(String.valueOf(strG).length() + 5);
        sb.append("|ID|");
        sb.append(strG);
        sb.append("|");
        intent.putExtra("kid", sb.toString());
        if (Log.isLoggable("Rpc", 3)) {
            String strValueOf = String.valueOf(intent.getExtras());
            StringBuilder sb2 = new StringBuilder(strValueOf.length() + 8);
            sb2.append("Sending ");
            sb2.append(strValueOf);
            Log.d("Rpc", sb2.toString());
        }
        intent.putExtra("google.messenger", this.e);
        if (this.f != null || this.g != null) {
            Message messageObtain = Message.obtain();
            messageObtain.obj = intent;
            try {
                Messenger messenger = this.f;
                if (messenger != null) {
                    messenger.send(messageObtain);
                } else {
                    this.g.b(messageObtain);
                }
            } catch (RemoteException unused) {
                if (Log.isLoggable("Rpc", 3)) {
                    Log.d("Rpc", "Messenger failed, fallback to startService");
                }
                if (this.c.b() == 2) {
                    this.b.sendBroadcast(intent);
                } else {
                    this.b.startService(intent);
                }
            }
        } else if (this.c.b() == 2) {
            this.b.sendBroadcast(intent);
        } else {
            this.b.startService(intent);
        }
        jVar.a().c(j, new A(this, strG, this.d.schedule(new C(jVar), 30L, TimeUnit.SECONDS)));
        return jVar.a();
    }

    private static synchronized String g() {
        int i2;
        i2 = h;
        h = i2 + 1;
        return Integer.toString(i2);
    }

    private static synchronized void h(Context context, Intent intent) {
        try {
            if (i == null) {
                Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                i = Q.a.a(context, 0, intent2, Q.a.a);
            }
            intent.putExtra("app", i);
        } catch (Throwable th) {
            throw th;
        }
    }

    private final void i(String str, Bundle bundle) {
        synchronized (this.a) {
            try {
                Z.j jVar = (Z.j) this.a.remove(str);
                if (jVar != null) {
                    jVar.c(bundle);
                } else {
                    String strValueOf = String.valueOf(str);
                    Log.w("Rpc", strValueOf.length() != 0 ? "Missing callback for ".concat(strValueOf) : new String("Missing callback for "));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static boolean j(Bundle bundle) {
        return bundle != null && bundle.containsKey("google.messenger");
    }

    public Z.i a(Bundle bundle) {
        return this.c.a() < 12000000 ? this.c.b() != 0 ? f(bundle).g(j, new y(this, bundle)) : Z.l.d(new IOException("MISSING_INSTANCEID_SERVICE")) : w.b(this.b).d(1, bundle).f(j, z.a);
    }

    public final Z.i c(Bundle bundle, Z.i iVar) {
        return (iVar.m() && j((Bundle) iVar.i())) ? f(bundle).o(j, B.a) : iVar;
    }

    public final void e(String str, ScheduledFuture scheduledFuture, Z.i iVar) {
        synchronized (this.a) {
            this.a.remove(str);
        }
        scheduledFuture.cancel(false);
    }
}
