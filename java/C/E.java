package c;

import android.app.AppOpsManager;

public abstract class e {
    public static String a(String str) {
        return AppOpsManager.permissionToOp(str);
    }
}
