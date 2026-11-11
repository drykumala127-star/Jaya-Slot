package org.cocos2dx.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class Cocos2dxWebView extends WebView {
    private static final String TAG = Cocos2dxWebViewHelper.class.getSimpleName();
    private String mJSScheme;
    private int mViewTag;

    class a extends WebViewClient {

        class a implements Runnable {
            final String i;

            a(String str) {
                this.i = str;
            }

            @Override
            public void run() {
                Cocos2dxWebViewHelper._onJsCallback(Cocos2dxWebView.access$100(Cocos2dxWebView.this), this.i);
            }
        }

        class b implements Runnable {
            final String i;

            b(String str) {
                this.i = str;
            }

            @Override
            public void run() {
                Cocos2dxWebViewHelper._didFinishLoading(Cocos2dxWebView.access$100(Cocos2dxWebView.this), this.i);
            }
        }

        class c implements Runnable {
            final String i;

            c(String str) {
                this.i = str;
            }

            @Override
            public void run() {
                Cocos2dxWebViewHelper._didFailLoading(Cocos2dxWebView.access$100(Cocos2dxWebView.this), this.i);
            }
        }

        a() {
        }

        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            ((Cocos2dxActivity) Cocos2dxWebView.this.getContext()).runOnGLThread(new b(str));
        }

        @Override
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            ((Cocos2dxActivity) Cocos2dxWebView.this.getContext()).runOnGLThread(new c(str2));
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            Cocos2dxActivity cocos2dxActivity = (Cocos2dxActivity) Cocos2dxWebView.this.getContext();
            try {
                URI uriCreate = URI.create(str);
                if (uriCreate != null && uriCreate.getScheme().equals(Cocos2dxWebView.access$000(Cocos2dxWebView.this))) {
                    cocos2dxActivity.runOnGLThread(new a(str));
                    return true;
                }
            } catch (Exception unused) {
                Log.d(Cocos2dxWebView.access$200(), "Failed to create URI from url");
            }
            boolean[] zArr = {true};
            CountDownLatch countDownLatch = new CountDownLatch(1);
            cocos2dxActivity.runOnGLThread(new org.cocos2dx.lib.b(countDownLatch, zArr, Cocos2dxWebView.access$100(Cocos2dxWebView.this), str));
            try {
                countDownLatch.await();
            } catch (InterruptedException unused2) {
                Log.d(Cocos2dxWebView.access$200(), "'shouldOverrideUrlLoading' failed");
            }
            return zArr[0];
        }
    }

    public Cocos2dxWebView(Context context) {
        this(context, -1);
    }

    static String access$000(Cocos2dxWebView cocos2dxWebView) {
        return cocos2dxWebView.mJSScheme;
    }

    static int access$100(Cocos2dxWebView cocos2dxWebView) {
        return cocos2dxWebView.mViewTag;
    }

    static String access$200() {
        return TAG;
    }

    public void setJavascriptInterfaceScheme(String str) {
        if (str == null) {
            str = "";
        }
        this.mJSScheme = str;
    }

    public void setScalesPageToFit(boolean z) {
        getSettings().setSupportZoom(z);
    }

    public void setWebViewRect(int i, int i2, int i3, int i4) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.leftMargin = i;
        layoutParams.topMargin = i2;
        layoutParams.width = i3;
        layoutParams.height = i4;
        setLayoutParams(layoutParams);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public Cocos2dxWebView(Context context, int i) {
        super(context);
        this.mViewTag = i;
        this.mJSScheme = "";
        setFocusable(true);
        setFocusableInTouchMode(true);
        getSettings().setSupportZoom(false);
        getSettings().setDomStorageEnabled(true);
        getSettings().setJavaScriptEnabled(true);
        try {
            getClass().getMethod("removeJavascriptInterface", String.class).invoke(this, "searchBoxJavaBridge_");
        } catch (Exception unused) {
            Log.d(TAG, "This API level do not support `removeJavascriptInterface`");
        }
        setWebViewClient(new a());
        setWebChromeClient(new WebChromeClient());
    }
}
