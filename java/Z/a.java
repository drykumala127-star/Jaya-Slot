package z;

import F.n;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;

public class a {
    private static final Lock c = new ReentrantLock();
    private static a d;
    private final Lock a = new ReentrantLock();
    private final SharedPreferences b;

    a(Context context) {
        this.b = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static a a(Context context) {
        n.i(context);
        c.lock();
        try {
            if (d == null) {
                d = new a(context.getApplicationContext());
            }
            return d;
        } finally {
            c.unlock();
        }
    }

    private static final String d(String str, String str2) {
        return str + ":" + str2;
    }

    public GoogleSignInAccount b() {
        String strC;
        String strC2 = c("defaultGoogleSignInAccount");
        if (TextUtils.isEmpty(strC2) || (strC = c(d("googleSignInAccount", strC2))) == null) {
            return null;
        }
        try {
            return GoogleSignInAccount.u(strC);
        } catch (JSONException unused) {
            return null;
        }
    }

    protected final String c(String str) {
        this.a.lock();
        try {
            return this.b.getString(str, null);
        } finally {
            this.a.unlock();
        }
    }
}
