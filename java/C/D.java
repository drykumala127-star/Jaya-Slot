package c;

import android.app.AppOpsManager;

public abstract class d {
    public static int a(AppOpsManager appOpsManager, String str, String str2) {
        return appOpsManager.noteProxyOpNoThrow(str, str2);
    }
}
