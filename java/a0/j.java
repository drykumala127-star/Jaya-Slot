package a0;

import F.n;

public abstract class j extends Exception {
    protected j() {
    }

    public j(String str) {
        super(str);
        n.f(str, "Detail message must not be empty");
    }
}
