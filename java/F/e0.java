package F;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public final class e0 {
    private static final Uri f = new Uri.Builder().scheme("content").authority("com.google.android.gms.chimera").build();
    private final String a;
    private final String b;
    private final ComponentName c;
    private final int d;
    private final boolean e;

    public e0(String str, String str2, int i, boolean z) {
        n.e(str);
        this.a = str;
        n.e(str2);
        this.b = str2;
        this.c = null;
        this.d = i;
        this.e = z;
    }

    public final int a() {
        return this.d;
    }

    public final ComponentName b() {
        return this.c;
    }

    public final Intent c(Context context) {
        Bundle bundleCall;
        if (this.a == null) {
            return new Intent().setComponent(this.c);
        }
        if (this.e) {
            Bundle bundle = new Bundle();
            bundle.putString("serviceActionBundleKey", this.a);
            try {
                bundleCall = context.getContentResolver().call(f, "serviceIntentCall", (String) null, bundle);
            } catch (IllegalArgumentException e) {
                Log.w("ConnectionStatusConfig", "Dynamic intent resolution failed: ".concat(e.toString()));
                bundleCall = null;
            }
            intent = bundleCall != null ? (Intent) bundleCall.getParcelable("serviceResponseIntentKey") : null;
            if (intent == null) {
                Log.w("ConnectionStatusConfig", "Dynamic lookup for intent failed for action: ".concat(String.valueOf(this.a)));
            }
        }
        return intent != null ? intent : new Intent(this.a).setPackage(this.b);
    }

    public final String d() {
        return this.b;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof e0)) {
            return false;
        }
        e0 e0Var = (e0) obj;
        return m.a(this.a, e0Var.a) && m.a(this.b, e0Var.b) && m.a(this.c, e0Var.c) && this.d == e0Var.d && this.e == e0Var.e;
    }

    public final int hashCode() {
        return m.b(this.a, this.b, this.c, Integer.valueOf(this.d), Boolean.valueOf(this.e));
    }

    public final String toString() {
        String str = this.a;
        if (str != null) {
            return str;
        }
        n.i(this.c);
        return this.c.flattenToString();
    }
}
