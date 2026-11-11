package org.cocos2dx.javascript;

import C.f;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import c.i;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.Q;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void scheduleJob() {
    }

    private void sendNotification(String str, String str2) {
        Intent intent = new Intent((Context) this, (Class<?>) AppActivity.class);
        intent.addFlags(67108864);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 67108864);
        String string = getString(w0.c.c);
        i.e eVarI = new i.e(this, string).u(w0.b.a).k(str2).j(str).f(true).v(RingtoneManager.getDefaultUri(2)).i(activity);
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            f.a(notificationManager, C.i.a(string, "Channel human readable title", 3));
        }
        notificationManager.notify(0, eVarI.b());
    }

    private void sendRegistrationToServer(String str) {
        PlatformAndroidApi.firebaseMsgToken = str;
    }

    public void onMessageReceived(Q q) {
        Log.d(TAG, "From: " + q.g());
        if (q.d().size() > 0) {
            Log.d(TAG, "Message data payload: " + q.d());
            handleNow();
        }
        if (q.h() != null) {
            sendNotification(q.h().a(), q.h().c());
            Log.d(TAG, "Message Notification Body: " + q.h().a());
        }
    }

    public void onNewToken(String str) {
        Log.d(TAG, "Refreshed token: " + str);
        sendRegistrationToServer(str);
    }
}
