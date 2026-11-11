package c;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;

public abstract class f {
    public static int a(Context context, String str, String str2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return d.a((AppOpsManager) c.a(context, AppOpsManager.class), str, str2);
        }
        return 1;
    }

    public static String b(String str) {
        if (Build.VERSION.SDK_INT >= 23) {
            return e.a(str);
        }
        return null;
    }
}
