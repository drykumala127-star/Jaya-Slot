package org.cocos2dx.lib;

import android.content.Context;
import android.graphics.Typeface;
import java.util.HashMap;

public class Cocos2dxTypefaces {
    private static final HashMap sTypefaceCache = new HashMap();

    public static synchronized Typeface get(Context context, String str) {
        HashMap map;
        try {
            map = sTypefaceCache;
            if (!map.containsKey(str)) {
                map.put(str, str.startsWith("/") ? Typeface.createFromFile(str) : Typeface.createFromAsset(context.getAssets(), str));
            }
        } catch (Throwable th) {
            throw th;
        }
        return (Typeface) map.get(str);
    }
}
