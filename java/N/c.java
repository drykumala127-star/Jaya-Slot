package n;

import android.content.Context;

final class c extends h {
    private final Context a;
    private final w.a b;
    private final w.a c;
    private final String d;

    c(Context context, w.a aVar, w.a aVar2, String str) {
        if (context == null) {
            throw new NullPointerException("Null applicationContext");
        }
        this.a = context;
        if (aVar == null) {
            throw new NullPointerException("Null wallClock");
        }
        this.b = aVar;
        if (aVar2 == null) {
            throw new NullPointerException("Null monotonicClock");
        }
        this.c = aVar2;
        if (str == null) {
            throw new NullPointerException("Null backendName");
        }
        this.d = str;
    }

    @Override
    public Context b() {
        return this.a;
    }

    @Override
    public String c() {
        return this.d;
    }

    @Override
    public w.a d() {
        return this.c;
    }

    @Override
    public w.a e() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof h)) {
            return false;
        }
        h hVar = (h) obj;
        return this.a.equals(hVar.b()) && this.b.equals(hVar.e()) && this.c.equals(hVar.d()) && this.d.equals(hVar.c());
    }

    public int hashCode() {
        return ((((((this.a.hashCode() ^ 1000003) * 1000003) ^ this.b.hashCode()) * 1000003) ^ this.c.hashCode()) * 1000003) ^ this.d.hashCode();
    }

    public String toString() {
        return "CreationContext{applicationContext=" + this.a + ", wallClock=" + this.b + ", monotonicClock=" + this.c + ", backendName=" + this.d + "}";
    }
}
