package E;

import android.os.Handler;
import java.util.concurrent.Executor;

public final class l implements Executor {
    public final Handler a;

    public l(Handler handler) {
        this.a = handler;
    }

    @Override
    public final void execute(Runnable runnable) {
        this.a.post(runnable);
    }
}
