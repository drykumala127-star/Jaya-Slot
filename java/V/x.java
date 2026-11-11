package V;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

public abstract class x {
    public static String a(String str, String[] strArr, String[] strArr2) {
        F.n.i(strArr);
        F.n.i(strArr2);
        int iMin = Math.min(strArr.length, strArr2.length);
        for (int i = 0; i < iMin; i++) {
            String str2 = strArr[i];
            if ((str == null && str2 == null) || (str != null && str.equals(str2))) {
                return strArr2[i];
            }
        }
        return null;
    }

    public static String b(Context context, String str, String str2) {
        F.n.i(context);
        Resources resources = context.getResources();
        if (TextUtils.isEmpty(str2)) {
            str2 = m.a(context);
        }
        return m.b("google_app_id", resources, str2);
    }
}
