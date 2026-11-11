package x;

import android.util.SparseArray;
import java.util.HashMap;
import k.d;

public abstract class a {
    private static SparseArray a = new SparseArray();
    private static HashMap b;

    static {
        HashMap map = new HashMap();
        b = map;
        map.put(d.DEFAULT, 0);
        b.put(d.VERY_LOW, 1);
        b.put(d.HIGHEST, 2);
        for (d dVar : b.keySet()) {
            a.append(((Integer) b.get(dVar)).intValue(), dVar);
        }
    }

    public static int a(d dVar) {
        Integer num = (Integer) b.get(dVar);
        if (num != null) {
            return num.intValue();
        }
        throw new IllegalStateException("PriorityMapping is missing known Priority value " + dVar);
    }

    public static d b(int i) {
        d dVar = (d) a.get(i);
        if (dVar != null) {
            return dVar;
        }
        throw new IllegalArgumentException("Unknown Priority for value " + i);
    }
}
