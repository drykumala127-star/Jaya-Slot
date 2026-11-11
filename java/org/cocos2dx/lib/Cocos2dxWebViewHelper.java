package org.cocos2dx.lib;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.widget.FrameLayout;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Cocos2dxWebViewHelper {
    private static final String TAG = "Cocos2dxWebViewHelper";
    private static Cocos2dxActivity sCocos2dxActivity;
    private static Handler sHandler;
    private static FrameLayout sLayout;
    private static int viewTag;
    private static SparseArray webViews;

    class a implements Runnable {
        final int i;
        final String j;

        a(int i, String str) {
            this.i = i;
            this.j = str;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.loadUrl(this.j);
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
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.stopLoading();
            }
        }
    }

    class c implements Runnable {
        final int i;

        c(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.reload();
            }
        }
    }

    class d implements Callable {
        final int a;

        d(int i) {
            this.a = i;
        }

        @Override
        public Boolean call() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.a);
            return Boolean.valueOf(cocos2dxWebView != null && cocos2dxWebView.canGoBack());
        }
    }

    class e implements Callable {
        final int a;

        e(int i) {
            this.a = i;
        }

        @Override
        public Boolean call() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.a);
            return Boolean.valueOf(cocos2dxWebView != null && cocos2dxWebView.canGoForward());
        }
    }

    class f implements Runnable {
        final int i;

        f(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.goBack();
            }
        }
    }

    class g implements Runnable {
        final int i;

        g(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.goForward();
            }
        }
    }

    class h implements Runnable {
        final int i;
        final String j;

        h(int i, String str) {
            this.i = i;
            this.j = str;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.loadUrl("javascript:" + this.j);
            }
        }
    }

    class i implements Runnable {
        final int i;
        final boolean j;

        i(int i, boolean z) {
            this.i = i;
            this.j = z;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.setScalesPageToFit(this.j);
            }
        }
    }

    class j implements Runnable {
        final int i;

        j(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = new Cocos2dxWebView(Cocos2dxWebViewHelper.access$000(), this.i);
            Cocos2dxWebViewHelper.access$100().addView(cocos2dxWebView, new FrameLayout.LayoutParams(-2, -2));
            Cocos2dxWebViewHelper.access$200().put(this.i, cocos2dxWebView);
        }
    }

    class k implements Runnable {
        final int i;

        k(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                Cocos2dxWebViewHelper.access$200().remove(this.i);
                Cocos2dxWebViewHelper.access$100().removeView(cocos2dxWebView);
                cocos2dxWebView.destroy();
            }
        }
    }

    class l implements Runnable {
        final int i;
        final boolean j;

        l(int i, boolean z) {
            this.i = i;
            this.j = z;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.setVisibility(this.j ? 0 : 8);
            }
        }
    }

    class m implements Runnable {
        final int i;
        final int j;
        final int k;
        final int l;
        final int m;

        m(int i, int i2, int i3, int i4, int i5) {
            this.i = i;
            this.j = i2;
            this.k = i3;
            this.l = i4;
            this.m = i5;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.setWebViewRect(this.j, this.k, this.l, this.m);
            }
        }
    }

    class n implements Runnable {
        final int i;
        final boolean j;

        n(int i, boolean z) {
            this.i = i;
            this.j = z;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.setBackgroundColor(this.j ? 0 : -1);
                cocos2dxWebView.setLayerType(1, null);
            }
        }
    }

    class o implements Runnable {
        final int i;
        final String j;

        o(int i, String str) {
            this.i = i;
            this.j = str;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.setJavascriptInterfaceScheme(this.j);
            }
        }
    }

    class p implements Runnable {
        final int i;
        final String j;
        final String k;
        final String l;
        final String m;

        p(int i, String str, String str2, String str3, String str4) {
            this.i = i;
            this.j = str;
            this.k = str2;
            this.l = str3;
            this.m = str4;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.loadDataWithBaseURL(this.j, this.k, this.l, this.m, null);
            }
        }
    }

    class q implements Runnable {
        final int i;
        final String j;
        final String k;

        q(int i, String str, String str2) {
            this.i = i;
            this.j = str;
            this.k = str2;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.loadDataWithBaseURL(this.j, this.k, null, null, null);
            }
        }
    }

    class r implements Runnable {
        final int i;
        final String j;

        r(int i, String str) {
            this.i = i;
            this.j = str;
        }

        @Override
        public void run() {
            Cocos2dxWebView cocos2dxWebView = (Cocos2dxWebView) Cocos2dxWebViewHelper.access$200().get(this.i);
            if (cocos2dxWebView != null) {
                cocos2dxWebView.loadUrl(this.j);
            }
        }
    }

    public Cocos2dxWebViewHelper(FrameLayout frameLayout) {
        sLayout = frameLayout;
        sHandler = new Handler(Looper.myLooper());
        sCocos2dxActivity = (Cocos2dxActivity) Cocos2dxActivity.getContext();
        webViews = new SparseArray();
    }

    public static void _didFailLoading(int i2, String str) {
        didFailLoading(i2, str);
    }

    public static void _didFinishLoading(int i2, String str) {
        didFinishLoading(i2, str);
    }

    public static void _onJsCallback(int i2, String str) {
        onJsCallback(i2, str);
    }

    public static boolean _shouldStartLoading(int i2, String str) {
        return !shouldStartLoading(i2, str);
    }

    static Cocos2dxActivity access$000() {
        return sCocos2dxActivity;
    }

    static FrameLayout access$100() {
        return sLayout;
    }

    static SparseArray access$200() {
        return webViews;
    }

    public static <T> Object callInMainThread(Callable callable) {
        FutureTask futureTask = new FutureTask(callable);
        sHandler.post(futureTask);
        return futureTask.get();
    }

    public static boolean canGoBack(int i2) {
        try {
            return ((Boolean) callInMainThread(new d(i2))).booleanValue();
        } catch (InterruptedException | ExecutionException unused) {
            return false;
        }
    }

    public static boolean canGoForward(int i2) {
        try {
            return ((Boolean) callInMainThread(new e(i2))).booleanValue();
        } catch (InterruptedException | ExecutionException unused) {
            return false;
        }
    }

    public static int createWebView() {
        sCocos2dxActivity.runOnUiThread(new j(viewTag));
        int i2 = viewTag;
        viewTag = i2 + 1;
        return i2;
    }

    private static native void didFailLoading(int i2, String str);

    private static native void didFinishLoading(int i2, String str);

    public static void evaluateJS(int i2, String str) {
        sCocos2dxActivity.runOnUiThread(new h(i2, str));
    }

    public static void goBack(int i2) {
        sCocos2dxActivity.runOnUiThread(new f(i2));
    }

    public static void goForward(int i2) {
        sCocos2dxActivity.runOnUiThread(new g(i2));
    }

    public static void loadData(int i2, String str, String str2, String str3, String str4) {
        sCocos2dxActivity.runOnUiThread(new p(i2, str4, str, str2, str3));
    }

    public static void loadFile(int i2, String str) {
        sCocos2dxActivity.runOnUiThread(new a(i2, str));
    }

    public static void loadHTMLString(int i2, String str, String str2) {
        sCocos2dxActivity.runOnUiThread(new q(i2, str2, str));
    }

    public static void loadUrl(int i2, String str) {
        sCocos2dxActivity.runOnUiThread(new r(i2, str));
    }

    private static native void onJsCallback(int i2, String str);

    public static void reload(int i2) {
        sCocos2dxActivity.runOnUiThread(new c(i2));
    }

    public static void removeWebView(int i2) {
        sCocos2dxActivity.runOnUiThread(new k(i2));
    }

    public static void setBackgroundTransparent(int i2, boolean z) {
        sCocos2dxActivity.runOnUiThread(new n(i2, z));
    }

    public static void setJavascriptInterfaceScheme(int i2, String str) {
        sCocos2dxActivity.runOnUiThread(new o(i2, str));
    }

    public static void setScalesPageToFit(int i2, boolean z) {
        sCocos2dxActivity.runOnUiThread(new i(i2, z));
    }

    public static void setVisible(int i2, boolean z) {
        sCocos2dxActivity.runOnUiThread(new l(i2, z));
    }

    public static void setWebViewRect(int i2, int i3, int i4, int i5, int i6) {
        sCocos2dxActivity.runOnUiThread(new m(i2, i3, i4, i5, i6));
    }

    private static native boolean shouldStartLoading(int i2, String str);

    public static void stopLoading(int i2) {
        sCocos2dxActivity.runOnUiThread(new b(i2));
    }
}
