package org.cocos2dx.javascript;

import Z.i;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.File;
import org.cocos2dx.lib.Cocos2dxActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class PlatformAndroidApi {
    public static JSONObject takephotoparam;
    public static AppActivity context = (AppActivity) Cocos2dxActivity.getContext();
    public static String openAppUrlDataString = "";
    public static String firebaseMsgToken = "";
    public static String channelstr = "";
    public static String channelex = "";
    public static boolean googleService = true;
    public static String constracts = "";

    class a implements Z.d {
        a() {
        }

        @Override
        public void a(i iVar) {
            if (iVar.m()) {
                String str = (String) iVar.i();
                PlatformAndroidApi.firebaseMsgToken = str;
                Log.w("FCM", str);
            }
        }
    }

    class b implements Z.d {
        b() {
        }

        @Override
        public void a(i iVar) {
            Log.d("FCM", !iVar.m() ? "Subscribe failed" : "Subscribed");
        }
    }

    class c implements Z.d {
        c() {
        }

        @Override
        public void a(i iVar) {
            Log.d("FCM", !iVar.m() ? "unSubscribe failed" : "unSubscribed");
        }
    }

    class d implements Runnable {
        final String i;

        d(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            ((ClipboardManager) PlatformAndroidApi.context.getSystemService("clipboard")).setText(this.i);
        }
    }

    class e implements Runnable {
        final String i;

        e(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            try {
                String strTrim = this.i.trim();
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(strTrim));
                intent.addCategory("android.intent.category.BROWSABLE");
                try {
                    PlatformAndroidApi.context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.w("openURL", "No default handler, using chooser: " + strTrim, e);
                    PlatformAndroidApi.context.startActivity(Intent.createChooser(intent, null));
                }
            } catch (Exception e2) {
                Log.e("openURL", "Failed to open URL: " + this.i, e2);
                ProjUtil.showToast("Failed to open Browser");
            }
        }
    }

    class f implements Runnable {
        final String i;

        f(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            if (this.i.equals("portrait")) {
                PlatformAndroidApi.context.setRequestedOrientation(7);
            } else if (this.i.equals("landscape")) {
                PlatformAndroidApi.context.setRequestedOrientation(6);
            }
        }
    }

    class g implements Runnable {
        final String i;
        final String j;
        final String k;

        g(String str, String str2, String str3) {
            this.i = str;
            this.j = str2;
            this.k = str3;
        }

        @Override
        public void run() {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/*");
            intent.putExtra("android.intent.extra.TEXT", this.i);
            intent.putExtra("android.intent.extra.TITLE", this.j);
            if (!this.k.isEmpty()) {
                intent.setPackage(this.k);
            }
            if (intent.resolveActivity(PlatformAndroidApi.context.getPackageManager()) != null) {
                PlatformAndroidApi.context.startActivity(intent);
            } else {
                intent.setPackage(null);
                PlatformAndroidApi.context.startActivity(Intent.createChooser(intent, this.j));
            }
        }
    }

    public static void ADTrackEvent(String str) {
    }

    public static void FbFriendsInApp() {
    }

    public static void KoTrackEvent(String str) {
    }

    public static boolean OpenFB(String str) {
        return false;
    }

    public static int SaveToAlumb(String str) {
        return ProjUtil.saveToAlumb(str);
    }

    public static int SaveUrlToAlumb(String str) {
        String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (d.c.a(context, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            c.b.g(context, strArr, 1);
            return 0;
        }
        Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(ProjUtil.getHtmlStream(str));
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append(".jpg");
        return ProjUtil.saveImageToGallery(context, bitmapDecodeStream, sb.toString()) ? 1 : 0;
    }

    public static void SdkPay(String str) {
    }

    public static void SystemImageShare(String str) {
        try {
            int i = Build.VERSION.SDK_INT;
            if (i >= 23 && i <= 29) {
                String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                if (d.c.a(context, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    c.b.g(context, strArr, 1);
                    return;
                }
            }
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("title");
            String string2 = jSONObject.getString("imgUrl");
            String string3 = jSONObject.has("pkName") ? jSONObject.getString("pkName") : "";
            String string4 = jSONObject.has("content") ? jSONObject.getString("content") : "";
            File fileSaveBitmap = ProjUtil.saveBitmap(context, BitmapFactory.decodeFile(string2), new File(string2).getName(), null, "shareData");
            Uri fileProvider = i >= 24 ? getFileProvider(context, fileSaveBitmap) : Uri.fromFile(fileSaveBitmap);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            if (!string4.isEmpty()) {
                intent.putExtra("android.intent.extra.TEXT", string4);
            }
            intent.putExtra("android.intent.extra.STREAM", fileProvider);
            intent.addFlags(1);
            if (!string3.isEmpty()) {
                intent.setPackage(string3);
            }
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                intent.setPackage(null);
                context.startActivity(Intent.createChooser(intent, string));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void callPhone(String str) {
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + str));
        context.startActivity(intent);
    }

    public static void clearOpenAppUrlDataString() {
        openAppUrlDataString = "";
    }

    public static void closeSpalsh() {
        AppActivity.hideSplash();
    }

    public static void fbSdkLogin() {
    }

    public static void fbSdkLoginOut() {
    }

    public static void fbSdkShare(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if ((jSONObject.has("shareWhere") ? jSONObject.getInt("shareWhere") : 1) == 3 && ProjUtil.isAppInstall(context, "com.whatsapp")) {
                String string = jSONObject.getString("msgStr");
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", string);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                context.startActivity(intent);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public static String getADTrackId() {
        return "";
    }

    public static String getAPPBundleId() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.packageName;
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static String getAppName() {
        try {
            return ((Object) context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).loadLabel(context.getPackageManager())) + "";
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static String getAppVersion() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e2) {
            e2.printStackTrace();
            return "1.0.0";
        }
    }

    public static String getApplicationLanguage() {
        return "2";
    }

    public static String getChannelExStr() {
        return channelex;
    }

    public static String getChannelstr() {
        return channelstr;
    }

    public static String getContracts() {
        return constracts;
    }

    public static String getDeviceBrand() {
        return ProjUtil.getDeviceBrand() + "_" + ProjUtil.getDeviceModel();
    }

    public static String getDeviceId() {
        return ProjUtil.getAnroidId();
    }

    public static String getDeviceOpSysVision() {
        return ProjUtil.getSystempVision();
    }

    public static String getFMCToken() {
        return firebaseMsgToken;
    }

    public static Uri getFileProvider(Context context2, File file) {
        return d.d.getUriForFile(context2, getAPPBundleId() + ".fileprovider", file);
    }

    public static String getGSFId() {
        return ProjUtil.getGSFID();
    }

    public static String getKoTrackUUID() {
        return "";
    }

    public static String getOpenAppUrlDataString() {
        String str = openAppUrlDataString;
        openAppUrlDataString = "";
        return str;
    }

    public static String getSimOperator() {
        return "";
    }

    public static String getSimacardid() {
        return "";
    }

    public static String getSingure() {
        return ProjUtil.getSignture();
    }

    @SuppressLint({"NewApi"})
    public static String getTxtFromClipboard() {
        ClipData primaryClip = ((ClipboardManager) context.getSystemService("clipboard")).getPrimaryClip();
        return (primaryClip == null || primaryClip.getItemAt(0) == null) ? "" : primaryClip.getItemAt(0).getText().toString();
    }

    public static void googleInstallReff() {
    }

    public static void googleLogin() {
    }

    public static void gpCheckOwned() {
    }

    public static int isCloner() {
        boolean zIsCloner = ProjUtil.isCloner(context);
        if (googleService) {
            return zIsCloner ? 1 : 0;
        }
        return 1;
    }

    public static int isInstallFB() {
        return 0;
    }

    public static boolean isOpenGPS() {
        return false;
    }

    public static void joinVoiceChannel(String str) {
    }

    public static void levelVoiceChannel() {
    }

    public static void loadAdMobRewardAd() {
    }

    public static void loadReviewComment() {
    }

    public static void loadTradPlusRewardedVideo(String str) {
    }

    public static void openGPSSetting() {
    }

    public static boolean openRating() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getAPPBundleId()));
        intent.addFlags(268435456);
        context.startActivity(intent);
        return true;
    }

    public static void openURL(String str) {
        if (str == null || str.trim().isEmpty()) {
            return;
        }
        context.runOnUiThread(new e(str));
    }

    public static void packageAppShare(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("packname");
            if (ProjUtil.isAppInstall(context, string)) {
                String string2 = jSONObject.getString("msgStr");
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", string2);
                intent.setType("text/plain");
                intent.setPackage(string);
                context.startActivity(intent);
            } else {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("packname", string);
                jSONObject2.put("result", "-1");
                JsTool.sendToPlatformApiCbFunc("packageAppShareCall", jSONObject2);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public static void phoneShock(String str) {
        ProjUtil.phoneShock(str);
    }

    public static void queryAllSKU(String str) {
    }

    public static void reGetFMCToken() {
        FirebaseMessaging.n().q().b(new a());
    }

    public static void requestContracts() {
    }

    public static void sendMail(String str) {
        Intent intent;
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("sender");
            String string2 = jSONObject.getString("title");
            String string3 = jSONObject.getString("content");
            if ((jSONObject.has("sendway") ? jSONObject.getString("sendway") : "send") == "send") {
                intent = new Intent("android.intent.action.SEND");
                intent.setType("message/rfc822");
            } else {
                intent = new Intent("android.intent.action.SENDTO");
                intent.setData(Uri.parse("mailto:"));
            }
            intent.putExtra("android.intent.extra.EMAIL", new String[]{string});
            intent.putExtra("android.intent.extra.SUBJECT", string2);
            intent.putExtra("android.intent.extra.TEXT", string3);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                context.startActivity(Intent.createChooser(intent, ""));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            ProjUtil.showToast("There are no email clients installed.");
        }
    }

    public static void setLocalMute(String str) {
    }

    public static void setOrientation(String str) {
        context.runOnUiThread(new f(str));
    }

    public static void setRemoteMute(String str) {
    }

    @SuppressLint({"NewApi"})
    public static void setTxtToClipboard(String str) {
        context.runOnUiThread(new d(str));
    }

    public static void showTradPlusRewardedVideo(String str) {
    }

    public static void subscribeFCMTopic(String str) {
        FirebaseMessaging.n().H(str).b(new b());
    }

    public static void systemShare(java.lang.String r5) {
        throw new UnsupportedOperationException("Method not generated: org.cocos2dx.javascript.PlatformAndroidApi.systemShare(java.lang.String):void");
    }

    public static void takePhoto(String str) {
        try {
            takephotoparam = new JSONObject(str);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", false);
        context.startActivityForResult(Intent.createChooser(intent, "Chooser an image"), 100);
    }

    public static void unsubscribeFCMTopic(String str) {
        FirebaseMessaging.n().K(str).b(new c());
    }
}
