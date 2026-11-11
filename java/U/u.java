package u;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Arrays;
import java.util.List;

final class U extends SQLiteOpenHelper {
    private static final String k = "INSERT INTO global_log_event_state VALUES (" + System.currentTimeMillis() + ")";
    static int l = 5;
    private static final a m;
    private static final a n;
    private static final a o;
    private static final a p;
    private static final a q;
    private static final List r;
    private final int i;
    private boolean j;

    public interface a {
        void a(SQLiteDatabase sQLiteDatabase);
    }

    static {
        O o2 = new O();
        m = o2;
        P p2 = new P();
        n = p2;
        Q q2 = new Q();
        o = q2;
        S s = new S();
        p = s;
        T t = new T();
        q = t;
        r = Arrays.asList(o2, p2, q2, s, t);
    }

    U(Context context, String str, int i) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, i);
        this.j = false;
        this.i = i;
    }

    public static void a(SQLiteDatabase sQLiteDatabase) {
        p(sQLiteDatabase);
    }

    public static void e(SQLiteDatabase sQLiteDatabase) {
        r(sQLiteDatabase);
    }

    public static void f(SQLiteDatabase sQLiteDatabase) {
        t(sQLiteDatabase);
    }

    public static void h(SQLiteDatabase sQLiteDatabase) {
        q(sQLiteDatabase);
    }

    public static void n(SQLiteDatabase sQLiteDatabase) {
        s(sQLiteDatabase);
    }

    private void o(SQLiteDatabase sQLiteDatabase) {
        if (this.j) {
            return;
        }
        onConfigure(sQLiteDatabase);
    }

    private static void p(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE events (_id INTEGER PRIMARY KEY, context_id INTEGER NOT NULL, transport_name TEXT NOT NULL, timestamp_ms INTEGER NOT NULL, uptime_ms INTEGER NOT NULL, payload BLOB NOT NULL, code INTEGER, num_attempts INTEGER NOT NULL,FOREIGN KEY (context_id) REFERENCES transport_contexts(_id) ON DELETE CASCADE)");
        sQLiteDatabase.execSQL("CREATE TABLE event_metadata (_id INTEGER PRIMARY KEY, event_id INTEGER NOT NULL, name TEXT NOT NULL, value TEXT NOT NULL,FOREIGN KEY (event_id) REFERENCES events(_id) ON DELETE CASCADE)");
        sQLiteDatabase.execSQL("CREATE TABLE transport_contexts (_id INTEGER PRIMARY KEY, backend_name TEXT NOT NULL, priority INTEGER NOT NULL, next_request_ms INTEGER NOT NULL)");
        sQLiteDatabase.execSQL("CREATE INDEX events_backend_id on events(context_id)");
        sQLiteDatabase.execSQL("CREATE UNIQUE INDEX contexts_backend_priority on transport_contexts(backend_name, priority)");
    }

    private static void q(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("ALTER TABLE transport_contexts ADD COLUMN extras BLOB");
        sQLiteDatabase.execSQL("CREATE UNIQUE INDEX contexts_backend_priority_extras on transport_contexts(backend_name, priority, extras)");
        sQLiteDatabase.execSQL("DROP INDEX contexts_backend_priority");
    }

    private static void r(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("ALTER TABLE events ADD COLUMN payload_encoding TEXT");
    }

    private static void s(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("ALTER TABLE events ADD COLUMN inline BOOLEAN NOT NULL DEFAULT 1");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS event_payloads");
        sQLiteDatabase.execSQL("CREATE TABLE event_payloads (sequence_num INTEGER NOT NULL, event_id INTEGER NOT NULL, bytes BLOB NOT NULL,FOREIGN KEY (event_id) REFERENCES events(_id) ON DELETE CASCADE,PRIMARY KEY (sequence_num, event_id))");
    }

    private static void t(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS log_event_dropped");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS global_log_event_state");
        sQLiteDatabase.execSQL("CREATE TABLE log_event_dropped (log_source VARCHAR(45) NOT NULL,reason INTEGER NOT NULL,events_dropped_count BIGINT NOT NULL,PRIMARY KEY(log_source, reason))");
        sQLiteDatabase.execSQL("CREATE TABLE global_log_event_state (last_metrics_upload_ms BIGINT PRIMARY KEY)");
        sQLiteDatabase.execSQL(k);
    }

    private void u(SQLiteDatabase sQLiteDatabase, int i) {
        o(sQLiteDatabase);
        v(sQLiteDatabase, 0, i);
    }

    private void v(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        List list = r;
        if (i2 <= list.size()) {
            while (i < i2) {
                ((a) r.get(i)).a(sQLiteDatabase);
                i++;
            }
            return;
        }
        throw new IllegalArgumentException("Migration from " + i + " to " + i2 + " was requested, but cannot be performed. Only " + list.size() + " migrations are provided");
    }

    @Override
    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        this.j = true;
        sQLiteDatabase.rawQuery("PRAGMA busy_timeout=0;", new String[0]).close();
        sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        u(sQLiteDatabase, this.i);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE events");
        sQLiteDatabase.execSQL("DROP TABLE event_metadata");
        sQLiteDatabase.execSQL("DROP TABLE transport_contexts");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS event_payloads");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS log_event_dropped");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS global_log_event_state");
        u(sQLiteDatabase, i2);
    }

    @Override
    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        o(sQLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        o(sQLiteDatabase);
        v(sQLiteDatabase, i, i2);
    }
}
