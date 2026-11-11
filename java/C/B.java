package c;

import android.app.Activity;
import android.os.Build;

public abstract class b extends d.c {
    public static void g(Activity activity, String[] strArr, int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            a.a(activity, strArr, i);
        }
    }
}
