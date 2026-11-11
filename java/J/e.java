package J;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class e {
    public static List a() {
        return Collections.emptyList();
    }

    public static List b(Object obj) {
        return Collections.singletonList(obj);
    }

    public static List c(Object... objArr) {
        int length = objArr.length;
        return length != 0 ? length != 1 ? Collections.unmodifiableList(Arrays.asList(objArr)) : b(objArr[0]) : a();
    }
}
