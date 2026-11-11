package org.cocos2dx.lib;

import android.app.Activity;
import android.view.View;

public class Utils {
    private static Activity sActivity;

    public static void hideVirtualButton() {
        if (sActivity != null) {
            int iIntValue = ((Integer) Cocos2dxReflectionHelper.getConstantValue(View.class, "SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION")).intValue();
            int iIntValue2 = ((Integer) Cocos2dxReflectionHelper.getConstantValue(View.class, "SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN")).intValue();
            int iIntValue3 = ((Integer) Cocos2dxReflectionHelper.getConstantValue(View.class, "SYSTEM_UI_FLAG_HIDE_NAVIGATION")).intValue();
            Cocos2dxReflectionHelper.invokeInstanceMethod(sActivity.getWindow().getDecorView(), "setSystemUiVisibility", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(iIntValue | ((Integer) Cocos2dxReflectionHelper.getConstantValue(View.class, "SYSTEM_UI_FLAG_LAYOUT_STABLE")).intValue() | iIntValue2 | iIntValue3 | ((Integer) Cocos2dxReflectionHelper.getConstantValue(View.class, "SYSTEM_UI_FLAG_FULLSCREEN")).intValue() | ((Integer) Cocos2dxReflectionHelper.getConstantValue(View.class, "SYSTEM_UI_FLAG_IMMERSIVE_STICKY")).intValue())});
        }
    }

    public static void setActivity(Activity activity) {
        sActivity = activity;
    }
}
