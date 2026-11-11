package c;

import android.app.Notification;

public abstract class z {
    public static Notification.Builder a(Notification.Builder builder, int i) {
        return builder.setGroupAlertBehavior(i);
    }
}
