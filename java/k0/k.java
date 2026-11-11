package k0;

public interface k {

    public enum a {
        NONE(0),
        SDK(1),
        GLOBAL(2),
        COMBINED(3);

        private final int i;

        a(int i) {
            this.i = i;
        }

        public int a() {
            return this.i;
        }
    }

    a b(String str);
}
