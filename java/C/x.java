package c;

import android.app.Notification;

public abstract class x {
    public static Notification.Builder a(Notification.Builder builder, String str) {
        return builder.setShortcutId(str);
    }
}
