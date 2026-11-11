package F;

import android.app.Activity;
import android.content.Intent;

final class z extends B {
    final Intent a;
    final Activity b;
    final int c;

    z(Intent intent, Activity activity, int i) {
        this.a = intent;
        this.b = activity;
        this.c = i;
    }

    @Override
    public final void a() {
        Intent intent = this.a;
        if (intent != null) {
            this.b.startActivityForResult(intent, this.c);
        }
    }
}
