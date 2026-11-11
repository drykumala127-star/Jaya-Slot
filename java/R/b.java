package r;

public abstract class b {
    public static Object a(int i, Object obj, a aVar, c cVar) {
        Object objA;
        if (i < 1) {
            return aVar.a(obj);
        }
        do {
            objA = aVar.a(obj);
            obj = cVar.a(obj, objA);
            if (obj == null) {
                break;
            }
            i--;
        } while (i >= 1);
        return objA;
    }
}
