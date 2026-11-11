package f;

import android.content.Context;
import android.os.Build;
import android.os.UserManager;

public abstract class m {
    public static boolean a(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return l.a((UserManager) c.c.a(context, UserManager.class));
        }
        return true;
    }
}
