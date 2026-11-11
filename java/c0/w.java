package c0;

public class w extends RuntimeException {
    public w(String str) {
        super(str);
    }

    public w(String str, Throwable th) {
        super(str, th);
    }
}
