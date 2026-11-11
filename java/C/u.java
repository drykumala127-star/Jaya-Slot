package c;

import android.app.Notification;
import android.widget.RemoteViews;

public abstract class u {
    public static Notification.Builder a(Notification.Builder builder, RemoteViews remoteViews) {
        return builder.setCustomBigContentView(remoteViews);
    }
}
