package e;

import android.graphics.PorterDuff;
import android.graphics.drawable.Icon;

public abstract class k {
    public static Icon a(Icon icon, PorterDuff.Mode mode) {
        return icon.setTintMode(mode);
    }
}
