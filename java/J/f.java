package J;

import android.content.Context;
import android.util.Log;

public abstract class f {
    private static final String[] a = {"android.", "com.android.", "dalvik.", "java.", "javax."};

    public static boolean a(Context context, Throwable th) {
        try {
            F.n.i(context);
            F.n.i(th);
            return false;
        } catch (Exception e) {
            Log.e("CrashUtils", "Error adding exception to DropBox!", e);
            return false;
        }
    }
}
