package x0;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class c {
    public static b a(File file) {
        Map mapB = b(file);
        if (mapB == null) {
            return null;
        }
        String str = (String) mapB.get("channel");
        mapB.remove("channel");
        return new b(str, mapB);
    }

    public static Map b(File file) {
        try {
            String strC = c(file);
            if (strC == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(strC);
            Iterator<String> itKeys = jSONObject.keys();
            HashMap map = new HashMap();
            while (itKeys.hasNext()) {
                String string = itKeys.next().toString();
                map.put(string, jSONObject.getString(string));
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String c(File file) {
        return e.d(file, 1903654775);
    }
}
