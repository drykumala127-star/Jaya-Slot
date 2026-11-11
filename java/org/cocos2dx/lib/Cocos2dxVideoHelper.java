package org.cocos2dx.lib;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.cocos2dx.lib.Cocos2dxVideoView;

public class Cocos2dxVideoHelper {
    static final int KeyEventBack = 1000;
    private static final int VideoTaskCreate = 0;
    private static final int VideoTaskFullScreen = 12;
    private static final int VideoTaskKeepRatio = 11;
    private static final int VideoTaskPause = 5;
    private static final int VideoTaskRemove = 1;
    private static final int VideoTaskRestart = 10;
    private static final int VideoTaskResume = 6;
    private static final int VideoTaskSeek = 8;
    private static final int VideoTaskSetRect = 3;
    private static final int VideoTaskSetSource = 2;
    private static final int VideoTaskSetVisible = 9;
    private static final int VideoTaskSetVolume = 13;
    private static final int VideoTaskStart = 4;
    private static final int VideoTaskStop = 7;
    static e mVideoHandler;
    private static Handler sHandler;
    private static SparseArray sVideoViews;
    private static int videoTag;
    private Cocos2dxActivity mActivity;
    private FrameLayout mLayout;
    Cocos2dxVideoView.OnVideoEventListener videoEventListener = new a();

    class a implements Cocos2dxVideoView.OnVideoEventListener {
        a() {
        }

        @Override
        public void onVideoEvent(int i, int i2) {
            Cocos2dxVideoHelper.access$1300(Cocos2dxVideoHelper.this).runOnGLThread(Cocos2dxVideoHelper.this.new d(i, i2));
        }
    }

    class b implements Callable {
        final int a;

        b(int i) {
            this.a = i;
        }

        @Override
        public Float call() {
            return new Float(((Cocos2dxVideoView) Cocos2dxVideoHelper.access$1400().get(this.a)) != null ? r0.getCurrentPosition() / 1000.0f : -1.0f);
        }
    }

    class c implements Callable {
        final int a;

        c(int i) {
            this.a = i;
        }

        @Override
        public Float call() {
            float duration = ((Cocos2dxVideoView) Cocos2dxVideoHelper.access$1400().get(this.a)) != null ? r0.getDuration() / 1000.0f : -1.0f;
            if (duration <= 0.0f) {
                Log.w("Cocos2dxVideoHelper", "Video player's duration is not ready to get now!");
            }
            return new Float(duration);
        }
    }

    private class d implements Runnable {
        private int i;
        private int j;

        public d(int i, int i2) {
            this.i = i;
            this.j = i2;
        }

        @Override
        public void run() {
            Cocos2dxVideoHelper.nativeExecuteVideoCallback(this.i, this.j);
        }
    }

    static class e extends Handler {
        WeakReference a;

        e(Cocos2dxVideoHelper cocos2dxVideoHelper) {
            this.a = new WeakReference(cocos2dxVideoHelper);
        }

