package B;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import java.util.List;

public final class x {
    private final Context a;
    private int b;
    private int c = 0;

    public x(Context context) {
        this.a = context;
    }

    public final synchronized int a() {
        PackageInfo packageInfoE;
        if (this.b == 0) {
            try {
                packageInfoE = L.e.a(this.a).e("com.google.android.gms", 0);
            } catch (PackageManager.NameNotFoundException e) {
                String strValueOf = String.valueOf(e);
                StringBuilder sb = new StringBuilder(strValueOf.length() + 23);
                sb.append("Failed to find package ");
                sb.append(strValueOf);
                Log.w("Metadata", sb.toString());
                packageInfoE = null;
            }
            if (packageInfoE != null) {
                this.b = packageInfoE.versionCode;
            }
        }
        return this.b;
    }

    public final synchronized int b() {
        int i = this.c;
        if (i != 0) {
            return i;
        }
        PackageManager packageManager = this.a.getPackageManager();
        if (L.e.a(this.a).b("com.google.android.c2dm.permission.SEND", "com.google.android.gms") == -1) {
            Log.e("Metadata", "Google Play services missing or without correct permission.");
            return 0;
        }
        int i2 = 1;
        if (!J.k.h()) {
            Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
            intent.setPackage("com.google.android.gms");
            List<ResolveInfo> listQueryIntentServices = packageManager.queryIntentServices(intent, 0);
            if (listQueryIntentServices != null && listQueryIntentServices.size() > 0) {
                this.c = 1;
                return 1;
            }
        }
        Intent intent2 = new Intent("com.google.iid.TOKEN_REQUEST");
        intent2.setPackage("com.google.android.gms");
        List<ResolveInfo> listQueryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent2, 0);
        if (listQueryBroadcastReceivers != null && listQueryBroadcastReceivers.size() > 0) {
            this.c = 2;
            return 2;
        }
        Log.w("Metadata", "Failed to resolve IID implementation package, falling back");
        if (J.k.h()) {
            this.c = 2;
            i2 = 2;
        } else {
            this.c = 1;
        }
        return i2;
    }
}
