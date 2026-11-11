package org.cocos2dx.lib;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import java.io.IOException;
import java.util.Map;

public class Cocos2dxVideoView extends SurfaceView {
    private static final String AssetResourceRoot = "@assets/";
    private static final int EVENT_CLICKED = 5;
    private static final int EVENT_COMPLETED = 3;
    private static final int EVENT_META_LOADED = 4;
    private static final int EVENT_PAUSED = 1;
    private static final int EVENT_PLAYING = 0;
    private static final int EVENT_READY_TO_PLAY = 6;
    private static final int EVENT_STOPPED = 2;
    private String TAG;
    protected Cocos2dxActivity mCocos2dxActivity;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private e mCurrentState;
    private int mDuration;
    private MediaPlayer.OnErrorListener mErrorListener;
    protected boolean mFullScreenEnabled;
    protected int mFullScreenHeight;
    protected int mFullScreenWidth;
    private boolean mIsAssetRouse;
    private boolean mKeepRatio;
    private MediaPlayer mMediaPlayer;
    private boolean mMetaUpdated;
    private OnVideoEventListener mOnVideoEventListener;
    private int mPositionBeforeRelease;
    MediaPlayer.OnPreparedListener mPreparedListener;
    SurfaceHolder.Callback mSHCallback;
    private int mSeekWhenPrepared;
    private SurfaceHolder mSurfaceHolder;
    private String mVideoFilePath;
    private int mVideoHeight;
    private Uri mVideoUri;
    private int mVideoWidth;
    protected int mViewHeight;
    protected int mViewLeft;
    private int mViewTag;
    protected int mViewTop;
    protected int mViewWidth;
    protected int mVisibleHeight;
    protected int mVisibleLeft;
    protected int mVisibleTop;
    protected int mVisibleWidth;

    public interface OnVideoEventListener {
        void onVideoEvent(int i, int i2);
    }

    class a implements MediaPlayer.OnPreparedListener {
        a() {
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Cocos2dxVideoView.access$002(Cocos2dxVideoView.this, mediaPlayer.getVideoWidth());
            Cocos2dxVideoView.access$102(Cocos2dxVideoView.this, mediaPlayer.getVideoHeight());
            if (Cocos2dxVideoView.access$000(Cocos2dxVideoView.this) != 0 && Cocos2dxVideoView.access$100(Cocos2dxVideoView.this) != 0) {
                Cocos2dxVideoView.this.fixSize();
            }
            if (!Cocos2dxVideoView.access$200(Cocos2dxVideoView.this)) {
                Cocos2dxVideoView.access$300(Cocos2dxVideoView.this, Cocos2dxVideoView.EVENT_META_LOADED);
                Cocos2dxVideoView.access$300(Cocos2dxVideoView.this, Cocos2dxVideoView.EVENT_READY_TO_PLAY);
                Cocos2dxVideoView.access$202(Cocos2dxVideoView.this, true);
            }
            Cocos2dxVideoView.access$402(Cocos2dxVideoView.this, e.PREPARED);
        }
    }