        @Override
        public void handleMessage(Message message) {
            Cocos2dxVideoHelper cocos2dxVideoHelper = (Cocos2dxVideoHelper) this.a.get();
            int i = message.what;
            if (i == 0) {
                Cocos2dxVideoHelper.access$000(cocos2dxVideoHelper, message.arg1);
            } else if (i == Cocos2dxVideoHelper.VideoTaskRemove) {
                Cocos2dxVideoHelper.access$100(cocos2dxVideoHelper, message.arg1);
            } else if (i == Cocos2dxVideoHelper.VideoTaskSetSource) {
                Cocos2dxVideoHelper.access$200(cocos2dxVideoHelper, message.arg1, message.arg2, (String) message.obj);
            } else if (i == Cocos2dxVideoHelper.VideoTaskSetRect) {
                Rect rect = (Rect) message.obj;
                Cocos2dxVideoHelper.access$400(cocos2dxVideoHelper, message.arg1, rect.left, rect.top, rect.right, rect.bottom);
            } else if (i == Cocos2dxVideoHelper.VideoTaskStart) {
                Cocos2dxVideoHelper.access$300(cocos2dxVideoHelper, message.arg1);
            } else if (i == Cocos2dxVideoHelper.VideoTaskPause) {
                Cocos2dxVideoHelper.access$600(cocos2dxVideoHelper, message.arg1);
            } else if (i == Cocos2dxVideoHelper.VideoTaskStop) {
                Cocos2dxVideoHelper.access$700(cocos2dxVideoHelper, message.arg1);
            } else if (i == Cocos2dxVideoHelper.VideoTaskSeek) {
                Cocos2dxVideoHelper.access$800(cocos2dxVideoHelper, message.arg1, message.arg2);
            } else if (i != Cocos2dxVideoHelper.VideoTaskSetVisible) {
                if (i != Cocos2dxVideoHelper.KeyEventBack) {
                    switch (i) {
                        case Cocos2dxVideoHelper.VideoTaskKeepRatio:
                            if (message.arg2 != Cocos2dxVideoHelper.VideoTaskRemove) {
                                Cocos2dxVideoHelper.access$1000(cocos2dxVideoHelper, message.arg1, false);
                            } else {
                                Cocos2dxVideoHelper.access$1000(cocos2dxVideoHelper, message.arg1, true);
                            }
                            break;
                        case Cocos2dxVideoHelper.VideoTaskFullScreen:
                            if (message.arg2 != Cocos2dxVideoHelper.VideoTaskRemove) {
                                Cocos2dxVideoHelper.access$500(cocos2dxVideoHelper, message.arg1, false);
                            } else {
                                Cocos2dxVideoHelper.access$500(cocos2dxVideoHelper, message.arg1, true);
                            }
                            break;
                        case Cocos2dxVideoHelper.VideoTaskSetVolume:
                            Cocos2dxVideoHelper.access$1200(cocos2dxVideoHelper, message.arg1, message.arg2 / 10.0f);
                            break;
                    }
                } else {
                    Cocos2dxVideoHelper.access$1100(cocos2dxVideoHelper);
                }
            } else if (message.arg2 == Cocos2dxVideoHelper.VideoTaskRemove) {
                Cocos2dxVideoHelper.access$900(cocos2dxVideoHelper, message.arg1, true);
            } else {
                Cocos2dxVideoHelper.access$900(cocos2dxVideoHelper, message.arg1, false);
            }
            super.handleMessage(message);
        }
    }

    Cocos2dxVideoHelper(Cocos2dxActivity cocos2dxActivity, FrameLayout frameLayout) {
        this.mLayout = null;
        this.mActivity = null;
        this.mActivity = cocos2dxActivity;
        this.mLayout = frameLayout;
        mVideoHandler = new e(this);
        sVideoViews = new SparseArray();
        sHandler = new Handler(Looper.myLooper());
    }

    private void _createVideoView(int i) {
        Cocos2dxVideoView cocos2dxVideoView = new Cocos2dxVideoView(this.mActivity, i);
        sVideoViews.put(i, cocos2dxVideoView);
        this.mLayout.addView(cocos2dxVideoView, new FrameLayout.LayoutParams(-2, -2));
        cocos2dxVideoView.setZOrderOnTop(true);
        cocos2dxVideoView.setVideoViewEventListener(this.videoEventListener);
    }

