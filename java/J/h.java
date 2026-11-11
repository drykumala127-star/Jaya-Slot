package J;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public abstract class h {
    private static Boolean a;
    private static Boolean b;
    private static Boolean c;
    private static Boolean d;

    public static boolean a(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (d == null) {
            boolean z = false;
            if (k.h() && packageManager.hasSystemFeature("android.hardware.type.automotive")) {
                z = true;
            }
            d = Boolean.valueOf(z);
        }
        return d.booleanValue();
    }

    public static boolean b() {
        int i = C.n.a;
        return "user".equals(Build.TYPE);
    }

    public static boolean c(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (a == null) {
            boolean z = false;
            if (k.e() && packageManager.hasSystemFeature("android.hardware.type.watch")) {
                z = true;
            }
            a = Boolean.valueOf(z);
        }
        return a.booleanValue();
    }

    public static boolean d(Context context) {
        if (c(context) && !k.g()) {
            return true;
        }
        if (e(context)) {
            return !k.h() || k.k();
        }
        return false;
    }

    public static boolean e(Context context) {
        if (b == null) {
            boolean z = false;
            if (k.f() && context.getPackageManager().hasSystemFeature("cn.google")) {
                z = true;
            }
            b = Boolean.valueOf(z);
        }
        return b.booleanValue();
    }

    public static boolean f(Context context) {
        if (c == null) {
            boolean z = true;
            if (!context.getPackageManager().hasSystemFeature("android.hardware.type.iot") && !context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                z = false;
            }
            c = Boolean.valueOf(z);
        }
        return c.booleanValue();
    }
}
