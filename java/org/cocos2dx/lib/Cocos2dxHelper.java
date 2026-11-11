package org.cocos2dx.lib;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowInsets;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import org.cocos2dx.lib.Cocos2dxAccelerometer;

public class Cocos2dxHelper {
    public static final int NETWORK_TYPE_LAN = 1;
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_WWAN = 2;
    private static final String PREFS_NAME = "Cocos2dxPrefsFile";
    private static final int RUNNABLES_PER_FRAME = 5;
    private static final String TAG = "Cocos2dxHelper";
    private static boolean sAccelerometerEnabled;
    private static Activity sActivity;
    private static boolean sActivityVisible;
    private static AssetManager sAssetManager;
    private static Cocos2dxAccelerometer sCocos2dxAccelerometer;
    private static Cocos2dxHelperListener sCocos2dxHelperListener;
    private static boolean sCompassEnabled;
    private static String sFileDirectory;
    private static OnGameInfoUpdatedListener sOnGameInfoUpdatedListener;
    private static String sPackageName;
    private static Set onActivityResultListeners = new LinkedHashSet();
    private static Vibrator sVibrateService = null;
    private static String sAssetsPath = "";
    private static j.b sOBBFile = null;
    private static c sBatteryReceiver = new c();
    private static boolean sInited = false;
    private static float[] sDeviceMotionValues = new float[9];

    public interface Cocos2dxHelperListener {
        void runOnGLThread(Runnable runnable);

        void showDialog(String str, String str2);
    }

    public interface OnGameInfoUpdatedListener {
        void onDisableBatchGLCommandsToNative();

        void onFPSUpdated(float f);

        void onGameInfoUpdated_0(String str);

        void onGameInfoUpdated_1(String str);

        void onGameInfoUpdated_2(String str);

        void onJSBInvocationCountUpdated(int i);

        void onOpenDebugView();
    }

    class a implements Runnable {
        final String i;

        a(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            ((ClipboardManager) Cocos2dxHelper.access$000().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", this.i));
        }
    }

    class b implements Runnable {
        final byte[] i;

        b(byte[] bArr) {
            this.i = bArr;
        }

        @Override
        public void run() {
            Cocos2dxHelper.access$100(this.i);
        }
    }

    static class c extends BroadcastReceiver {
        public float a = 0.0f;

        c() {
        }

        public void a(Intent intent) {
            if (intent != null) {
                this.a = Math.min(Math.max((intent.getIntExtra("level", 0) * 1.0f) / intent.getIntExtra("scale", 1), 0.0f), 1.0f);
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            a(intent);
        }
    }

    static Activity access$000() {
        return sActivity;
    }

    static void access$100(byte[] bArr) {
        nativeSetEditTextDialogResult(bArr);
    }

    public static void addOnActivityResultListener(PreferenceManager.OnActivityResultListener onActivityResultListener) {
        onActivityResultListeners.add(onActivityResultListener);
    }

