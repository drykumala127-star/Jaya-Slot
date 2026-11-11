package u;

import android.database.Cursor;
import u.M;

public final class r implements M.b {
    public final M a;

    public r(M m) {
        this.a = m;
    }

    @Override
    public final Object a(Object obj) {
        return M.s(this.a, (Cursor) obj);
    }
}
