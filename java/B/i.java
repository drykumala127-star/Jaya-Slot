package B;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class i implements Runnable {
    public final b i;
    public final Intent j;
    public final Context k;
    public final boolean l;
    public final BroadcastReceiver.PendingResult m;

    public i(b bVar, Intent intent, Context context, boolean z, BroadcastReceiver.PendingResult pendingResult) {
        this.i = bVar;
        this.j = intent;
        this.k = context;
        this.l = z;
        this.m = pendingResult;
    }

    @Override
    public final void run() {
        this.i.d(this.j, this.k, this.l, this.m);
    }
}
