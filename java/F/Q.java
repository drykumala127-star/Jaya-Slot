package F;

import android.content.Context;
import android.content.res.Resources;

public class q {
    private final Resources a;
    private final String b;

    public q(Context context) {
        n.i(context);
        Resources resources = context.getResources();
        this.a = resources;
        this.b = resources.getResourcePackageName(C.p.a);
    }

    public String a(String str) {
        int identifier = this.a.getIdentifier(str, "string", this.b);
        if (identifier == 0) {
            return null;
        }
        return this.a.getString(identifier);
    }
}
