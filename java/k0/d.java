package k0;

import android.content.Context;

public final class d implements m0.b {
    public final Context a;
    public final String b;

    public d(Context context, String str) {
        this.a = context;
        this.b = str;
    }

    @Override
    public final Object get() {
        return g.d(this.a, this.b);
    }
}