    class b implements MediaPlayer.OnCompletionListener {
        b() {
        }

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Cocos2dxVideoView.access$402(Cocos2dxVideoView.this, e.PLAYBACK_COMPLETED);
            Cocos2dxVideoView.access$300(Cocos2dxVideoView.this, Cocos2dxVideoView.EVENT_COMPLETED);
        }
    }

    class c implements MediaPlayer.OnErrorListener {

        class a implements DialogInterface.OnClickListener {
            a() {
            }

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Cocos2dxVideoView.access$300(Cocos2dxVideoView.this, Cocos2dxVideoView.EVENT_COMPLETED);
            }
        }

        c() {
        }

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
            Log.d(Cocos2dxVideoView.access$500(Cocos2dxVideoView.this), "Error: " + i + "," + i2);
            Cocos2dxVideoView.access$402(Cocos2dxVideoView.this, e.ERROR);
            if (Cocos2dxVideoView.this.getWindowToken() == null) {
                return true;
            }
            Resources resources = Cocos2dxVideoView.this.mCocos2dxActivity.getResources();
            new AlertDialog.Builder(Cocos2dxVideoView.this.mCocos2dxActivity).setTitle(resources.getString(resources.getIdentifier("VideoView_error_title", "string", "android"))).setMessage(i == 200 ? resources.getIdentifier("VideoView_error_text_invalid_progressive_playback", "string", "android") : resources.getIdentifier("VideoView_error_text_unknown", "string", "android")).setPositiveButton(resources.getString(resources.getIdentifier("VideoView_error_button", "string", "android")), new a()).setCancelable(false).show();
            return true;
        }
    }

    class d implements SurfaceHolder.Callback {
        d() {
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Cocos2dxVideoView.access$602(Cocos2dxVideoView.this, surfaceHolder);
            Cocos2dxVideoView.access$700(Cocos2dxVideoView.this);
            if (Cocos2dxVideoView.access$800(Cocos2dxVideoView.this) > 0) {
                Cocos2dxVideoView.access$900(Cocos2dxVideoView.this).seekTo(Cocos2dxVideoView.access$800(Cocos2dxVideoView.this));
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Cocos2dxVideoView.access$602(Cocos2dxVideoView.this, null);
            Cocos2dxVideoView cocos2dxVideoView = Cocos2dxVideoView.this;
            Cocos2dxVideoView.access$802(cocos2dxVideoView, cocos2dxVideoView.getCurrentPosition());
            Cocos2dxVideoView.access$1000(Cocos2dxVideoView.this);
        }
    }

    private enum e {
        IDLE,
        ERROR,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        PLAYBACK_COMPLETED
    }

    public Cocos2dxVideoView(Cocos2dxActivity cocos2dxActivity, int i) {
        super(cocos2dxActivity);
        this.TAG = "Cocos2dxVideoView";
        this.mCurrentState = e.IDLE;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mVideoWidth = EVENT_PLAYING;
        this.mVideoHeight = EVENT_PLAYING;
        this.mSeekWhenPrepared = EVENT_PLAYING;
        this.mCocos2dxActivity = null;
        this.mViewLeft = EVENT_PLAYING;
        this.mViewTop = EVENT_PLAYING;
        this.mViewWidth = EVENT_PLAYING;
        this.mViewHeight = EVENT_PLAYING;
        this.mVisibleLeft = EVENT_PLAYING;
        this.mVisibleTop = EVENT_PLAYING;
        this.mVisibleWidth = EVENT_PLAYING;
        this.mVisibleHeight = EVENT_PLAYING;
        this.mFullScreenEnabled = false;
        this.mFullScreenWidth = EVENT_PLAYING;
        this.mFullScreenHeight = EVENT_PLAYING;
        this.mIsAssetRouse = false;
        this.mVideoFilePath = null;
        this.mViewTag = EVENT_PLAYING;
        this.mKeepRatio = false;
        this.mMetaUpdated = false;
        this.mPositionBeforeRelease = EVENT_PLAYING;
        this.mPreparedListener = new a();
        this.mCompletionListener = new b();
        this.mErrorListener = new c();
        this.mSHCallback = new d();
        this.mViewTag = i;
        this.mCocos2dxActivity = cocos2dxActivity;
        initVideoView();
    }

    static int access$000(Cocos2dxVideoView cocos2dxVideoView) {
        return cocos2dxVideoView.mVideoWidth;
    }

    static int access$002(Cocos2dxVideoView cocos2dxVideoView, int i) {
        cocos2dxVideoView.mVideoWidth = i;
        return i;
    }

    static int access$100(Cocos2dxVideoView cocos2dxVideoView) {
        return cocos2dxVideoView.mVideoHeight;
    }

    static void access$1000(Cocos2dxVideoView cocos2dxVideoView) {
        cocos2dxVideoView.release();
    }

    static int access$102(Cocos2dxVideoView cocos2dxVideoView, int i) {
        cocos2dxVideoView.mVideoHeight = i;
        return i;
    }

    static boolean access$200(Cocos2dxVideoView cocos2dxVideoView) {
        return cocos2dxVideoView.mMetaUpdated;
    }

    static boolean access$202(Cocos2dxVideoView cocos2dxVideoView, boolean z) {
        cocos2dxVideoView.mMetaUpdated = z;
        return z;
    }

    static void access$300(Cocos2dxVideoView cocos2dxVideoView, int i) {
        cocos2dxVideoView.sendEvent(i);
    }

    static e access$402(Cocos2dxVideoView cocos2dxVideoView, e eVar) {
        cocos2dxVideoView.mCurrentState = eVar;
        return eVar;
    }

    static String access$500(Cocos2dxVideoView cocos2dxVideoView) {
        return cocos2dxVideoView.TAG;
    }

    static SurfaceHolder access$602(Cocos2dxVideoView cocos2dxVideoView, SurfaceHolder surfaceHolder) {
        cocos2dxVideoView.mSurfaceHolder = surfaceHolder;
        return surfaceHolder;
    }

    static void access$700(Cocos2dxVideoView cocos2dxVideoView) {
        cocos2dxVideoView.openVideo();
    }

    static int access$800(Cocos2dxVideoView cocos2dxVideoView) {
        return cocos2dxVideoView.mPositionBeforeRelease;
    }

    static int access$802(Cocos2dxVideoView cocos2dxVideoView, int i) {
        cocos2dxVideoView.mPositionBeforeRelease = i;
        return i;
    }

    static MediaPlayer access$900(Cocos2dxVideoView cocos2dxVideoView) {
        return cocos2dxVideoView.mMediaPlayer;
    }

    private void initVideoView() {
        this.mVideoWidth = EVENT_PLAYING;
        this.mVideoHeight = EVENT_PLAYING;
        getHolder().addCallback(this.mSHCallback);
        getHolder().setType(EVENT_COMPLETED);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.mCurrentState = e.IDLE;
    }

    private void openVideo() {
        if (this.mSurfaceHolder == null) {
            return;
        }
        if (this.mIsAssetRouse) {
            if (this.mVideoFilePath == null) {
                return;
            }
        } else if (this.mVideoUri == null) {
            return;
        }
        pausePlaybackService();
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mMediaPlayer = mediaPlayer;
            mediaPlayer.setOnPreparedListener(this.mPreparedListener);
            this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
            this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
            this.mMediaPlayer.setAudioStreamType(EVENT_COMPLETED);
            this.mMediaPlayer.setScreenOnWhilePlaying(true);
            if (this.mIsAssetRouse) {
                AssetFileDescriptor assetFileDescriptorOpenFd = this.mCocos2dxActivity.getAssets().openFd(this.mVideoFilePath);
                this.mMediaPlayer.setDataSource(assetFileDescriptorOpenFd.getFileDescriptor(), assetFileDescriptorOpenFd.getStartOffset(), assetFileDescriptorOpenFd.getLength());
            } else {
                this.mMediaPlayer.setDataSource(this.mVideoUri.toString());
            }
            this.mCurrentState = e.INITIALIZED;
            this.mMediaPlayer.prepare();
            showFirstFrame();
        } catch (IOException e2) {
            Log.w(this.TAG, "Unable to open content: " + this.mVideoUri, e2);
            this.mCurrentState = e.ERROR;
            this.mErrorListener.onError(this.mMediaPlayer, EVENT_PAUSED, EVENT_PLAYING);
        } catch (IllegalArgumentException e3) {
            Log.w(this.TAG, "Unable to open content: " + this.mVideoUri, e3);
            this.mCurrentState = e.ERROR;
            this.mErrorListener.onError(this.mMediaPlayer, EVENT_PAUSED, EVENT_PLAYING);
        }
    }

    private void pausePlaybackService() {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        this.mCocos2dxActivity.sendBroadcast(intent);
    }

    private void release() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    private void sendEvent(int i) {
        OnVideoEventListener onVideoEventListener = this.mOnVideoEventListener;
        if (onVideoEventListener != null) {
            onVideoEventListener.onVideoEvent(this.mViewTag, i);
        }
    }

    private void setVideoURI(Uri uri, Map map) {
        this.mVideoUri = uri;
        this.mVideoWidth = EVENT_PLAYING;
        this.mVideoHeight = EVENT_PLAYING;
    }

    private void showFirstFrame() {
        this.mMediaPlayer.seekTo(EVENT_PAUSED);
    }

    public void fixSize() {
        if (!this.mFullScreenEnabled) {
            fixSize(this.mViewLeft, this.mViewTop, this.mViewWidth, this.mViewHeight);
            return;
        }
        this.mFullScreenWidth = this.mCocos2dxActivity.getGLSurfaceView().getWidth();
        int height = this.mCocos2dxActivity.getGLSurfaceView().getHeight();
        this.mFullScreenHeight = height;
        fixSize(EVENT_PLAYING, EVENT_PLAYING, this.mFullScreenWidth, height);
    }

    public int getCurrentPosition() {
        boolean z = this.mCurrentState == e.ERROR;
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (!z && !(mediaPlayer == null)) {
            return mediaPlayer.getCurrentPosition();
        }
        return -1;
    }

    public int getDuration() {
        MediaPlayer mediaPlayer;
        e eVar = this.mCurrentState;
        if (eVar != e.IDLE && eVar != e.ERROR && eVar != e.INITIALIZED && (mediaPlayer = this.mMediaPlayer) != null) {
            this.mDuration = mediaPlayer.getDuration();
        }
        return this.mDuration;
    }

    @Override
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(this.mVisibleWidth, this.mVisibleHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if ((motionEvent.getAction() & 255) == EVENT_PAUSED) {
            sendEvent(EVENT_CLICKED);
        }
        return true;
    }

    public void pause() {
        MediaPlayer mediaPlayer;
        e eVar = this.mCurrentState;
        if ((eVar == e.STARTED || eVar == e.PLAYBACK_COMPLETED) && (mediaPlayer = this.mMediaPlayer) != null) {
            this.mCurrentState = e.PAUSED;
            mediaPlayer.pause();
            sendEvent(EVENT_PAUSED);
        }
    }

    public int resolveAdjustedSize(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        return mode != Integer.MIN_VALUE ? mode != 1073741824 ? i : size : Math.min(i, size);
    }

    public void seekTo(int i) {
        MediaPlayer mediaPlayer;
        e eVar = this.mCurrentState;
        if (eVar == e.IDLE || eVar == e.INITIALIZED || eVar == e.STOPPED || eVar == e.ERROR || (mediaPlayer = this.mMediaPlayer) == null) {
            return;
        }
        mediaPlayer.seekTo(i);
    }

    public void setFullScreenEnabled(boolean z) {
        if (this.mFullScreenEnabled != z) {
            this.mFullScreenEnabled = z;
            fixSize();
        }
    }

    public void setKeepRatio(boolean z) {
        this.mKeepRatio = z;
        fixSize();
    }

    public void setVideoFileName(String str) {
        if (str.startsWith(AssetResourceRoot)) {
            str = str.substring(8);
        }
        if (str.startsWith("/")) {
            this.mIsAssetRouse = false;
            setVideoURI(Uri.parse(str), null);
        } else {
            this.mVideoFilePath = str;
            this.mIsAssetRouse = true;
            setVideoURI(Uri.parse(str), null);
        }
    }

    public void setVideoRect(int i, int i2, int i3, int i4) {
        if (this.mViewLeft == i && this.mViewTop == i2 && this.mViewWidth == i3 && this.mViewHeight == i4) {
            return;
        }
        this.mViewLeft = i;
        this.mViewTop = i2;
        this.mViewWidth = i3;
        this.mViewHeight = i4;
        fixSize(i, i2, i3, i4);
    }

    public void setVideoURL(String str) {
        this.mIsAssetRouse = false;
        setVideoURI(Uri.parse(str), null);
    }

    public void setVideoViewEventListener(OnVideoEventListener onVideoEventListener) {
        this.mOnVideoEventListener = onVideoEventListener;
    }

    @Override
    public void setVisibility(int i) {
        super.setVisibility(i);
    }

    public void setVolume(float f) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(f, f);
        }
    }

    public void start() {
        MediaPlayer mediaPlayer;
        e eVar = this.mCurrentState;
        if ((eVar == e.PREPARED || eVar == e.PAUSED || eVar == e.PLAYBACK_COMPLETED) && (mediaPlayer = this.mMediaPlayer) != null) {
            this.mCurrentState = e.STARTED;
            mediaPlayer.start();
            sendEvent(EVENT_PLAYING);
        }
    }

    public void stop() {
        e eVar;
        MediaPlayer mediaPlayer;
        e eVar2 = this.mCurrentState;
        if (eVar2 == e.IDLE || eVar2 == e.INITIALIZED || eVar2 == e.ERROR || eVar2 == (eVar = e.STOPPED) || (mediaPlayer = this.mMediaPlayer) == null) {
            return;
        }
        this.mCurrentState = eVar;
        mediaPlayer.stop();
        sendEvent(EVENT_STOPPED);
        try {
            this.mMediaPlayer.prepare();
            showFirstFrame();
        } catch (Exception unused) {
        }
    }

    public void stopPlayback() {
        release();
    }

    public void fixSize(int i, int i2, int i3, int i4) {
        int i5;
        int i6 = this.mVideoWidth;
        if (i6 == 0 || (i5 = this.mVideoHeight) == 0) {
            this.mVisibleLeft = i;
            this.mVisibleTop = i2;
            this.mVisibleWidth = i3;
            this.mVisibleHeight = i4;
        } else if (i3 != 0 && i4 != 0) {
            if (this.mKeepRatio && !this.mFullScreenEnabled) {
                if (i6 * i4 > i3 * i5) {
                    this.mVisibleWidth = i3;
                    this.mVisibleHeight = (i5 * i3) / i6;
                } else if (i6 * i4 < i3 * i5) {
                    this.mVisibleWidth = (i6 * i4) / i5;
                    this.mVisibleHeight = i4;
                }
                this.mVisibleLeft = i + ((i3 - this.mVisibleWidth) / EVENT_STOPPED);
                this.mVisibleTop = i2 + ((i4 - this.mVisibleHeight) / EVENT_STOPPED);
            } else {
                this.mVisibleLeft = i;
                this.mVisibleTop = i2;
                this.mVisibleWidth = i3;
                this.mVisibleHeight = i4;
            }
        } else {
            this.mVisibleLeft = i;
            this.mVisibleTop = i2;
            this.mVisibleWidth = i6;
            this.mVisibleHeight = i5;
        }
        getHolder().setFixedSize(this.mVisibleWidth, this.mVisibleHeight);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.leftMargin = this.mVisibleLeft;
        layoutParams.topMargin = this.mVisibleTop;
        setLayoutParams(layoutParams);
    }
}
