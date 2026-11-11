package m;

import android.content.Context;
import java.io.Closeable;
import java.io.IOException;

abstract class v implements Closeable {

    interface a {
        v a();

        a b(Context context);
    }

    v() {
    }

    abstract u.d a();

    @Override
    public void close() throws IOException {
        a().close();
    }

    abstract u e();
}
