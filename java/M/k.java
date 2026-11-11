package m;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

abstract class k {
    static Executor a() {
        return new n(Executors.newSingleThreadExecutor());
    }
}
