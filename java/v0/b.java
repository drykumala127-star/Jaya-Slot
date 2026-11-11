package v0;

import c0.d;
import c0.e;
import c0.j;
import com.google.firebase.components.ComponentRegistrar;
import java.util.ArrayList;
import java.util.List;

public class b implements j {
    public static Object b(String str, d dVar, e eVar) {
        return c(str, dVar, eVar);
    }

    private static Object c(String str, d dVar, e eVar) {
        try {
            c.b(str);
            return dVar.f().a(eVar);
        } finally {
            c.a();
        }
    }

    @Override
    public List a(ComponentRegistrar componentRegistrar) {
        ArrayList arrayList = new ArrayList();
        for (d dVarR : componentRegistrar.getComponents()) {
            String strG = dVarR.g();
            if (strG != null) {
                dVarR = dVarR.r(new a(strG, dVarR));
            }
            arrayList.add(dVarR);
        }
        return arrayList;
    }
}
