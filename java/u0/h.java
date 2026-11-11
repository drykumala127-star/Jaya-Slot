package u0;

import android.content.Context;
import c0.r;

public abstract class h {

    public interface a {
        String a(Object obj);
    }

    public static f a(String str, a aVar, c0.e eVar) {
        return d(str, aVar, eVar);
    }

    public static c0.d b(String str, String str2) {
        return c0.d.j(f.a(str, str2), f.class);
    }

    public static c0.d c(String str, a aVar) {
        return c0.d.k(f.class).b(r.i(Context.class)).f(new g(str, aVar)).d();
    }

    private static f d(String str, a aVar, c0.e eVar) {
        return f.a(str, aVar.a((Context) eVar.a(Context.class)));
    }
}
