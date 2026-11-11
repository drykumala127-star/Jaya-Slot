package B;

import android.os.Bundle;

final class s extends t {
    s(int i, int i2, Bundle bundle) {
        super(i, 2, bundle);
    }

    @Override
    final void a(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            d(null);
        } else {
            c(new u(4, "Invalid response to one way request", null));
        }
    }

    @Override
    final boolean b() {
        return true;
    }
}
