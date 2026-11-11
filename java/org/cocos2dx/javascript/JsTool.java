package org.cocos2dx.javascript;

import android.util.Log;
import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

public class JsTool {
    public static AppActivity context = (AppActivity) Cocos2dxActivity.getContext();
    public static final String fName = "trigerCallback";
    public static final String objPath = "cc.vv.PlatformApiMgr";

    class a implements Runnable {
        final String i;
        final String j;
        final JSONObject k;

        a(String str, String str2, JSONObject jSONObject) {
            this.i = str;
            this.j = str2;
            this.k = jSONObject;
        }

        @Override
        public void run() {
            Log.d("RummySlots", this.i + "   " + this.j + "   " + this.k.toString());
            Cocos2dxJavascriptJavaBridge.evalString(this.j + "." + this.i + "(" + this.k.toString() + ")");
        }
    }

    public static void callJsFunc(String str, String str2, JSONObject jSONObject) {
        context.runOnGLThread(new a(str, str2, jSONObject));
    }

    public static void sendToPlatformApiCbFunc(String str, JSONObject jSONObject) {
        try {
            jSONObject.put("cbName", str);
            callJsFunc(fName, objPath, jSONObject);
        } catch (JSONException unused) {
        }
    }
}
