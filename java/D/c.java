package d;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import java.io.File;

public abstract class c {
    private static final Object a = new Object();

    public static int a(Context context, String str) {
        if (str != null) {
            return context.checkPermission(str, Process.myPid(), Process.myUid());
        }
        throw new IllegalArgumentException("permission is null");
    }

    public static Context b(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return b.a(context);
        }
        return null;
    }

    public static int c(Context context, int i) {
        return Build.VERSION.SDK_INT >= 23 ? a.a(context, i) : context.getResources().getColor(i);
    }

    public static File[] d(Context context) {
        return context.getExternalCacheDirs();
    }

    public static File[] e(Context context, String str) {
        return context.getExternalFilesDirs(str);
    }

    public static File f(Context context) {
        return context.getNoBackupFilesDir();
    }
}
