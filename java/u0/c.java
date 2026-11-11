package u0;

import c0.r;
import java.util.Iterator;
import java.util.Set;

public class c implements i {
    private final String a;
    private final d b;

    c(Set set, d dVar) {
        this.a = e(set);
        this.b = dVar;
    }

    public static i b(c0.e eVar) {
        return d(eVar);
    }

    public static c0.d c() {
        return c0.d.c(i.class).b(r.k(f.class)).f(new b()).d();
    }

    private static i d(c0.e eVar) {
        return new c(eVar.c(f.class), d.a());
    }

    private static String e(Set set) {
        StringBuilder sb = new StringBuilder();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            f fVar = (f) it.next();
            sb.append(fVar.b());
            sb.append('/');
            sb.append(fVar.c());
            if (it.hasNext()) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    @Override
    public String a() {
        if (this.b.b().isEmpty()) {
            return this.a;
        }
        return this.a + ' ' + e(this.b.b());
    }
}
