package org.cocos2dx.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.Iterator;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import org.cocos2dx.lib.Cocos2dxHandler;
import org.cocos2dx.lib.Cocos2dxHelper;

public abstract class Cocos2dxActivity extends Activity implements Cocos2dxHelper.Cocos2dxHelperListener {
    private static final String TAG = "Cocos2dxActivity";
    private static Cocos2dxActivity sContext;
    private TextView mFPSTextView;
    private TextView mGLOptModeTextView;
    private TextView mGameInfoTextView_0;
    private TextView mGameInfoTextView_1;
    private TextView mGameInfoTextView_2;
    private TextView mJSBInvocationTextView;
    private LinearLayout mLinearLayoutForDebugView;
    protected FrameLayout mFrameLayout = null;
    private Cocos2dxGLSurfaceView mGLSurfaceView = null;
    private int[] mGLContextAttrs = null;
    private Cocos2dxHandler mHandler = null;
    private Cocos2dxVideoHelper mVideoHelper = null;
    private Cocos2dxWebViewHelper mWebViewHelper = null;
    private boolean hasFocus = false;
    private Cocos2dxEditBox mEditBox = null;
    private boolean gainAudioFocus = false;
    private boolean paused = true;

    class a implements Runnable {
        final boolean i;

        a(boolean z) {
            this.i = z;
        }

        @Override
        public void run() {
            Cocos2dxActivity.access$100(Cocos2dxActivity.this).setKeepScreenOn(this.i);
        }
    }

    class b implements Cocos2dxHelper.OnGameInfoUpdatedListener {
        final LinearLayout.LayoutParams a;
        final Cocos2dxRenderer b;

        class a implements Runnable {
            final float i;

            a(float f) {
                this.i = f;
            }

            @Override
            public void run() {
                if (Cocos2dxActivity.access$200(Cocos2dxActivity.this) != null) {
                    Cocos2dxActivity.access$200(Cocos2dxActivity.this).setText("FPS: " + ((int) Math.ceil(this.i)));
                }
            }
        }

        class b implements Runnable {
            final int i;

            b(int i) {
                this.i = i;
            }

            @Override
            public void run() {
                if (Cocos2dxActivity.access$300(Cocos2dxActivity.this) != null) {
                    Cocos2dxActivity.access$300(Cocos2dxActivity.this).setText("JSB: " + this.i);
                }
            }
        }

        class c implements Runnable {
            c() {
            }

            @Override
            public void run() {
                if (Cocos2dxActivity.access$400(Cocos2dxActivity.this) == null) {
                    Cocos2dxActivity cocos2dxActivity = Cocos2dxActivity.this;
                    if (cocos2dxActivity.mFrameLayout != null) {
                        Cocos2dxActivity.access$402(cocos2dxActivity, new LinearLayout(Cocos2dxActivity.this));
                        Cocos2dxActivity.access$400(Cocos2dxActivity.this).setOrientation(1);
                        Cocos2dxActivity cocos2dxActivity2 = Cocos2dxActivity.this;
                        cocos2dxActivity2.mFrameLayout.addView(Cocos2dxActivity.access$400(cocos2dxActivity2));
                        Cocos2dxActivity.access$202(Cocos2dxActivity.this, new TextView(Cocos2dxActivity.this));
                        Cocos2dxActivity.access$200(Cocos2dxActivity.this).setBackgroundColor(-65536);
                        Cocos2dxActivity.access$200(Cocos2dxActivity.this).setTextColor(-1);
                        Cocos2dxActivity.access$400(Cocos2dxActivity.this).addView(Cocos2dxActivity.access$200(Cocos2dxActivity.this), b.this.a);
                        Cocos2dxActivity.access$302(Cocos2dxActivity.this, new TextView(Cocos2dxActivity.this));
                        Cocos2dxActivity.access$300(Cocos2dxActivity.this).setBackgroundColor(-16711936);
                        Cocos2dxActivity.access$300(Cocos2dxActivity.this).setTextColor(-1);
                        Cocos2dxActivity.access$400(Cocos2dxActivity.this).addView(Cocos2dxActivity.access$300(Cocos2dxActivity.this), b.this.a);
                        Cocos2dxActivity.access$602(Cocos2dxActivity.this, new TextView(Cocos2dxActivity.this));
                        Cocos2dxActivity.access$600(Cocos2dxActivity.this).setBackgroundColor(-16776961);
                        Cocos2dxActivity.access$600(Cocos2dxActivity.this).setTextColor(-1);
                        Cocos2dxActivity.access$600(Cocos2dxActivity.this).setText("GL Opt: Enabled");
                        Cocos2dxActivity.access$400(Cocos2dxActivity.this).addView(Cocos2dxActivity.access$600(Cocos2dxActivity.this), b.this.a);
                        Cocos2dxActivity.access$702(Cocos2dxActivity.this, new TextView(Cocos2dxActivity.this));
                        Cocos2dxActivity.access$700(Cocos2dxActivity.this).setBackgroundColor(-65536);
                        Cocos2dxActivity.access$700(Cocos2dxActivity.this).setTextColor(-1);
                        Cocos2dxActivity.access$400(Cocos2dxActivity.this).addView(Cocos2dxActivity.access$700(Cocos2dxActivity.this), b.this.a);
                        Cocos2dxActivity.access$802(Cocos2dxActivity.this, new TextView(Cocos2dxActivity.this));
                        Cocos2dxActivity.access$800(Cocos2dxActivity.this).setBackgroundColor(-16711936);
                        Cocos2dxActivity.access$800(Cocos2dxActivity.this).setTextColor(-1);
                        Cocos2dxActivity.access$400(Cocos2dxActivity.this).addView(Cocos2dxActivity.access$800(Cocos2dxActivity.this), b.this.a);
                        Cocos2dxActivity.access$902(Cocos2dxActivity.this, new TextView(Cocos2dxActivity.this));
                        Cocos2dxActivity.access$900(Cocos2dxActivity.this).setBackgroundColor(-16776961);
                        Cocos2dxActivity.access$900(Cocos2dxActivity.this).setTextColor(-1);
                        Cocos2dxActivity.access$400(Cocos2dxActivity.this).addView(Cocos2dxActivity.access$900(Cocos2dxActivity.this), b.this.a);
                        return;
                    }
                }
                Log.e(Cocos2dxActivity.access$500(), "onOpenDebugView: failed!");
            }
        }

