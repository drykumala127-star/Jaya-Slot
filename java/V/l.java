package V;

import com.google.android.gms.internal.measurement.z6;
import com.google.android.gms.measurement.internal.E1;
import com.google.android.gms.measurement.internal.y1;
import java.util.concurrent.Callable;

public final class l implements Callable {
    public final E1 a;
    public final String b;

    public l(E1 e1, String str) {
        this.a = e1;
        this.b = str;
    }

    @Override
    public final Object call() {
        return new z6("internal.appMetadata", new y1(this.a, this.b));
    }
}
