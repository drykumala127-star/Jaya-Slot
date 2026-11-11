package c0;

import java.util.Set;

abstract class a implements e {
    a() {
    }

    @Override
    public Object a(Class cls) {
        m0.b bVarB = b(cls);
        if (bVarB == null) {
            return null;
        }
        return bVarB.get();
    }

    @Override
    public Set c(Class cls) {
        return (Set) d(cls).get();
    }
}
