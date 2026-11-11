package u0;

import kotlin.KotlinVersion;

public abstract class e {
    public static String a() {
        try {
            return KotlinVersion.CURRENT.toString();
        } catch (NoClassDefFoundError unused) {
            return null;
        }
    }
}
