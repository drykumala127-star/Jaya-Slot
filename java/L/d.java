package l;

import java.util.List;

final class d extends j {
    private final List a;

    d(List list) {
        if (list == null) {
            throw new NullPointerException("Null logRequests");
        }
        this.a = list;
    }

    @Override
    public List c() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof j) {
            return this.a.equals(((j) obj).c());
        }
        return false;
    }

    public int hashCode() {
        return this.a.hashCode() ^ 1000003;
    }

    public String toString() {
        return "BatchedLogRequest{logRequests=" + this.a + "}";
    }
}
