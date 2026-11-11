package t;

import java.util.Map;
import v.b;

public final class p implements b.a {
    public final r a;
    public final Map b;

    public p(r rVar, Map map) {
        this.a = rVar;
        this.b = map;
    }

    @Override
    public final Object execute() {
        return r.h(this.a, this.b);
    }
}
