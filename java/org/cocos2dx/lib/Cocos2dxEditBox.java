package org.cocos2dx.lib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Cocos2dxEditBox {
    private static final int DARK_GREEN = Color.parseColor("#1fa014");
    private static final int DARK_GREEN_PRESS = Color.parseColor("#008e26");
    private static Cocos2dxEditBox sThis = null;
    private Cocos2dxActivity mActivity;
    private RelativeLayout.LayoutParams mButtonParams;
    private g mEditText = null;
    private Button mButton = null;
    private String mButtonTitle = null;
    private boolean mConfirmHold = true;
    private RelativeLayout mButtonLayout = null;
    private int mEditTextID = 1;
    private int mButtonLayoutID = 2;

    class a implements View.OnClickListener {
        a() {
        }

        @Override
        public void onClick(View view) {
            Cocos2dxEditBox cocos2dxEditBox = Cocos2dxEditBox.this;
            Cocos2dxEditBox.access$1100(cocos2dxEditBox, Cocos2dxEditBox.access$200(cocos2dxEditBox).getText().toString());
            if (Cocos2dxEditBox.access$1200(Cocos2dxEditBox.this)) {
                return;
            }
            Cocos2dxEditBox.access$600(Cocos2dxEditBox.this);
        }
    }

    class b implements Runnable {
        final String i;
        final int j;
        final boolean k;
        final boolean l;
        final String m;
        final String n;

        b(String str, int i, boolean z, boolean z2, String str2, String str3) {
            this.i = str;
            this.j = i;
            this.k = z;
            this.l = z2;
            this.m = str2;
            this.n = str3;
        }

        @Override
        public void run() {
            Cocos2dxEditBox.access$1400(Cocos2dxEditBox.access$1300(), this.i, this.j, this.k, this.l, this.m, this.n);
        }
    }

    class c implements Runnable {
        c() {
        }

        @Override
        public void run() {
            Cocos2dxEditBox.access$600(Cocos2dxEditBox.access$1300());
        }
    }

    class d implements Runnable {
        final String i;

        d(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            Cocos2dxEditBox.access$1500(this.i);
        }
    }

    class e implements Runnable {
        final String i;

        e(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            Cocos2dxEditBox.access$1600(this.i);
        }
    }

    class f implements Runnable {
        final String i;

        f(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            Cocos2dxEditBox.access$1700(this.i);
        }
    }

    class g extends EditText {
        private final String a;
        private boolean b;
        private TextWatcher c;
        private Paint d;
        private int e;
        private float f;
        private boolean g;
        private int h;
        private int i;

        class a implements TextWatcher {
            final Cocos2dxEditBox a;

            a(Cocos2dxEditBox cocos2dxEditBox) {
                this.a = cocos2dxEditBox;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Cocos2dxEditBox.access$100(Cocos2dxEditBox.this, editable.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        }

        class b implements TextView.OnEditorActionListener {
            b() {
            }

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (g.b(g.this)) {
                    return false;
                }
                Cocos2dxEditBox.access$600(Cocos2dxEditBox.this);
                return false;
            }
        }

        class c implements ViewTreeObserver.OnGlobalLayoutListener {
            c() {
            }

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                g.this.getWindowVisibleDisplayFrame(rect);
                if (g.this.getRootView().getHeight() - (rect.bottom - rect.top) > g.c(g.this) / 4) {
                    if (!g.d(g.this)) {
                        g.e(g.this, true);
                    }
                } else if (g.d(g.this)) {
                    g.e(g.this, false);
                    Cocos2dxEditBox.access$600(Cocos2dxEditBox.this);
                }
                if (g.f(g.this) != 0 || rect.bottom == g.this.getRootView().getHeight()) {
                    return;
                }
                g.a(g.this, rect.bottom);
            }
        }

        public g(Cocos2dxActivity cocos2dxActivity) {
            super(cocos2dxActivity);
            this.a = "Cocos2dxEditBox";
            this.b = false;
            this.c = null;
            this.e = Cocos2dxEditBox.access$000();
            this.f = 2.0f;
            this.g = false;
            this.i = 0;
            setBackground(null);
            setTextColor(-16777216);
            this.h = ((WindowManager) cocos2dxActivity.getSystemService("window")).getDefaultDisplay().getHeight();
            Paint paint = new Paint();
            this.d = paint;
            paint.setStrokeWidth(this.f);
            this.d.setStyle(Paint.Style.FILL);
            this.d.setColor(this.e);
            this.c = new a(Cocos2dxEditBox.this);
            i();
        }

        static void a(g gVar, int i) {
            gVar.m(i);
        }

        static boolean b(g gVar) {
            return gVar.b;
        }

        static int c(g gVar) {
            return gVar.h;
        }

        static boolean d(g gVar) {
            return gVar.g;
        }

        static boolean e(g gVar, boolean z) {
            gVar.g = z;
            return z;
        }

        static int f(g gVar) {
            return gVar.i;
        }

        private void g() {
            setOnEditorActionListener(new b());
            addTextChangedListener(this.c);
        }

        private void i() {
            getViewTreeObserver().addOnGlobalLayoutListener(new c());
        }

        private void j() {
            setOnEditorActionListener(null);
            removeTextChangedListener(this.c);
        }

        private void k(String str) {
            if (str.contentEquals("done")) {
                setImeOptions(268435462);
                Cocos2dxEditBox cocos2dxEditBox = Cocos2dxEditBox.this;
                Cocos2dxEditBox.access$302(cocos2dxEditBox, Cocos2dxEditBox.access$400(cocos2dxEditBox).getResources().getString(R.string.done));
                return;
            }
            if (str.contentEquals("next")) {
                setImeOptions(268435461);
                Cocos2dxEditBox cocos2dxEditBox2 = Cocos2dxEditBox.this;
                Cocos2dxEditBox.access$302(cocos2dxEditBox2, Cocos2dxEditBox.access$400(cocos2dxEditBox2).getResources().getString(R.string.next));
                return;
            }
            if (str.contentEquals("search")) {
                setImeOptions(268435459);
                Cocos2dxEditBox cocos2dxEditBox3 = Cocos2dxEditBox.this;
                Cocos2dxEditBox.access$302(cocos2dxEditBox3, Cocos2dxEditBox.access$400(cocos2dxEditBox3).getResources().getString(R.string.search));
                return;
            }
            if (str.contentEquals("go")) {
                setImeOptions(268435458);
                Cocos2dxEditBox cocos2dxEditBox4 = Cocos2dxEditBox.this;
                Cocos2dxEditBox.access$302(cocos2dxEditBox4, Cocos2dxEditBox.access$400(cocos2dxEditBox4).getResources().getString(R.string.go));
            } else if (str.contentEquals("send")) {
                setImeOptions(268435460);
                Cocos2dxEditBox cocos2dxEditBox5 = Cocos2dxEditBox.this;
                Cocos2dxEditBox.access$302(cocos2dxEditBox5, Cocos2dxEditBox.access$400(cocos2dxEditBox5).getResources().getString(R.string.send));
            } else {
                Cocos2dxEditBox.access$302(Cocos2dxEditBox.this, null);
                Log.e("Cocos2dxEditBox", "unknown confirm type " + str);
            }
        }

        private void l(String str, boolean z) {
            if (str.contentEquals("text")) {
                if (z) {
                    setInputType(131073);
                    return;
                } else {
                    setInputType(1);
                    return;
                }
            }
            if (str.contentEquals("email")) {
                setInputType(32);
                return;
            }
            if (str.contentEquals("number")) {
                setInputType(12290);
                return;
            }
            if (str.contentEquals("phone")) {
                setInputType(3);
                return;
            }
            if (str.contentEquals("password")) {
                setInputType(129);
                return;
            }
            Log.e("Cocos2dxEditBox", "unknown input type " + str);
        }

        private void m(int i) {
            this.i = i;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) Cocos2dxEditBox.access$200(Cocos2dxEditBox.this).getLayoutParams();
            layoutParams.topMargin = this.i - getHeight();
            setLayoutParams(layoutParams);
            requestLayout();
        }

        public void h() {
            Cocos2dxEditBox.access$200(Cocos2dxEditBox.this).setVisibility(4);
            j();
        }

        public void n(String str, int i, boolean z, boolean z2, String str2, String str3) {
            this.b = z;
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
            setText(str);
            if (getText().length() >= str.length()) {
                setSelection(str.length());
            } else {
                setSelection(getText().length());
            }
            k(str2);
            l(str3, this.b);
            setVisibility(0);
            requestFocus();
            g();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int paddingBottom = getPaddingBottom() / 2;
            canvas.drawLine(getScrollX(), (getHeight() - paddingBottom) - this.f, getScrollX() + getWidth(), (getHeight() - paddingBottom) - this.f, this.d);
            super.onDraw(canvas);
        }
    }

    public Cocos2dxEditBox(Cocos2dxActivity cocos2dxActivity, FrameLayout frameLayout) {
        sThis = this;
        this.mActivity = cocos2dxActivity;
        addItems(cocos2dxActivity, frameLayout);
    }

    static int access$000() {
        return DARK_GREEN;
    }

    static void access$100(Cocos2dxEditBox cocos2dxEditBox, String str) {
        cocos2dxEditBox.onKeyboardInput(str);
    }

    static void access$1100(Cocos2dxEditBox cocos2dxEditBox, String str) {
        cocos2dxEditBox.onKeyboardConfirm(str);
    }

    static boolean access$1200(Cocos2dxEditBox cocos2dxEditBox) {
        return cocos2dxEditBox.mConfirmHold;
    }

    static Cocos2dxEditBox access$1300() {
        return sThis;
    }

    static void access$1400(Cocos2dxEditBox cocos2dxEditBox, String str, int i, boolean z, boolean z2, String str2, String str3) {
        cocos2dxEditBox.show(str, i, z, z2, str2, str3);
    }

    static void access$1500(String str) {
        onKeyboardInputNative(str);
    }

    static void access$1600(String str) {
        onKeyboardCompleteNative(str);
    }

    static void access$1700(String str) {
        onKeyboardConfirmNative(str);
    }

    static g access$200(Cocos2dxEditBox cocos2dxEditBox) {
        return cocos2dxEditBox.mEditText;
    }

    static String access$302(Cocos2dxEditBox cocos2dxEditBox, String str) {
        cocos2dxEditBox.mButtonTitle = str;
        return str;
    }

    static Cocos2dxActivity access$400(Cocos2dxEditBox cocos2dxEditBox) {
        return cocos2dxEditBox.mActivity;
    }

    static void access$600(Cocos2dxEditBox cocos2dxEditBox) {
        cocos2dxEditBox.hide();
    }

    private void addButton(Cocos2dxActivity cocos2dxActivity, RelativeLayout relativeLayout) {
        this.mButton = new Button(cocos2dxActivity);
        this.mButtonParams = new RelativeLayout.LayoutParams(-2, -2);
        this.mButton.setTextColor(-1);
        this.mButton.setBackground(getRoundRectShape());
        RelativeLayout relativeLayout2 = new RelativeLayout(Cocos2dxHelper.getActivity());
        this.mButtonLayout = relativeLayout2;
        relativeLayout2.setVisibility(4);
        this.mButtonLayout.setBackgroundColor(-1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(11);
        layoutParams.addRule(8, this.mEditTextID);
        layoutParams.addRule(6, this.mEditTextID);
        this.mButtonLayout.addView(this.mButton, this.mButtonParams);
        this.mButtonLayout.setId(this.mButtonLayoutID);
        relativeLayout.addView(this.mButtonLayout, layoutParams);
        this.mButton.setOnClickListener(new a());
    }

    private void addEditText(Cocos2dxActivity cocos2dxActivity, RelativeLayout relativeLayout) {
        g gVar = new g(cocos2dxActivity);
        this.mEditText = gVar;
        gVar.setVisibility(4);
        this.mEditText.setBackgroundColor(-1);
        this.mEditText.setId(this.mEditTextID);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(0, this.mButtonLayoutID);
        relativeLayout.addView(this.mEditText, layoutParams);
    }

    private void addItems(Cocos2dxActivity cocos2dxActivity, FrameLayout frameLayout) {
        RelativeLayout relativeLayout = new RelativeLayout(cocos2dxActivity);
        addEditText(cocos2dxActivity, relativeLayout);
        addButton(cocos2dxActivity, relativeLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(12);
        frameLayout.addView(relativeLayout, layoutParams);
    }

    private void closeKeyboard() {
        ((InputMethodManager) this.mActivity.getSystemService("input_method")).hideSoftInputFromWindow(this.mEditText.getWindowToken(), 0);
        onKeyboardComplete(this.mEditText.getText().toString());
    }

    public static void complete() {
        sThis.hide();
    }

    private Drawable getRoundRectShape() {
        float f2 = 7;
        RoundRectShape roundRectShape = new RoundRectShape(new float[]{f2, f2, f2, f2, f2, f2, f2, f2}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(roundRectShape);
        Paint paint = shapeDrawable.getPaint();
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        shapeDrawable.getPaint().setColor(DARK_GREEN);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape);
        shapeDrawable2.getPaint().setStyle(style);
        shapeDrawable2.getPaint().setColor(DARK_GREEN_PRESS);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable2);
        stateListDrawable.addState(new int[0], shapeDrawable);
        return stateListDrawable;
    }

    private void hide() {
        Utils.hideVirtualButton();
        this.mEditText.h();
        this.mButtonLayout.setVisibility(4);
        closeKeyboard();
        this.mActivity.getGLSurfaceView().requestFocus();
        this.mActivity.getGLSurfaceView().setStopHandleTouchAndKeyEvents(false);
    }

    private static void hideNative() {
        Cocos2dxEditBox cocos2dxEditBox = sThis;
        if (cocos2dxEditBox != null) {
            cocos2dxEditBox.mActivity.runOnUiThread(new c());
        }
    }

    private void onKeyboardComplete(String str) {
        this.mActivity.getGLSurfaceView().requestFocus();
        this.mActivity.getGLSurfaceView().setStopHandleTouchAndKeyEvents(false);
        this.mActivity.runOnGLThread(new e(str));
    }

    private static native void onKeyboardCompleteNative(String str);

    private void onKeyboardConfirm(String str) {
        this.mActivity.runOnGLThread(new f(str));
    }

    private static native void onKeyboardConfirmNative(String str);

    private void onKeyboardInput(String str) {
        this.mActivity.runOnGLThread(new d(str));
    }

    private static native void onKeyboardInputNative(String str);

    private void openKeyboard() {
        ((InputMethodManager) this.mActivity.getSystemService("input_method")).showSoftInput(this.mEditText, 1);
    }

    private void show(String str, int i, boolean z, boolean z2, String str2, String str3) {
        this.mConfirmHold = z2;
        this.mEditText.n(str, i, z, z2, str2, str3);
        int paddingBottom = this.mEditText.getPaddingBottom();
        int paddingTop = this.mEditText.getPaddingTop();
        this.mEditText.setPadding(paddingTop, paddingTop, paddingTop, paddingBottom);
        this.mButton.setText(this.mButtonTitle);
        if (TextUtils.isEmpty(this.mButtonTitle)) {
            this.mButton.setPadding(0, 0, 0, 0);
            this.mButtonParams.setMargins(0, 0, 0, 0);
            this.mButtonLayout.setVisibility(4);
        } else {
            int paddingBottom2 = this.mEditText.getPaddingBottom() / 2;
            this.mButton.setPadding(paddingTop, paddingBottom2, paddingTop, paddingBottom2);
            this.mButtonParams.setMargins(0, paddingBottom2, 2, 0);
            this.mButtonLayout.setVisibility(0);
        }
        this.mActivity.getGLSurfaceView().setStopHandleTouchAndKeyEvents(true);
        openKeyboard();
    }

    private static void showNative(String str, int i, boolean z, boolean z2, String str2, String str3) {
        Cocos2dxEditBox cocos2dxEditBox = sThis;
        if (cocos2dxEditBox != null) {
            cocos2dxEditBox.mActivity.runOnUiThread(new b(str, i, z, z2, str2, str3));
        }
    }
}
