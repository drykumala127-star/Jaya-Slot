package u;

import android.database.Cursor;
import java.util.Map;
import u.M;

public final class z implements M.b {
    public final Map a;

    public z(Map map) {
        this.a = map;
    }

    @Override
    public final Object a(Object obj) {
        return M.F(this.a, (Cursor) obj);
    }
}
