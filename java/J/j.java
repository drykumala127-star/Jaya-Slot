package J;

import java.io.Closeable;
import java.io.IOException;

public abstract class j {
    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
