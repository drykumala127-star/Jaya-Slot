package J;

import android.os.Looper;

public abstract class q {
    public static boolean a() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
