package r0;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import j0.c;

public class a {
    private final Context a;
    private final SharedPreferences b;
    private final c c;
    private boolean d;

    public a(Context context, String str, c cVar) {
        Context contextA = a(context);
        this.a = contextA;
        this.b = contextA.getSharedPreferences("com.google.firebase.common.prefs:" + str, 0);
        this.c = cVar;
        this.d = c();
    }

    private static Context a(Context context) {
        return Build.VERSION.SDK_INT < 24 ? context : d.c.b(context);
    }

    private boolean c() {
        return this.b.contains("firebase_data_collection_default_enabled") ? this.b.getBoolean("firebase_data_collection_default_enabled", true) : d();
    }

    private boolean d() {
        ApplicationInfo applicationInfo;
        Bundle bundle;
        try {
            PackageManager packageManager = this.a.getPackageManager();
            if (packageManager == null || (applicationInfo = packageManager.getApplicationInfo(this.a.getPackageName(), 128)) == null || (bundle = applicationInfo.metaData) == null || !bundle.containsKey("firebase_data_collection_default_enabled")) {
                return true;
            }
            return applicationInfo.metaData.getBoolean("firebase_data_collection_default_enabled");
        } catch (PackageManager.NameNotFoundException unused) {
            return true;
        }
    }

    public synchronized boolean b() {
        return this.d;
    }
}
