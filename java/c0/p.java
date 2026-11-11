package c0;

import c0.o;
import com.google.firebase.components.ComponentRegistrar;

public final class p implements m0.b {
    public final ComponentRegistrar a;

    public p(ComponentRegistrar componentRegistrar) {
        this.a = componentRegistrar;
    }

    @Override
    public final Object get() {
        return o.b.a(this.a);
    }
}
