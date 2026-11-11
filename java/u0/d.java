package u0;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class d {
    private static volatile d b;
    private final Set a = new HashSet();

    d() {
    }

    public static d a() {
        d dVar = b;
        if (dVar == null) {
            synchronized (d.class) {
                try {
                    dVar = b;
                    if (dVar == null) {
                        dVar = new d();
                        b = dVar;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return dVar;
    }

    Set b() {
        Set setUnmodifiableSet;
        synchronized (this.a) {
            setUnmodifiableSet = Collections.unmodifiableSet(this.a);
        }
        return setUnmodifiableSet;
    }
}
