package R;

import android.os.Build;

public abstract class d {
    public static final int a;

    static {
        a = Build.VERSION.SDK_INT >= 23 ? 67108864 : 0;
    }
}
