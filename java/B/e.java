package B;

import android.os.Looper;
import android.os.Message;

final class e extends Q.f {
    final c a;

    e(c cVar, Looper looper) {
        super(looper);
        this.a = cVar;
    }

    @Override
    public final void handleMessage(Message message) {
        c.d(this.a, message);
    }
}
