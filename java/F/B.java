package f;

import android.content.res.Configuration;
import android.os.Build;

public abstract class b {
    public static d a(Configuration configuration) {
        return Build.VERSION.SDK_INT >= 24 ? d.d(a.a(configuration)) : d.a(configuration.locale);
    }
}
