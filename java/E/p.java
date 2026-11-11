package E;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class p extends BroadcastReceiver {
    Context a;
    private final o b;

    public p(o oVar) {
        this.b = oVar;
    }

    public final void a(Context context) {
        this.a = context;
    }

    public final synchronized void b() {
        try {
            Context context = this.a;
            if (context != null) {
                context.unregisterReceiver(this);
            }
            this.a = null;
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if ("com.google.android.gms".equals(data != null ? data.getSchemeSpecificPart() : null)) {
            this.b.a();
            b();
        }
    }
}
