package f0;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class c {
    private final String a;
    private final Map b;

    public static final class b {
        private final String a;
        private Map b = null;

        b(String str) {
            this.a = str;
        }

        public c a() {
            return new c(this.a, this.b == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap(this.b)), null);
        }

        public b b(Annotation annotation) {
            if (this.b == null) {
                this.b = new HashMap();
            }
            this.b.put(annotation.annotationType(), annotation);
            return this;
        }
    }

    c(String str, Map map, a aVar) {
        this(str, map);
    }

    public static b a(String str) {
        return new b(str);
    }

    public static c d(String str) {
        return new c(str, Collections.emptyMap());
    }

    public String b() {
        return this.a;
    }

    public Annotation c(Class cls) {
        return (Annotation) this.b.get(cls);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        c cVar = (c) obj;
        return this.a.equals(cVar.a) && this.b.equals(cVar.b);
    }

    public int hashCode() {
        return (this.a.hashCode() * 31) + this.b.hashCode();
    }

    public String toString() {
        return "FieldDescriptor{name=" + this.a + ", properties=" + this.b.values() + "}";
    }

    private c(String str, Map map) {
        this.a = str;
        this.b = map;
    }
}
