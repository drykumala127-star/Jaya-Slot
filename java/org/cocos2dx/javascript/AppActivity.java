package org.cocos2dx.javascript;

import C.f;
import C.i;
import C.j;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.json.JSONException;
import org.json.JSONObject;
import x0.g;

public class AppActivity extends Cocos2dxActivity {
    private static final int REQUEST_NOTIFY_CODE = 1;
    private static Cocos2dxActivity sCocos2dxActivity;
    private static ImageView sSplashBgImageView;
    Handler handler = new Handler();
    Runnable runnable = new a();
    private Uri selectedUri;

    class a implements Runnable {
        a() {
        }

        @Override
        public void run() {
            System.exit(0);
        }
    }

    class b implements Runnable {
        b() {
        }

        @Override
        public void run() {
            if (AppActivity.access$000() != null) {
                AppActivity.access$000().setVisibility(8);
            }
        }
    }

    static ImageView access$000() {
        return sSplashBgImageView;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            String string = getString(w0.c.b);
            String string2 = getString(w0.c.a);
            c.a();
            NotificationChannel notificationChannelA = i.a(getString(w0.c.c), string, 3);
            org.cocos2dx.javascript.a.a(notificationChannelA, string2);
            f.a((NotificationManager) org.cocos2dx.javascript.b.a(this, NotificationManager.class), notificationChannelA);
        }
    }

    public static void hideSplash() {
        sCocos2dxActivity.runOnUiThread(new b());
    }

    private boolean onCheckGooglePlayService() {
        int iG = j.n().g(this);
        if (iG == 0) {
            return true;
        }
        j.n().o(this);
        if (j.n().j(iG)) {
            Dialog dialogK = j.n().k(this, iG, 200);
            dialogK.setCancelable(false);
            dialogK.setCanceledOnTouchOutside(false);
            dialogK.show();
        }
        this.handler.postDelayed(this.runnable, 6000L);
        return false;
    }

    private void onOpenAppByUrl(Intent intent) {
        Uri data;
        String query;
        if (intent == null || (data = intent.getData()) == null || (query = data.getQuery()) == null) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            String[] strArrSplit = query.split("[&]");
            int length = strArrSplit.length;
            for (int i = 0; i < length; i += REQUEST_NOTIFY_CODE) {
                String[] strArrSplit2 = strArrSplit[i].split("[=]");
                if (strArrSplit2.length > REQUEST_NOTIFY_CODE) {
                    jSONObject.put(strArrSplit2[0], strArrSplit2[REQUEST_NOTIFY_CODE]);
                } else {
                    String str = strArrSplit2[0];
                    if (str != "") {
                        jSONObject.put(str, "");
                    }
                }
            }
            PlatformAndroidApi.openAppUrlDataString = jSONObject.toString();
            JsTool.sendToPlatformApiCbFunc("OpenAppUrlLink", jSONObject);
        } catch (JSONException unused) {
        }
    }

    private void processImgURI(Uri uri) {
        String strUriToBase64 = ImageUtils.uriToBase64(this, uri);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("result", REQUEST_NOTIFY_CODE);
            if (!strUriToBase64.isEmpty()) {
                jSONObject.put("data", strUriToBase64);
            }
        } catch (JSONException unused) {
        }
        ProjUtil.callJS("TakePhotoCallback", jSONObject);
    }

    private void showActivity(String str) {
        startActivity(getPackageManager().getLaunchIntentForPackage(str));
    }

    private static void showSplash() {
        ImageView imageView = new ImageView(sCocos2dxActivity);
        sSplashBgImageView = imageView;
        imageView.setBackgroundColor(sCocos2dxActivity.getResources().getColor(w0.a.a));
        sSplashBgImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        sCocos2dxActivity.addContentView(sSplashBgImageView, new WindowManager.LayoutParams(-1, -1));
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        Bundle extras;
        int i3;
        int i4;
        int i5;
        super.onActivityResult(i, i2, intent);
        SDKWrapper.getInstance().onActivityResult(i, i2, intent);
        if (i != 100 || i2 != -1) {
            if (i == 101) {
                if (i2 != -1) {
                    processImgURI(this.selectedUri);
                    return;
                }
                if (intent == null || (extras = intent.getExtras()) == null) {
                    return;
                }
                String strBitmapToBase64 = ProjUtil.bitmapToBase64((Bitmap) extras.getParcelable("data"), Bitmap.CompressFormat.JPEG, 80);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("result", REQUEST_NOTIFY_CODE);
                    if (!strBitmapToBase64.isEmpty()) {
                        jSONObject.put("data", strBitmapToBase64);
                    }
                } catch (JSONException unused) {
                }
                ProjUtil.callJS("TakePhotoCallback", jSONObject);
                return;
            }
            return;
        }
        if (intent == null || intent.getData() == null) {
            return;
        }
        this.selectedUri = intent.getData();
        int i6 = 0;
        if (PlatformAndroidApi.takephotoparam.has("crop")) {
            try {
                i3 = Integer.parseInt(PlatformAndroidApi.takephotoparam.getString("crop"));
                try {
                    i4 = Integer.parseInt(PlatformAndroidApi.takephotoparam.getString("height"));
                    try {
                        i6 = i3;
                        i5 = Integer.parseInt(PlatformAndroidApi.takephotoparam.getString("width"));
                    } catch (JSONException e) {
                        e = e;
                        e.printStackTrace();
                        i6 = i3;
                        i5 = 0;
                    }
                } catch (JSONException e2) {
                    e = e2;
                    i4 = 0;
                    e.printStackTrace();
                    i6 = i3;
                    i5 = 0;
                    if (i6 == REQUEST_NOTIFY_CODE) {
                        processImgURI(this.selectedUri);
                        return;
                    }
                    try {
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setDataAndType(this.selectedUri, "image/*");
                        intent2.putExtra("crop", "true");
                        intent2.putExtra("aspectX", REQUEST_NOTIFY_CODE);
                        intent2.putExtra("aspectY", REQUEST_NOTIFY_CODE);
                        intent2.putExtra("outputX", i5);
                        intent2.putExtra("outputY", i4);
                        intent2.putExtra("return-data", true);
                        intent2.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        startActivityForResult(intent2, 101);
                    } catch (Exception unused2) {
                        processImgURI(this.selectedUri);
                        return;
                    }
                }
            } catch (JSONException e3) {
                e = e3;
                i3 = 0;
            }
        } else {
            i5 = 0;
            i4 = 0;
        }
        if (i6 == REQUEST_NOTIFY_CODE) {
            processImgURI(this.selectedUri);
            return;
        }
        Intent intent22 = new Intent("com.android.camera.action.CROP");
        intent22.setDataAndType(this.selectedUri, "image/*");
        intent22.putExtra("crop", "true");
        intent22.putExtra("aspectX", REQUEST_NOTIFY_CODE);
        intent22.putExtra("aspectY", REQUEST_NOTIFY_CODE);
        intent22.putExtra("outputX", i5);
        intent22.putExtra("outputY", i4);
        intent22.putExtra("return-data", true);
        intent22.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent22, 101);
    }

    @Override
    public void onBackPressed() {
        SDKWrapper.getInstance().onBackPressed();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("result", "Back");
            JsTool.sendToPlatformApiCbFunc("BackPressedCallback", jSONObject);
        } catch (JSONException unused) {
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        SDKWrapper.getInstance().onConfigurationChanged(configuration);
        super.onConfigurationChanged(configuration);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (isTaskRoot()) {
            sCocos2dxActivity = this;
            getWindow().addFlags(128);
            showSplash();
            SDKWrapper.getInstance().init(this);
            x0.b bVarB = g.b(Cocos2dxActivity.getContext());
            if (bVarB != null) {
                PlatformAndroidApi.channelstr = bVarB.a();
                try {
                    PlatformAndroidApi.channelex = ProjUtil.map2json(bVarB.b()).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            onOpenAppByUrl(getIntent());
            PlatformAndroidApi.googleService = onCheckGooglePlayService();
            createNotificationChannel();
            PlatformAndroidApi.reGetFMCToken();
        }
    }

    @Override
    public Cocos2dxGLSurfaceView onCreateView() {
        Cocos2dxGLSurfaceView cocos2dxGLSurfaceView = new Cocos2dxGLSurfaceView(this);
        cocos2dxGLSurfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);
        SDKWrapper.getInstance().setGLSurfaceView(cocos2dxGLSurfaceView, this);
        return cocos2dxGLSurfaceView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isTaskRoot()) {
            SDKWrapper.getInstance().onDestroy();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SDKWrapper.getInstance().onNewIntent(intent);
        onOpenAppByUrl(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SDKWrapper.getInstance().onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SDKWrapper.getInstance().onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        SDKWrapper.getInstance().onRestoreInstanceState(bundle);
        super.onRestoreInstanceState(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKWrapper.getInstance().onResume();
        PlatformAndroidApi.googleService = onCheckGooglePlayService();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        SDKWrapper.getInstance().onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onStart() {
        SDKWrapper.getInstance().onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SDKWrapper.getInstance().onStop();
    }

    private void showActivity(String str, String str2) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(str, str2));
        intent.addFlags(268435456);
        startActivity(intent);
    }
}
