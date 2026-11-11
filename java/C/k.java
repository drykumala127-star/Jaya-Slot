package c;

import android.app.Notification;

public abstract class k {
    public static Notification.Builder a(Notification.Builder builder, boolean z) {
        return builder.setAllowSystemGeneratedContextualActions(z);
    }
}