        class d implements Runnable {
            d() {
            }

            @Override
            public void run() {
                if (Cocos2dxActivity.access$600(Cocos2dxActivity.this) != null) {
                    Cocos2dxActivity.access$600(Cocos2dxActivity.this).setText("GL Opt: Disabled");
                }
            }
        }

        class e implements Runnable {
            final String i;

            e(String str) {
                this.i = str;
            }

            @Override
            public void run() {
                if (Cocos2dxActivity.access$700(Cocos2dxActivity.this) != null) {
                    Cocos2dxActivity.access$700(Cocos2dxActivity.this).setText(this.i);
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
                if (Cocos2dxActivity.access$800(Cocos2dxActivity.this) != null) {
                    Cocos2dxActivity.access$800(Cocos2dxActivity.this).setText(this.i);
                }
            }
        }

        class g implements Runnable {
            final String i;

            g(String str) {
                this.i = str;
            }

            @Override
            public void run() {
                if (Cocos2dxActivity.access$900(Cocos2dxActivity.this) != null) {
                    Cocos2dxActivity.access$900(Cocos2dxActivity.this).setText(this.i);
                }
            }
        }

        b(LinearLayout.LayoutParams layoutParams, Cocos2dxRenderer cocos2dxRenderer) {
            this.a = layoutParams;
            this.b = cocos2dxRenderer;
        }

        @Override
        public void onDisableBatchGLCommandsToNative() {
            Cocos2dxActivity.this.runOnUiThread(new d());
        }

        @Override
        public void onFPSUpdated(float f2) {
            Cocos2dxActivity.this.runOnUiThread(new a(f2));
        }

        @Override
        public void onGameInfoUpdated_0(String str) {
            Cocos2dxActivity.this.runOnUiThread(new e(str));
        }

        @Override
        public void onGameInfoUpdated_1(String str) {
            Cocos2dxActivity.this.runOnUiThread(new f(str));
        }

        @Override
        public void onGameInfoUpdated_2(String str) {
            Cocos2dxActivity.this.runOnUiThread(new g(str));
        }

        @Override
        public void onJSBInvocationCountUpdated(int i) {
            Cocos2dxActivity.this.runOnUiThread(new b(i));
        }

        @Override
        public void onOpenDebugView() {
            Cocos2dxActivity.this.runOnUiThread(new c());
            this.b.showFPS();
        }
    }

    static Cocos2dxGLSurfaceView access$100(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mGLSurfaceView;
    }

    static TextView access$200(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mFPSTextView;
    }

    static TextView access$202(Cocos2dxActivity cocos2dxActivity, TextView textView) {
        cocos2dxActivity.mFPSTextView = textView;
        return textView;
    }

