package B;

import android.os.Bundle;

final class v extends t {
    v(int i, int i2, Bundle bundle) {
        super(i, 1, bundle);
    }

    @Override
    final void a(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle("data");
        if (bundle2 == null) {
            bundle2 = Bundle.EMPTY;
        }
        d(bundle2);
    }

    @Override
    final boolean b() {
        return false;
    }
}