    private void _pauseVideo(int i) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.pause();
        }
    }

    private void _removeVideoView(int i) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.stopPlayback();
            sVideoViews.remove(i);
            this.mLayout.removeView(cocos2dxVideoView);
        }
    }

    private void _seekVideoTo(int i, int i2) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.seekTo(i2);
        }
    }

    private void _setFullScreenEnabled(int i, boolean z) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.setFullScreenEnabled(z);
        }
    }

    private void _setVideoKeepRatio(int i, boolean z) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.setKeepRatio(z);
        }
    }

    private void _setVideoRect(int i, int i2, int i3, int i4, int i5) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.setVideoRect(i2, i3, i4, i5);
        }
    }

    private void _setVideoURL(int i, int i2, String str) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            if (i2 == 0) {
                cocos2dxVideoView.setVideoFileName(str);
            } else {
                if (i2 != VideoTaskRemove) {
                    return;
                }
                cocos2dxVideoView.setVideoURL(str);
            }
        }
    }

    private void _setVideoVisible(int i, boolean z) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            if (!z) {
                cocos2dxVideoView.setVisibility(VideoTaskStart);
            } else {
                cocos2dxVideoView.fixSize();
                cocos2dxVideoView.setVisibility(VideoTaskCreate);
            }
        }
    }

    private void _setVolume(int i, float f) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.setVolume(f);
        }
    }

    private void _startVideo(int i) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.start();
        }
    }

    private void _stopVideo(int i) {
        Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(i);
        if (cocos2dxVideoView != null) {
            cocos2dxVideoView.stop();
        }
    }

    static void access$000(Cocos2dxVideoHelper cocos2dxVideoHelper, int i) {
        cocos2dxVideoHelper._createVideoView(i);
    }

    static void access$100(Cocos2dxVideoHelper cocos2dxVideoHelper, int i) {
        cocos2dxVideoHelper._removeVideoView(i);
    }

    static void access$1000(Cocos2dxVideoHelper cocos2dxVideoHelper, int i, boolean z) {
        cocos2dxVideoHelper._setVideoKeepRatio(i, z);
    }

    static void access$1100(Cocos2dxVideoHelper cocos2dxVideoHelper) {
        cocos2dxVideoHelper.onBackKeyEvent();
    }

    static void access$1200(Cocos2dxVideoHelper cocos2dxVideoHelper, int i, float f) {
        cocos2dxVideoHelper._setVolume(i, f);
    }

    static Cocos2dxActivity access$1300(Cocos2dxVideoHelper cocos2dxVideoHelper) {
        return cocos2dxVideoHelper.mActivity;
    }

    static SparseArray access$1400() {
        return sVideoViews;
    }

    static void access$200(Cocos2dxVideoHelper cocos2dxVideoHelper, int i, int i2, String str) {
        cocos2dxVideoHelper._setVideoURL(i, i2, str);
    }

    static void access$300(Cocos2dxVideoHelper cocos2dxVideoHelper, int i) {
        cocos2dxVideoHelper._startVideo(i);
    }

    static void access$400(Cocos2dxVideoHelper cocos2dxVideoHelper, int i, int i2, int i3, int i4, int i5) {
        cocos2dxVideoHelper._setVideoRect(i, i2, i3, i4, i5);
    }

    static void access$500(Cocos2dxVideoHelper cocos2dxVideoHelper, int i, boolean z) {
        cocos2dxVideoHelper._setFullScreenEnabled(i, z);
    }

    static void access$600(Cocos2dxVideoHelper cocos2dxVideoHelper, int i) {
        cocos2dxVideoHelper._pauseVideo(i);
    }

    static void access$700(Cocos2dxVideoHelper cocos2dxVideoHelper, int i) {
        cocos2dxVideoHelper._stopVideo(i);
    }

    static void access$800(Cocos2dxVideoHelper cocos2dxVideoHelper, int i, int i2) {
        cocos2dxVideoHelper._seekVideoTo(i, i2);
    }

    static void access$900(Cocos2dxVideoHelper cocos2dxVideoHelper, int i, boolean z) {
        cocos2dxVideoHelper._setVideoVisible(i, z);
    }

    public static <T> Object callInMainThread(Callable callable) {
        FutureTask futureTask = new FutureTask(callable);
        sHandler.post(futureTask);
        return futureTask.get();
    }

    public static int createVideoWidget() {
        Message message = new Message();
        message.what = VideoTaskCreate;
        message.arg1 = videoTag;
        mVideoHandler.sendMessage(message);
        int i = videoTag;
        videoTag = i + VideoTaskRemove;
        return i;
    }

    public static float getCurrentTime(int i) {
        try {
            return ((Float) callInMainThread(new b(i))).floatValue();
        } catch (InterruptedException | ExecutionException unused) {
            return -1.0f;
        }
    }

    public static float getDuration(int i) {
        try {
            return ((Float) callInMainThread(new c(i))).floatValue();
        } catch (InterruptedException | ExecutionException unused) {
            return -1.0f;
        }
    }

    public static native void nativeExecuteVideoCallback(int i, int i2);

    private void onBackKeyEvent() {
        int size = sVideoViews.size();
        for (int i = VideoTaskCreate; i < size; i += VideoTaskRemove) {
            int iKeyAt = sVideoViews.keyAt(i);
            Cocos2dxVideoView cocos2dxVideoView = (Cocos2dxVideoView) sVideoViews.get(iKeyAt);
            if (cocos2dxVideoView != null) {
                cocos2dxVideoView.setFullScreenEnabled(false);
                this.mActivity.runOnGLThread(new d(iKeyAt, KeyEventBack));
            }
        }
    }

    public static void pauseVideo(int i) {
        Message message = new Message();
        message.what = VideoTaskPause;
        message.arg1 = i;
        mVideoHandler.sendMessage(message);
    }

    public static void removeVideoWidget(int i) {
        Message message = new Message();
        message.what = VideoTaskRemove;
        message.arg1 = i;
        mVideoHandler.sendMessage(message);
    }

    public static void seekVideoTo(int i, int i2) {
        Message message = new Message();
        message.what = VideoTaskSeek;
        message.arg1 = i;
        message.arg2 = i2;
        mVideoHandler.sendMessage(message);
    }

    public static void setFullScreenEnabled(int i, boolean z) {
        Message message = new Message();
        message.what = VideoTaskFullScreen;
        message.arg1 = i;
        if (z) {
            message.arg2 = VideoTaskRemove;
        } else {
            message.arg2 = VideoTaskCreate;
        }
        mVideoHandler.sendMessage(message);
    }

    public static void setVideoKeepRatioEnabled(int i, boolean z) {
        Message message = new Message();
        message.what = VideoTaskKeepRatio;
        message.arg1 = i;
        if (z) {
            message.arg2 = VideoTaskRemove;
        } else {
            message.arg2 = VideoTaskCreate;
        }
        mVideoHandler.sendMessage(message);
    }

    public static void setVideoRect(int i, int i2, int i3, int i4, int i5) {
        Message message = new Message();
        message.what = VideoTaskSetRect;
        message.arg1 = i;
        message.obj = new Rect(i2, i3, i4, i5);
        mVideoHandler.sendMessage(message);
    }

    public static void setVideoUrl(int i, int i2, String str) {
        Message message = new Message();
        message.what = VideoTaskSetSource;
        message.arg1 = i;
        message.arg2 = i2;
        message.obj = str;
        mVideoHandler.sendMessage(message);
    }

    public static void setVideoVisible(int i, boolean z) {
        Message message = new Message();
        message.what = VideoTaskSetVisible;
        message.arg1 = i;
        if (z) {
            message.arg2 = VideoTaskRemove;
        } else {
            message.arg2 = VideoTaskCreate;
        }
        mVideoHandler.sendMessage(message);
    }

    public static void setVolume(int i, float f) {
        Message message = new Message();
        message.what = VideoTaskSetVolume;
        message.arg1 = i;
        message.arg2 = (int) (f * 10.0f);
        mVideoHandler.sendMessage(message);
    }

    public static void startVideo(int i) {
        Message message = new Message();
        message.what = VideoTaskStart;
        message.arg1 = i;
        mVideoHandler.sendMessage(message);
    }

    public static void stopVideo(int i) {
        Message message = new Message();
        message.what = VideoTaskStop;
        message.arg1 = i;
        mVideoHandler.sendMessage(message);
    }
}
