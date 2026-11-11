package B;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class b extends BroadcastReceiver {
    private final ExecutorService a;

    public b() {
        Q.e.a();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new K.a("firebase-iid-executor"));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        this.a = Executors.unconfigurableExecutorService(threadPoolExecutor);
    }

    private final int e(Context context, Intent intent) {
        Z.i iVarC;
        if (intent.getExtras() == null) {
            return 500;
        }
        String stringExtra = intent.getStringExtra("google.message_id");
        if (TextUtils.isEmpty(stringExtra)) {
            iVarC = Z.l.e(null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("google.message_id", stringExtra);
            iVarC = w.b(context).c(2, bundle);
        }
        int iB = b(context, new a(intent));
        try {
            Z.l.b(iVarC, TimeUnit.SECONDS.toMillis(1L), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            String strValueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(strValueOf.length() + 20);
            sb.append("Message ack failed: ");
            sb.append(strValueOf);
            Log.w("CloudMessagingReceiver", sb.toString());
        }
        return iB;
    }

    private final int f(Context context, Intent intent) {
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException unused) {
                Log.e("CloudMessagingReceiver", "Notification pending intent canceled");
            }
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            extras.remove("pending_intent");
        } else {
            extras = new Bundle();
        }
        if ("com.google.firebase.messaging.NOTIFICATION_DISMISS".equals(intent.getAction())) {
            c(context, extras);
            return -1;
        }
        Log.e("CloudMessagingReceiver", "Unknown notification action");
        return 500;
    }

    protected Executor a() {
        return this.a;
    }

    protected abstract int b(Context context, a aVar);

    protected abstract void c(Context context, Bundle bundle);

    public final void d(Intent intent, Context context, boolean z, BroadcastReceiver.PendingResult pendingResult) {
        try {
            Parcelable parcelableExtra = intent.getParcelableExtra("wrapped_intent");
            Intent intent2 = parcelableExtra instanceof Intent ? (Intent) parcelableExtra : null;
            int iF = intent2 != null ? f(context, intent2) : e(context, intent);
            if (z) {
                pendingResult.setResultCode(iF);
            }
        } finally {
            pendingResult.finish();
        }
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        a().execute(new i(this, intent, context, isOrderedBroadcast(), goAsync()));
    }
}
