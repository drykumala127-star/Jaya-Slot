package c;

import android.app.Notification;
import android.content.Context;

public abstract class n {
    public static Notification.Builder a(Context context, String str) {
        return new Notification.Builder(context, str);
    }
}