    static TextView access$300(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mJSBInvocationTextView;
    }

    static TextView access$302(Cocos2dxActivity cocos2dxActivity, TextView textView) {
        cocos2dxActivity.mJSBInvocationTextView = textView;
        return textView;
    }

    static LinearLayout access$400(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mLinearLayoutForDebugView;
    }

    static LinearLayout access$402(Cocos2dxActivity cocos2dxActivity, LinearLayout linearLayout) {
        cocos2dxActivity.mLinearLayoutForDebugView = linearLayout;
        return linearLayout;
    }

    static String access$500() {
        return TAG;
    }

    static TextView access$600(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mGLOptModeTextView;
    }

    static TextView access$602(Cocos2dxActivity cocos2dxActivity, TextView textView) {
        cocos2dxActivity.mGLOptModeTextView = textView;
        return textView;
    }

    static TextView access$700(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mGameInfoTextView_0;
    }

    static TextView access$702(Cocos2dxActivity cocos2dxActivity, TextView textView) {
        cocos2dxActivity.mGameInfoTextView_0 = textView;
        return textView;
    }

    static TextView access$800(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mGameInfoTextView_1;
    }

    static TextView access$802(Cocos2dxActivity cocos2dxActivity, TextView textView) {
        cocos2dxActivity.mGameInfoTextView_1 = textView;
        return textView;
    }

    static TextView access$900(Cocos2dxActivity cocos2dxActivity) {
        return cocos2dxActivity.mGameInfoTextView_2;
    }

    static TextView access$902(Cocos2dxActivity cocos2dxActivity, TextView textView) {
        cocos2dxActivity.mGameInfoTextView_2 = textView;
        return textView;
    }

    private void addDebugInfo(Cocos2dxRenderer cocos2dxRenderer) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(30, 0, 0, 0);
        Cocos2dxHelper.setOnGameInfoUpdatedListener(new b(layoutParams, cocos2dxRenderer));
    }

    private Cocos2dxRenderer addSurfaceView() {
        Cocos2dxGLSurfaceView cocos2dxGLSurfaceViewOnCreateView = onCreateView();
        this.mGLSurfaceView = cocos2dxGLSurfaceViewOnCreateView;
        cocos2dxGLSurfaceViewOnCreateView.setPreserveEGLContextOnPause(true);
        this.mGLSurfaceView.setBackgroundColor(0);
        if (isAndroidEmulator()) {
            this.mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        }
        Cocos2dxRenderer cocos2dxRenderer = new Cocos2dxRenderer();
        this.mGLSurfaceView.setCocos2dxRenderer(cocos2dxRenderer);
        this.mFrameLayout.addView(this.mGLSurfaceView);
        return cocos2dxRenderer;
    }

    public static Context getContext() {
        return sContext;
    }

    private static native int[] getGLContextAttrs();

    private static final boolean isAndroidEmulator() {
        String str = Build.MODEL;
        String str2 = TAG;
        Log.d(str2, "model=" + str);
        String str3 = Build.PRODUCT;
        Log.d(str2, "product=" + str3);
        boolean z = false;
        if (str3 != null && (str3.equals("sdk") || str3.contains("_sdk") || str3.contains("sdk_"))) {
            z = true;
        }
        Log.d(str2, "isEmulator=" + z);
        return z;
    }

    private void resumeIfHasFocus() {
        if (!this.hasFocus || this.paused) {
            return;
        }
        Utils.hideVirtualButton();
        Cocos2dxHelper.onResume();
        this.mGLSurfaceView.onResume();
    }

    public Cocos2dxGLSurfaceView getGLSurfaceView() {
        return this.mGLSurfaceView;
    }

