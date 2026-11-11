package u;

import android.database.sqlite.SQLiteDatabase;
import u.M;

public final class l implements M.d {
    public final SQLiteDatabase a;

    public l(SQLiteDatabase sQLiteDatabase) {
        this.a = sQLiteDatabase;
    }

    @Override
    public final Object a() {
        return M.B(this.a);
    }
}
