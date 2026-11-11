package B;

import android.os.Build;
import android.util.Log;

public final class g extends ClassLoader {
    @Override
    protected final Class loadClass(String str, boolean z) {
        if (!"com.google.android.gms.iid.MessengerCompat".equals(str)) {
            return super.loadClass(str, z);
        }
        if (!Log.isLoggable("CloudMessengerCompat", 3) && (Build.VERSION.SDK_INT != 23 || !Log.isLoggable("CloudMessengerCompat", 3))) {
            return h.class;
        }
        Log.d("CloudMessengerCompat", "Using renamed FirebaseIidMessengerCompat class");
        return h.class;
    }
}
