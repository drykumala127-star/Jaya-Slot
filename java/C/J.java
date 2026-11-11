package c;

import android.app.Notification;

public abstract class j {
    public static Notification.Builder a(Notification.Builder builder, CharSequence[] charSequenceArr) {
        return builder.setRemoteInputHistory(charSequenceArr);
    }
}