    public static byte[] conversionEncoding(byte[] bArr, String str, String str2) {
        try {
            return new String(bArr, str).getBytes(str2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copyTextToClipboard(String str) {
        sActivity.runOnUiThread(new a(str));
    }

    public static void disableAccelerometer() {
        sAccelerometerEnabled = false;
        sCocos2dxAccelerometer.disable();
    }

    private static void disableBatchGLCommandsToNative() {
        OnGameInfoUpdatedListener onGameInfoUpdatedListener = sOnGameInfoUpdatedListener;
        if (onGameInfoUpdatedListener != null) {
            onGameInfoUpdatedListener.onDisableBatchGLCommandsToNative();
        }
    }

    public static void enableAccelerometer() {
        sAccelerometerEnabled = true;
        sCocos2dxAccelerometer.enable();
    }

    public static void endApplication() {
        Activity activity = sActivity;
        if (activity != null) {
            activity.finish();
        }
    }

    public static Activity getActivity() {
        return sActivity;
    }

    public static AssetManager getAssetManager() {
        return sAssetManager;
    }

    public static String getAssetsPath() {
        int i;
        if (sAssetsPath == "") {
            try {
                i = sActivity.getPackageManager().getPackageInfo(sPackageName, 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                i = 1;
            }
            String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/" + sPackageName + "/main." + i + "." + sPackageName + ".obb";
            if (new File(str).exists()) {
                sAssetsPath = str;
            } else {
                sAssetsPath = sActivity.getApplicationInfo().sourceDir;
            }
        }
        return sAssetsPath;
    }

    public static float getBatteryLevel() {
        return sBatteryReceiver.a;
    }

    public static String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getCurrentLanguageCode() {
        return Locale.getDefault().toString();
    }

    public static int getDPI() {
        Display defaultDisplay;
        if (sActivity == null) {
            return -1;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = sActivity.getWindowManager();
        if (windowManager == null || (defaultDisplay = windowManager.getDefaultDisplay()) == null) {
            return -1;
        }
        defaultDisplay.getMetrics(displayMetrics);
        return (int) (displayMetrics.density * 160.0f);
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    private static float[] getDeviceMotionValue() {
        Cocos2dxAccelerometer.b deviceMotionEvent = sCocos2dxAccelerometer.getDeviceMotionEvent();
        float[] fArr = sDeviceMotionValues;
        Cocos2dxAccelerometer.a aVar = deviceMotionEvent.a;
        fArr[0] = aVar.a;
        fArr[1] = aVar.b;
        fArr[2] = aVar.c;
        Cocos2dxAccelerometer.a aVar2 = deviceMotionEvent.b;
        fArr[3] = aVar2.a;
        fArr[4] = aVar2.b;
        fArr[RUNNABLES_PER_FRAME] = aVar2.c;
        Cocos2dxAccelerometer.c cVar = deviceMotionEvent.c;
        fArr[6] = cVar.a;
        fArr[7] = cVar.b;
        fArr[8] = cVar.c;
        return fArr;
    }

    public static int getDeviceRotation() {
        try {
            return ((WindowManager) sActivity.getSystemService("window")).getDefaultDisplay().getRotation();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getNetworkType() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) sActivity.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return 0;
            }
            int type = activeNetworkInfo.getType();
            if (type == 0) {
                return 2;
            }
            return type == 1 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long[] getObbAssetFileDescriptor(String str) {
        AssetFileDescriptor assetFileDescriptorB;
        long[] jArr = new long[3];
        j.b bVar = sOBBFile;
        if (bVar != null && (assetFileDescriptorB = bVar.b(str)) != null) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = assetFileDescriptorB.getParcelFileDescriptor();
                jArr[0] = ((Integer) parcelFileDescriptor.getClass().getMethod("getFd", null).invoke(parcelFileDescriptor, null)).intValue();
                jArr[1] = assetFileDescriptorB.getStartOffset();
                jArr[2] = assetFileDescriptorB.getLength();
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.toString());
            } catch (NoSuchMethodException unused) {
                Log.e(TAG, "Accessing file descriptor directly from the OBB is only supported from Android 3.1 (API level 12) and above.");
            } catch (InvocationTargetException e2) {
                Log.e(TAG, e2.toString());
            }
        }
        return jArr;
    }

    public static j.b getObbFile() {
        return sOBBFile;
    }

    public static Set getOnActivityResultListeners() {
        return onActivityResultListeners;
    }

    public static OnGameInfoUpdatedListener getOnGameInfoUpdatedListener() {
        return sOnGameInfoUpdatedListener;
    }

    public static String getPackageName() {
        return sPackageName;
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static float[] getSafeArea() {
        WindowInsets windowInsetsA;
        if (Build.VERSION.SDK_INT >= 28 && (windowInsetsA = org.cocos2dx.lib.a.a(getActivity().getWindow().getDecorView())) != null) {
            try {
                Object objInvoke = WindowInsets.class.getMethod("getDisplayCutout", null).invoke(windowInsetsA, null);
                if (objInvoke != null) {
                    Class<?> cls = objInvoke.getClass();
                    Method method = cls.getMethod("getSafeInsetLeft", null);
                    Method method2 = cls.getMethod("getSafeInsetRight", null);
                    Method method3 = cls.getMethod("getSafeInsetBottom", null);
                    Method method4 = cls.getMethod("getSafeInsetTop", null);
                    if (method != null && method2 != null && method3 != null && method4 != null) {
                        return new float[]{((Integer) method4.invoke(objInvoke, null)).intValue(), ((Integer) method.invoke(objInvoke, null)).intValue(), ((Integer) method3.invoke(objInvoke, null)).intValue(), ((Integer) method2.invoke(objInvoke, null)).intValue()};
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        return new float[]{0.0f, 0.0f, 0.0f, 0.0f};
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getVersion() {
        try {
            return Cocos2dxActivity.getContext().getPackageManager().getPackageInfo(Cocos2dxActivity.getContext().getPackageName(), 0).versionName;
        } catch (Exception unused) {
            return "";
        }
    }

    public static String getWritablePath() {
        return sFileDirectory;
    }

    public static void init(Activity activity) {
        int i = 1;
        sActivity = activity;
        sCocos2dxHelperListener = (Cocos2dxHelperListener) activity;
        if (sInited) {
            return;
        }
        boolean zHasSystemFeature = activity.getPackageManager().hasSystemFeature("android.hardware.audio.low_latency");
        String str = TAG;
        Log.d(str, "isSupportLowLatency:" + zHasSystemFeature);
        AudioManager audioManager = (AudioManager) activity.getSystemService("audio");
        String str2 = (String) Cocos2dxReflectionHelper.invokeInstanceMethod(audioManager, "getProperty", new Class[]{String.class}, new Object[]{Cocos2dxReflectionHelper.getConstantValue(AudioManager.class, "PROPERTY_OUTPUT_SAMPLE_RATE")});
        String str3 = (String) Cocos2dxReflectionHelper.invokeInstanceMethod(audioManager, "getProperty", new Class[]{String.class}, new Object[]{Cocos2dxReflectionHelper.getConstantValue(AudioManager.class, "PROPERTY_OUTPUT_FRAMES_PER_BUFFER")});
        int i2 = Integer.parseInt(str2);
        int i3 = Integer.parseInt(str3);
        Log.d(str, "sampleRate: " + i2 + ", framesPerBuffer: " + i3);
        nativeSetAudioDeviceInfo(zHasSystemFeature, i2, i3);
        sPackageName = activity.getApplicationInfo().packageName;
        sFileDirectory = activity.getFilesDir().getAbsolutePath();
        nativeSetApkPath(getAssetsPath());
        sCocos2dxAccelerometer = new Cocos2dxAccelerometer(activity);
        AssetManager assets = activity.getAssets();
        sAssetManager = assets;
        nativeSetContext(activity, assets);
        sVibrateService = (Vibrator) activity.getSystemService("vibrator");
        sInited = true;
        try {
            i = Cocos2dxActivity.getContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            sOBBFile = j.a.b(Cocos2dxActivity.getContext(), i, 0);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static boolean isActivityVisible() {
        return sActivityVisible;
    }

    private static native void nativeSetApkPath(String str);

    private static native void nativeSetAudioDeviceInfo(boolean z, int i, int i2);

    private static native void nativeSetContext(Context context, AssetManager assetManager);

    private static native void nativeSetEditTextDialogResult(byte[] bArr);

    public static void onEnterBackground() {
    }

    public static void onEnterForeground() {
    }

    public static void onPause() {
        sActivityVisible = false;
        if (sAccelerometerEnabled) {
            sCocos2dxAccelerometer.disable();
        }
    }

    public static void onResume() {
        sActivityVisible = true;
        if (sAccelerometerEnabled) {
            sCocos2dxAccelerometer.enable();
        }
    }

    private static void openDebugView() {
        OnGameInfoUpdatedListener onGameInfoUpdatedListener = sOnGameInfoUpdatedListener;
        if (onGameInfoUpdatedListener != null) {
            onGameInfoUpdatedListener.onOpenDebugView();
        }
    }

    public static boolean openURL(String str) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            sActivity.startActivity(intent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    static void registerBatteryLevelReceiver(Context context) {
        sBatteryReceiver.a(context.registerReceiver(sBatteryReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED")));
    }

    public static void runOnGLThread(Runnable runnable) {
        ((Cocos2dxActivity) sActivity).runOnGLThread(runnable);
    }

    public static void setAccelerometerInterval(float f) {
        sCocos2dxAccelerometer.setInterval(f);
    }

    public static void setEditTextDialogResult(String str) {
        try {
            sCocos2dxHelperListener.runOnGLThread(new b(str.getBytes("UTF8")));
        } catch (UnsupportedEncodingException unused) {
        }
    }

    private static void setGameInfoDebugViewText(int i, String str) {
        OnGameInfoUpdatedListener onGameInfoUpdatedListener = sOnGameInfoUpdatedListener;
        if (onGameInfoUpdatedListener != null) {
            if (i == 0) {
                onGameInfoUpdatedListener.onGameInfoUpdated_0(str);
            } else if (i == 1) {
                onGameInfoUpdatedListener.onGameInfoUpdated_1(str);
            } else if (i == 2) {
                onGameInfoUpdatedListener.onGameInfoUpdated_2(str);
            }
        }
    }

    private static void setJSBInvocationCount(int i) {
        OnGameInfoUpdatedListener onGameInfoUpdatedListener = sOnGameInfoUpdatedListener;
        if (onGameInfoUpdatedListener != null) {
            onGameInfoUpdatedListener.onJSBInvocationCountUpdated(i);
        }
    }

    public static void setKeepScreenOn(boolean z) {
        ((Cocos2dxActivity) sActivity).setKeepScreenOn(z);
    }

    public static void setOnGameInfoUpdatedListener(OnGameInfoUpdatedListener onGameInfoUpdatedListener) {
        sOnGameInfoUpdatedListener = onGameInfoUpdatedListener;
    }

    private static void showDialog(String str, String str2) {
        sCocos2dxHelperListener.showDialog(str, str2);
    }

    public static void terminateProcess() {
        Process.killProcess(Process.myPid());
    }

    static void unregisterBatteryLevelReceiver(Context context) {
        context.unregisterReceiver(sBatteryReceiver);
    }

    public static void vibrate(float f) {
        try {
            Vibrator vibrator = sVibrateService;
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    Class<?> cls = Class.forName("android.os.VibrationEffect");
                    Integer num = (Integer) Cocos2dxReflectionHelper.getConstantValue(cls, "DEFAULT_AMPLITUDE");
                    num.intValue();
                    Method method = cls.getMethod("createOneShot", Long.TYPE, Integer.TYPE);
                    Cocos2dxReflectionHelper.invokeInstanceMethod(sVibrateService, "vibrate", new Class[]{method.getReturnType()}, new Object[]{method.invoke(cls, Long.valueOf((long) (f * 1000.0f)), num)});
                } else {
                    sVibrateService.vibrate((long) (f * 1000.0f));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
