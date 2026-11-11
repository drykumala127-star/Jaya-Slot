package D;

public final class g extends UnsupportedOperationException {
    private final C.d i;

    public g(C.d dVar) {
        this.i = dVar;
    }

    @Override
    public String getMessage() {
        return "Missing ".concat(String.valueOf(this.i));
    }
}