    public void init() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        FrameLayout frameLayout = new FrameLayout(this);
        this.mFrameLayout = frameLayout;
        frameLayout.setLayoutParams(layoutParams);
        addDebugInfo(addSurfaceView());
        this.mEditBox = new Cocos2dxEditBox(this, this.mFrameLayout);
        setContentView(this.mFrameLayout);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        try {
            Field field = attributes.getClass().getField("layoutInDisplayCutoutMode");
            if (field != null) {
                field.setInt(attributes, attributes.getClass().getDeclaredField("LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES").getInt(null));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        Iterator it = Cocos2dxHelper.getOnActivityResultListeners().iterator();
        while (it.hasNext()) {
            ((PreferenceManager.OnActivityResultListener) it.next()).onActivityResult(i, i2, intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        String str = TAG;
        Log.d(str, "Cocos2dxActivity onCreate: " + this + ", savedInstanceState: " + bundle);
        super.onCreate(bundle);
        if (!isTaskRoot()) {
            finish();
            Log.w(str, "[Workaround] Ignore the activity started from icon!");
            return;
        }
        Utils.setActivity(this);
        Utils.hideVirtualButton();
        Cocos2dxHelper.registerBatteryLevelReceiver(this);
        onLoadNativeLibraries();
        sContext = this;
        this.mHandler = new Cocos2dxHandler(this);
        Cocos2dxHelper.init(this);
        CanvasRenderingContext2DImpl.init(this);
        this.mGLContextAttrs = getGLContextAttrs();
        init();
        if (this.mVideoHelper == null) {
            this.mVideoHelper = new Cocos2dxVideoHelper(this, this.mFrameLayout);
        }
        if (this.mWebViewHelper == null) {
            this.mWebViewHelper = new Cocos2dxWebViewHelper(this.mFrameLayout);
        }
        getWindow().setSoftInputMode(16);
        setVolumeControlStream(3);
    }

    public Cocos2dxGLSurfaceView onCreateView() {
        Cocos2dxGLSurfaceView cocos2dxGLSurfaceView = new Cocos2dxGLSurfaceView(this);
        if (this.mGLContextAttrs[3] > 0) {
            cocos2dxGLSurfaceView.getHolder().setFormat(-3);
        }
        cocos2dxGLSurfaceView.setEGLConfigChooser(new Cocos2dxEGLConfigChooser(this.mGLContextAttrs));
        return cocos2dxGLSurfaceView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isTaskRoot()) {
            if (this.gainAudioFocus) {
                Cocos2dxAudioFocusManager.c(this);
            }
            Cocos2dxHelper.unregisterBatteryLevelReceiver(this);
            CanvasRenderingContext2DImpl.destroy();
            Log.d(TAG, "Cocos2dxActivity onDestroy: " + this + ", mGLSurfaceView" + this.mGLSurfaceView);
            if (this.mGLSurfaceView != null) {
                Cocos2dxHelper.terminateProcess();
            }
        }
    }

    protected void onLoadNativeLibraries() {
        try {
            System.loadLibrary(getPackageManager().getApplicationInfo(getPackageName(), 128).metaData.getString("android.app.lib_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        this.paused = true;
        super.onPause();
        if (this.gainAudioFocus) {
            Cocos2dxAudioFocusManager.c(this);
        }
        Cocos2dxHelper.onPause();
        this.mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        this.paused = false;
        super.onResume();
        if (this.gainAudioFocus) {
            Cocos2dxAudioFocusManager.b(this);
        }
        Utils.hideVirtualButton();
        resumeIfHasFocus();
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        Log.d(TAG, "onWindowFocusChanged() hasFocus=" + z);
        super.onWindowFocusChanged(z);
        this.hasFocus = z;
        resumeIfHasFocus();
    }

    @Override
    public void runOnGLThread(Runnable runnable) {
        this.mGLSurfaceView.queueEvent(runnable);
    }

    public void setEnableAudioFocusGain(boolean z) {
        if (this.gainAudioFocus != z) {
            if (!this.paused) {
                if (z) {
                    Cocos2dxAudioFocusManager.b(this);
                } else {
                    Cocos2dxAudioFocusManager.c(this);
                }
            }
            this.gainAudioFocus = z;
        }
    }

    public void setKeepScreenOn(boolean z) {
        runOnUiThread(new a(z));
    }

    @Override
    public void showDialog(String str, String str2) {
        Message message = new Message();
        message.what = 1;
        message.obj = new Cocos2dxHandler.DialogMessage(str, str2);
        this.mHandler.sendMessage(message);
    }

    public class Cocos2dxEGLConfigChooser implements GLSurfaceView.EGLConfigChooser {
        protected int[] configAttribs;

        public Cocos2dxEGLConfigChooser(int i, int i2, int i3, int i4, int i5, int i6) {
            this.configAttribs = new int[]{i, i2, i3, i4, i5, i6};
        }

        static int access$000(Cocos2dxEGLConfigChooser cocos2dxEGLConfigChooser, EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return cocos2dxEGLConfigChooser.findConfigAttrib(egl10, eGLDisplay, eGLConfig, i, i2);
        }

        private int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            int[] iArr = new int[1];
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, iArr) ? iArr[0] : i2;
        }

        @Override
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            int i;
            int[] iArr = this.configAttribs;
            int i2 = 0;
            EGLConfig[] eGLConfigArr = new EGLConfig[1];
            int[] iArr2 = new int[1];
            if (egl10.eglChooseConfig(eGLDisplay, new int[]{12324, iArr[0], 12323, iArr[1], 12322, iArr[2], 12321, iArr[3], 12325, iArr[4], 12326, iArr[5], 12352, 4, 12344}, eGLConfigArr, 1, iArr2) && iArr2[0] > 0) {
                return eGLConfigArr[0];
            }
            int[] iArr3 = {12352, 4, 12344};
            if (!egl10.eglChooseConfig(eGLDisplay, iArr3, null, 0, iArr2) || (i = iArr2[0]) <= 0) {
                Log.e("device_policy", "Can not select an EGLConfig for rendering.");
                return null;
            }
            a[] aVarArr = new a[i];
            EGLConfig[] eGLConfigArr2 = new EGLConfig[i];
            egl10.eglChooseConfig(eGLDisplay, iArr3, eGLConfigArr2, i, iArr2);
            for (int i3 = 0; i3 < i; i3++) {
                aVarArr[i3] = new a(egl10, eGLDisplay, eGLConfigArr2[i3]);
            }
            a aVar = new a(this.configAttribs);
            int i4 = i;
            while (i2 < i4 - 1) {
                int i5 = (i2 + i4) / 2;
                if (aVar.compareTo(aVarArr[i5]) < 0) {
                    i4 = i5;
                } else {
                    i2 = i5;
                }
            }
            if (i2 != i - 1) {
                i2++;
            }
            Log.w("cocos2d", "Can't find EGLConfig match: " + aVar + ", instead of closest one:" + aVarArr[i2]);
            return aVarArr[i2].i;
        }

        public Cocos2dxEGLConfigChooser(int[] iArr) {
            this.configAttribs = iArr;
        }

        class a implements Comparable {
            public EGLConfig i;
            public int[] j;
            public int k;

            public a(int[] iArr) {
                this.i = null;
                this.k = 0;
                this.j = iArr;
                a();
            }

            private void a() {
                int[] iArr = this.j;
                int i = iArr[4];
                if (i > 0) {
                    this.k = this.k + 536870912 + ((i % 64) << 6);
                }
                int i2 = iArr[5];
                if (i2 > 0) {
                    this.k = this.k + 268435456 + (i2 % 64);
                }
                int i3 = iArr[3];
                if (i3 > 0) {
                    this.k = this.k + 1073741824 + ((i3 % 16) << 24);
                }
                int i4 = iArr[1];
                if (i4 > 0) {
                    this.k += (i4 % 16) << 20;
                }
                int i5 = iArr[2];
                if (i5 > 0) {
                    this.k += (i5 % 16) << 16;
                }
                int i6 = iArr[0];
                if (i6 > 0) {
                    this.k += (i6 % 16) << 12;
                }
            }

            @Override
            public int compareTo(a aVar) {
                int i = this.k;
                int i2 = aVar.k;
                if (i < i2) {
                    return -1;
                }
                return i > i2 ? 1 : 0;
            }

            public String toString() {
                return "{ color: " + this.j[3] + this.j[2] + this.j[1] + this.j[0] + "; depth: " + this.j[4] + "; stencil: " + this.j[5] + ";}";
            }

            public a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
                this.k = 0;
                this.i = eGLConfig;
                int[] iArr = new int[6];
                this.j = iArr;
                iArr[0] = Cocos2dxEGLConfigChooser.access$000(Cocos2dxEGLConfigChooser.this, egl10, eGLDisplay, eGLConfig, 12324, 0);
                this.j[1] = Cocos2dxEGLConfigChooser.access$000(Cocos2dxEGLConfigChooser.this, egl10, eGLDisplay, eGLConfig, 12323, 0);
                this.j[2] = Cocos2dxEGLConfigChooser.access$000(Cocos2dxEGLConfigChooser.this, egl10, eGLDisplay, eGLConfig, 12322, 0);
                this.j[3] = Cocos2dxEGLConfigChooser.access$000(Cocos2dxEGLConfigChooser.this, egl10, eGLDisplay, eGLConfig, 12321, 0);
                this.j[4] = Cocos2dxEGLConfigChooser.access$000(Cocos2dxEGLConfigChooser.this, egl10, eGLDisplay, eGLConfig, 12325, 0);
                this.j[5] = Cocos2dxEGLConfigChooser.access$000(Cocos2dxEGLConfigChooser.this, egl10, eGLDisplay, eGLConfig, 12326, 0);
                a();
            }
        }
    }
}
