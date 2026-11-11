package org.cocos2dx.lib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Cocos2dxGLSurfaceView extends GLSurfaceView {
    private static final int HANDLER_CLOSE_IME_KEYBOARD = 3;
    private static final int HANDLER_OPEN_IME_KEYBOARD = 2;
    private static final String TAG = "Cocos2dxGLSurfaceView";
    private static Cocos2dxGLSurfaceView mCocos2dxGLSurfaceView;
    private static Handler sHandler;
    private Cocos2dxRenderer mCocos2dxRenderer;
    private boolean mStopHandleTouchAndKeyEvents;

    class a implements Runnable {
        final int i;

        a(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleKeyDown(this.i);
        }
    }

    class b implements Runnable {
        final int i;

        b(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleKeyUp(this.i);
        }
    }

    class c implements Runnable {
        final float i;
        final float j;
        final float k;
        final long l;

        c(float f, float f2, float f3, long j) {
            this.i = f;
            this.j = f2;
            this.k = f3;
            this.l = j;
        }

        @Override
        public void run() {
            Cocos2dxAccelerometer.onSensorChanged(this.i, this.j, this.k, this.l);
        }
    }

    class d implements Runnable {
        d() {
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleOnResume();
        }
    }

    class e implements Runnable {
        e() {
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleOnPause();
        }
    }

    class f implements Runnable {
        final int i;
        final float j;
        final float k;

        f(int i, float f, float f2) {
            this.i = i;
            this.j = f;
            this.k = f2;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleActionDown(this.i, this.j, this.k);
        }
    }

    class g implements Runnable {
        final int i;
        final float j;
        final float k;

        g(int i, float f, float f2) {
            this.i = i;
            this.j = f;
            this.k = f2;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleActionDown(this.i, this.j, this.k);
        }
    }

    class h implements Runnable {
        final int[] i;
        final float[] j;
        final float[] k;

        h(int[] iArr, float[] fArr, float[] fArr2) {
            this.i = iArr;
            this.j = fArr;
            this.k = fArr2;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleActionMove(this.i, this.j, this.k);
        }
    }

    class i implements Runnable {
        final int i;
        final float j;
        final float k;

        i(int i, float f, float f2) {
            this.i = i;
            this.j = f;
            this.k = f2;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleActionUp(this.i, this.j, this.k);
        }
    }

    class j implements Runnable {
        final int i;
        final float j;
        final float k;

        j(int i, float f, float f2) {
            this.i = i;
            this.j = f;
            this.k = f2;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleActionUp(this.i, this.j, this.k);
        }
    }

    class k implements Runnable {
        final int[] i;
        final float[] j;
        final float[] k;

        k(int[] iArr, float[] fArr, float[] fArr2) {
            this.i = iArr;
            this.j = fArr;
            this.k = fArr2;
        }

        @Override
        public void run() {
            Cocos2dxGLSurfaceView.access$000(Cocos2dxGLSurfaceView.this).handleActionCancel(this.i, this.j, this.k);
        }
    }

    public Cocos2dxGLSurfaceView(Context context) {
        super(context);
        this.mStopHandleTouchAndKeyEvents = false;
        initView();
    }

    static Cocos2dxRenderer access$000(Cocos2dxGLSurfaceView cocos2dxGLSurfaceView) {
        return cocos2dxGLSurfaceView.mCocos2dxRenderer;
    }

    private static void dumpMotionEvent(MotionEvent motionEvent) {
        StringBuilder sb = new StringBuilder();
        int action = motionEvent.getAction();
        int i2 = action & 255;
        sb.append("event ACTION_");
        sb.append(new String[]{"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"}[i2]);
        if (i2 == 5 || i2 == 6) {
            sb.append("(pid ");
            sb.append(action >> 8);
            sb.append(")");
        }
        sb.append("[");
        int i3 = 0;
        while (i3 < motionEvent.getPointerCount()) {
            sb.append("#");
            sb.append(i3);
            sb.append("(pid ");
            sb.append(motionEvent.getPointerId(i3));
            sb.append(")=");
            sb.append((int) motionEvent.getX(i3));
            sb.append(",");
            sb.append((int) motionEvent.getY(i3));
            i3++;
            if (i3 < motionEvent.getPointerCount()) {
                sb.append(";");
            }
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    private String getContentText() {
        return this.mCocos2dxRenderer.getContentText();
    }

    public static Cocos2dxGLSurfaceView getInstance() {
        return mCocos2dxGLSurfaceView;
    }

    public static native void nativeOnSizeChanged(int i2, int i3);

    public static void queueAccelerometer(float f2, float f3, float f4, long j2) {
        mCocos2dxGLSurfaceView.queueEvent(new c(f2, f3, f4, j2));
    }

    protected void initView() {
        setEGLContextClientVersion(HANDLER_OPEN_IME_KEYBOARD);
        setFocusableInTouchMode(true);
        mCocos2dxGLSurfaceView = this;
    }

    @Override
    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        if (i2 == 4) {
            Cocos2dxVideoHelper.mVideoHandler.sendEmptyMessage(1000);
        } else if (i2 != 66 && i2 != 82 && i2 != 85) {
            switch (i2) {
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    break;
                default:
                    return super.onKeyDown(i2, keyEvent);
            }
        }
        queueEvent(new a(i2));
        return true;
    }

    @Override
    public boolean onKeyUp(int i2, KeyEvent keyEvent) {
        if (i2 != 4 && i2 != 66 && i2 != 82 && i2 != 85) {
            switch (i2) {
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    break;
                default:
                    return super.onKeyUp(i2, keyEvent);
            }
        }
        queueEvent(new b(i2));
        return true;
    }

    @Override
    public void onPause() {
        queueEvent(new e());
        setRenderMode(0);
        this.mCocos2dxRenderer.setPauseInMainThread(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setRenderMode(1);
        queueEvent(new d());
        this.mCocos2dxRenderer.setPauseInMainThread(false);
    }

    @Override
    protected void onSizeChanged(int i2, int i3, int i4, int i5) {
        if (isInEditMode()) {
            return;
        }
        this.mCocos2dxRenderer.setScreenWidthAndHeight(i2, i3);
        nativeOnSizeChanged(i2, i3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        int[] iArr = new int[pointerCount];
        float[] fArr = new float[pointerCount];
        float[] fArr2 = new float[pointerCount];
        for (int i2 = 0; i2 < pointerCount; i2++) {
            iArr[i2] = motionEvent.getPointerId(i2);
            fArr[i2] = motionEvent.getX(i2);
            fArr2[i2] = motionEvent.getY(i2);
        }
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            if (this.mStopHandleTouchAndKeyEvents) {
                Cocos2dxEditBox.complete();
                return true;
            }
            queueEvent(new g(motionEvent.getPointerId(0), fArr[0], fArr2[0]));
        } else if (action == 1) {
            queueEvent(new j(motionEvent.getPointerId(0), fArr[0], fArr2[0]));
        } else if (action == HANDLER_OPEN_IME_KEYBOARD) {
            queueEvent(new h(iArr, fArr, fArr2));
        } else if (action == HANDLER_CLOSE_IME_KEYBOARD) {
            queueEvent(new k(iArr, fArr, fArr2));
        } else if (action == 5) {
            if (this.mStopHandleTouchAndKeyEvents) {
                Cocos2dxEditBox.complete();
                return true;
            }
            int action2 = motionEvent.getAction() >> 8;
            queueEvent(new f(motionEvent.getPointerId(action2), motionEvent.getX(action2), motionEvent.getY(action2)));
        } else if (action == 6) {
            int action3 = motionEvent.getAction() >> 8;
            queueEvent(new i(motionEvent.getPointerId(action3), motionEvent.getX(action3), motionEvent.getY(action3)));
        }
        return true;
    }

    public void setCocos2dxRenderer(Cocos2dxRenderer cocos2dxRenderer) {
        this.mCocos2dxRenderer = cocos2dxRenderer;
        setRenderer(cocos2dxRenderer);
    }

    public void setStopHandleTouchAndKeyEvents(boolean z) {
        this.mStopHandleTouchAndKeyEvents = z;
    }

    public Cocos2dxGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mStopHandleTouchAndKeyEvents = false;
        initView();
    }
}
