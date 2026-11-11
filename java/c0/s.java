package c0;

import java.util.Arrays;
import java.util.List;

public class s extends t {
    private final List i;

    public s(List list) {
        super("Dependency cycle detected: " + Arrays.toString(list.toArray()));
        this.i = list;
    }
}
