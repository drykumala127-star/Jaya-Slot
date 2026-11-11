package P;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class g extends d.c {
    public static Intent g(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        if (e.a()) {
            return f.a(context, broadcastReceiver, intentFilter, true != e.a() ? 0 : 2);
        }
        return context.registerReceiver(broadcastReceiver, intentFilter);
    }
}
