package u;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.SystemClock;
import android.util.Base64;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import m.j;
import p.a;
import p.c;
import v.b;

public class M implements u.d, v.b, u.c {
    private static final k.b n = k.b.b("proto");
    private final U i;
    private final w.a j;
    private final w.a k;
    private final e l;
    private final y0.a m;

    interface b {
        Object a(Object obj);
    }

    private static class c {
        final String a;
        final String b;

        c(String str, String str2, a aVar) {
            this(str, str2);
        }

        private c(String str, String str2) {
            this.a = str;
            this.b = str2;
        }
    }

    interface d {
        Object a();
    }

    M(w.a aVar, w.a aVar2, e eVar, U u, y0.a aVar3) {
        this.i = u;
        this.j = aVar;
        this.k = aVar2;
        this.l = eVar;
        this.m = aVar3;
    }

    public static Long A(M m, m.j jVar, m.p pVar, SQLiteDatabase sQLiteDatabase) {
        return m.q0(jVar, pVar, sQLiteDatabase);
    }

    private static byte[] A0(String str) {
        if (str == null) {
            return null;
        }
        return Base64.decode(str, 0);
    }

    public static Object B(SQLiteDatabase sQLiteDatabase) {
        return b0(sQLiteDatabase);
    }

    private void B0(a.a aVar, Map map) {
        for (Map.Entry entry : map.entrySet()) {
            aVar.a(p.d.c().c((String) entry.getKey()).b((List) entry.getValue()).a());
        }
    }

    public static Long C(Cursor cursor) {
        return h0(cursor);
    }

    private byte[] C0(long j) {
        return (byte[]) G0(Q().query("event_payloads", new String[]{"bytes"}, "event_id = ?", new String[]{String.valueOf(j)}, null, null, "sequence_num"), new B());
    }

    public static Object D(Throwable th) {
        return c0(th);
    }

    private Object D0(d dVar, b bVar) {
        long jA = this.k.a();
        while (true) {
            try {
                return dVar.a();
            } catch (SQLiteDatabaseLockedException e) {
                if (this.k.a() >= ((long) this.l.b()) + jA) {
                    return bVar.a(e);
                }
                SystemClock.sleep(50L);
            }
        }
    }

    public static Object E(String str, c.b bVar, long j, SQLiteDatabase sQLiteDatabase) {
        return v0(str, bVar, j, sQLiteDatabase);
    }

    private static k.b E0(String str) {
        return str == null ? n : k.b.b(str);
    }

    public static Object F(Map map, Cursor cursor) {
        return p0(map, cursor);
    }

    private static String F0(Iterable iterable) {
        StringBuilder sb = new StringBuilder("(");
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            sb.append(((k) it.next()).c());
            if (it.hasNext()) {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }

    public static Integer G(M m, long j, SQLiteDatabase sQLiteDatabase) {
        return m.a0(j, sQLiteDatabase);
    }

    static Object G0(Cursor cursor, b bVar) {
        try {
            return bVar.a(cursor);
        } finally {
            cursor.close();
        }
    }

    public static Object H(M m, List list, m.p pVar, Cursor cursor) {
        return m.o0(list, pVar, cursor);
    }

    public static Object I(M m, SQLiteDatabase sQLiteDatabase) {
        return m.x0(sQLiteDatabase);
    }

    public static p.a J(M m, String str, Map map, a.a aVar, SQLiteDatabase sQLiteDatabase) {
        return m.n0(str, map, aVar, sQLiteDatabase);
    }

    public static Long K(Cursor cursor) {
        return e0(cursor);
    }

    public static p.f L(long j, SQLiteDatabase sQLiteDatabase) {
        return g0(j, sQLiteDatabase);
    }

    private c.b M(int i) {
        c.b bVar = c.b.REASON_UNKNOWN;
        if (i == bVar.a()) {
            return bVar;
        }
        c.b bVar2 = c.b.MESSAGE_TOO_OLD;
        if (i == bVar2.a()) {
            return bVar2;
        }
        c.b bVar3 = c.b.CACHE_FULL;
        if (i == bVar3.a()) {
            return bVar3;
        }
        c.b bVar4 = c.b.PAYLOAD_TOO_BIG;
        if (i == bVar4.a()) {
            return bVar4;
        }
        c.b bVar5 = c.b.MAX_RETRIES_REACHED;
        if (i == bVar5.a()) {
            return bVar5;
        }
        c.b bVar6 = c.b.INVALID_PAYLOD;
        if (i == bVar6.a()) {
            return bVar6;
        }
        c.b bVar7 = c.b.SERVER_ERROR;
        if (i == bVar7.a()) {
            return bVar7;
        }
        q.a.b("SQLiteEventStore", "%n is not valid. No matched LogEventDropped-Reason found. Treated it as REASON_UNKNOWN", Integer.valueOf(i));
        return bVar;
    }

    private void N(SQLiteDatabase sQLiteDatabase) {
        D0(new l(sQLiteDatabase), new w());
    }

    private long O(SQLiteDatabase sQLiteDatabase, m.p pVar) {
        Long lV = V(sQLiteDatabase, pVar);
        if (lV != null) {
            return lV.longValue();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("backend_name", pVar.b());
        contentValues.put("priority", Integer.valueOf(x.a.a(pVar.d())));
        contentValues.put("next_request_ms", (Integer) 0);
        if (pVar.c() != null) {
            contentValues.put("extras", Base64.encodeToString(pVar.c(), 0));
        }
        return sQLiteDatabase.insert("transport_contexts", null, contentValues);
    }

    private p.b R() {
        return p.b.b().b(p.e.c().b(P()).c(e.a.f()).a()).a();
    }

    private long S() {
        return Q().compileStatement("PRAGMA page_count").simpleQueryForLong();
    }

    private long T() {
        return Q().compileStatement("PRAGMA page_size").simpleQueryForLong();
    }

    private p.f U() {
        return (p.f) W(new C(this.j.a()));
    }

    private Long V(SQLiteDatabase sQLiteDatabase, m.p pVar) {
        StringBuilder sb = new StringBuilder("backend_name = ? and priority = ?");
        ArrayList arrayList = new ArrayList(Arrays.asList(pVar.b(), String.valueOf(x.a.a(pVar.d()))));
        if (pVar.c() != null) {
            sb.append(" and extras = ?");
            arrayList.add(Base64.encodeToString(pVar.c(), 0));
        } else {
            sb.append(" and extras is null");
        }
        return (Long) G0(sQLiteDatabase.query("transport_contexts", new String[]{"_id"}, sb.toString(), (String[]) arrayList.toArray(new String[0]), null, null, null), new x());
    }

    private boolean X() {
        return S() * T() >= this.l.f();
    }

    private List Y(List list, Map map) {
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            k kVar = (k) listIterator.next();
            if (map.containsKey(Long.valueOf(kVar.c()))) {
                j.a aVarL = kVar.b().l();
                for (c cVar : (Set) map.get(Long.valueOf(kVar.c()))) {
                    aVarL.c(cVar.a, cVar.b);
                }
                listIterator.set(k.a(kVar.c(), kVar.d(), aVarL.d()));
            }
        }
        return list;
    }

    private Object Z(Cursor cursor) {
        while (cursor.moveToNext()) {
            f(cursor.getInt(0), c.b.MESSAGE_TOO_OLD, cursor.getString(1));
        }
        return null;
    }

    private Integer a0(long j, SQLiteDatabase sQLiteDatabase) {
        String[] strArr = {String.valueOf(j)};
        G0(sQLiteDatabase.rawQuery("SELECT COUNT(*), transport_name FROM events WHERE timestamp_ms < ? GROUP BY transport_name", strArr), new r(this));
        return Integer.valueOf(sQLiteDatabase.delete("events", "timestamp_ms < ?", strArr));
    }

    private static Object b0(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.beginTransaction();
        return null;
    }

    private static Object c0(Throwable th) {
        throw new v.a("Timed out while trying to acquire the lock.", th);
    }

    private static SQLiteDatabase d0(Throwable th) {
        throw new v.a("Timed out while trying to open db.", th);
    }

    private static Long e0(Cursor cursor) {
        if (cursor.moveToNext()) {
            return Long.valueOf(cursor.getLong(0));
        }
        return 0L;
    }

    private static p.f f0(long j, Cursor cursor) {
        cursor.moveToNext();
        return p.f.c().c(cursor.getLong(0)).b(j).a();
    }

    private static p.f g0(long j, SQLiteDatabase sQLiteDatabase) {
        return (p.f) G0(sQLiteDatabase.rawQuery("SELECT last_metrics_upload_ms FROM global_log_event_state LIMIT 1", new String[0]), new D(j));
    }

    private static Long h0(Cursor cursor) {
        if (cursor.moveToNext()) {
            return Long.valueOf(cursor.getLong(0));
        }
        return null;
    }

    private Boolean i0(m.p pVar, SQLiteDatabase sQLiteDatabase) {
        Long lV = V(sQLiteDatabase, pVar);
        return lV == null ? Boolean.FALSE : (Boolean) G0(Q().rawQuery("SELECT 1 FROM events WHERE context_id = ? LIMIT 1", new String[]{lV.toString()}), new t());
    }

    private static List j0(SQLiteDatabase sQLiteDatabase) {
        return (List) G0(sQLiteDatabase.rawQuery("SELECT distinct t._id, t.backend_name, t.priority, t.extras FROM transport_contexts AS t, events AS e WHERE e.context_id = t._id", new String[0]), new J());
    }

    private static List k0(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            arrayList.add(m.p.a().b(cursor.getString(1)).d(x.a.b(cursor.getInt(2))).c(A0(cursor.getString(3))).a());
        }
        return arrayList;
    }

    private List l0(m.p pVar, SQLiteDatabase sQLiteDatabase) {
        List listY0 = y0(sQLiteDatabase, pVar, this.l.d());
        for (k.d dVar : k.d.values()) {
            if (dVar != pVar.d()) {
                int iD = this.l.d() - listY0.size();
                if (iD <= 0) {
                    break;
                }
                listY0.addAll(y0(sQLiteDatabase, pVar.f(dVar), iD));
            }
        }
        return Y(listY0, z0(sQLiteDatabase, listY0));
    }

    private p.a m0(Map map, a.a aVar, Cursor cursor) {
        while (cursor.moveToNext()) {
            String string = cursor.getString(0);
            c.b bVarM = M(cursor.getInt(1));
            long j = cursor.getLong(2);
            if (!map.containsKey(string)) {
                map.put(string, new ArrayList());
            }
            ((List) map.get(string)).add(p.c.c().c(bVarM).b(j).a());
        }
        B0(aVar, map);
        aVar.e(U());
        aVar.d(R());
        aVar.c((String) this.m.get());
        return aVar.b();
    }

    public static p.f n(long j, Cursor cursor) {
        return f0(j, cursor);
    }

    private p.a n0(String str, Map map, a.a aVar, SQLiteDatabase sQLiteDatabase) {
        return (p.a) G0(sQLiteDatabase.rawQuery(str, new String[0]), new A(this, map, aVar));
    }

    public static List o(M m, m.p pVar, SQLiteDatabase sQLiteDatabase) {
        return m.l0(pVar, sQLiteDatabase);
    }

    private Object o0(List list, m.p pVar, Cursor cursor) {
        while (cursor.moveToNext()) {
            long j = cursor.getLong(0);
            boolean z = cursor.getInt(7) != 0;
            j.a aVarK = m.j.a().j(cursor.getString(1)).i(cursor.getLong(2)).k(cursor.getLong(3));
            if (z) {
                aVarK.h(new m.i(E0(cursor.getString(4)), cursor.getBlob(5)));
            } else {
                aVarK.h(new m.i(E0(cursor.getString(4)), C0(j)));
            }
            if (!cursor.isNull(6)) {
                aVarK.g(Integer.valueOf(cursor.getInt(6)));
            }
            list.add(k.a(j, pVar, aVarK.d()));
        }
        return null;
    }

    public static List p(Cursor cursor) {
        return k0(cursor);
    }

    private static Object p0(Map map, Cursor cursor) {
        while (cursor.moveToNext()) {
            long j = cursor.getLong(0);
            Set hashSet = (Set) map.get(Long.valueOf(j));
            if (hashSet == null) {
                hashSet = new HashSet();
                map.put(Long.valueOf(j), hashSet);
            }
            hashSet.add(new c(cursor.getString(1), cursor.getString(2), null));
        }
        return null;
    }

    public static Object q(M m, Cursor cursor) {
        return m.s0(cursor);
    }

    private Long q0(m.j jVar, m.p pVar, SQLiteDatabase sQLiteDatabase) {
        if (X()) {
            f(1L, c.b.CACHE_FULL, jVar.j());
            return -1L;
        }
        long jO = O(sQLiteDatabase, pVar);
        int iE = this.l.e();
        byte[] bArrA = jVar.e().a();
        boolean z = bArrA.length <= iE;
        ContentValues contentValues = new ContentValues();
        contentValues.put("context_id", Long.valueOf(jO));
        contentValues.put("transport_name", jVar.j());
        contentValues.put("timestamp_ms", Long.valueOf(jVar.f()));
        contentValues.put("uptime_ms", Long.valueOf(jVar.k()));
        contentValues.put("payload_encoding", jVar.e().b().a());
        contentValues.put("code", jVar.d());
        contentValues.put("num_attempts", (Integer) 0);
        contentValues.put("inline", Boolean.valueOf(z));
        contentValues.put("payload", z ? bArrA : new byte[0]);
        long jInsert = sQLiteDatabase.insert("events", null, contentValues);
        if (!z) {
            int iCeil = (int) Math.ceil(((double) bArrA.length) / ((double) iE));
            for (int i = 1; i <= iCeil; i++) {
                byte[] bArrCopyOfRange = Arrays.copyOfRange(bArrA, (i - 1) * iE, Math.min(i * iE, bArrA.length));
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("event_id", Long.valueOf(jInsert));
                contentValues2.put("sequence_num", Integer.valueOf(i));
                contentValues2.put("bytes", bArrCopyOfRange);
                sQLiteDatabase.insert("event_payloads", null, contentValues2);
            }
        }
        for (Map.Entry entry : jVar.i().entrySet()) {
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("event_id", Long.valueOf(jInsert));
            contentValues3.put("name", (String) entry.getKey());
            contentValues3.put("value", (String) entry.getValue());
            sQLiteDatabase.insert("event_metadata", null, contentValues3);
        }
        return Long.valueOf(jInsert);
    }

    public static SQLiteDatabase r(Throwable th) {
        return d0(th);
    }

    private static byte[] r0(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        int length = 0;
        while (cursor.moveToNext()) {
            byte[] blob = cursor.getBlob(0);
            arrayList.add(blob);
            length += blob.length;
        }
        byte[] bArr = new byte[length];
        int length2 = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            byte[] bArr2 = (byte[]) arrayList.get(i);
            System.arraycopy(bArr2, 0, bArr, length2, bArr2.length);
            length2 += bArr2.length;
        }
        return bArr;
    }

    public static Object s(M m, Cursor cursor) {
        return m.Z(cursor);
    }

    private Object s0(Cursor cursor) {
        while (cursor.moveToNext()) {
            f(cursor.getInt(0), c.b.MAX_RETRIES_REACHED, cursor.getString(1));
        }
        return null;
    }

    public static Object t(M m, String str, String str2, SQLiteDatabase sQLiteDatabase) {
        return m.t0(str, str2, sQLiteDatabase);
    }

    private Object t0(String str, String str2, SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.compileStatement(str).execute();
        G0(sQLiteDatabase.rawQuery(str2, null), new u(this));
        sQLiteDatabase.compileStatement("DELETE FROM events WHERE num_attempts >= 16").execute();
        return null;
    }

    public static Boolean u(M m, m.p pVar, SQLiteDatabase sQLiteDatabase) {
        return m.i0(pVar, sQLiteDatabase);
    }

    private static Boolean u0(Cursor cursor) {
        return Boolean.valueOf(cursor.getCount() > 0);
    }

    public static Object v(long j, m.p pVar, SQLiteDatabase sQLiteDatabase) {
        return w0(j, pVar, sQLiteDatabase);
    }

    private static Object v0(String str, c.b bVar, long j, SQLiteDatabase sQLiteDatabase) {
        if (((Boolean) G0(sQLiteDatabase.rawQuery("SELECT 1 FROM log_event_dropped WHERE log_source = ? AND reason = ?", new String[]{str, Integer.toString(bVar.a())}), new y())).booleanValue()) {
            sQLiteDatabase.execSQL("UPDATE log_event_dropped SET events_dropped_count = events_dropped_count + " + j + " WHERE log_source = ? AND reason = ?", new String[]{str, Integer.toString(bVar.a())});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("log_source", str);
            contentValues.put("reason", Integer.valueOf(bVar.a()));
            contentValues.put("events_dropped_count", Long.valueOf(j));
            sQLiteDatabase.insert("log_event_dropped", null, contentValues);
        }
        return null;
    }

    public static byte[] w(Cursor cursor) {
        return r0(cursor);
    }

    private static Object w0(long j, m.p pVar, SQLiteDatabase sQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("next_request_ms", Long.valueOf(j));
        if (sQLiteDatabase.update("transport_contexts", contentValues, "backend_name = ? and priority = ?", new String[]{pVar.b(), String.valueOf(x.a.a(pVar.d()))}) < 1) {
            contentValues.put("backend_name", pVar.b());
            contentValues.put("priority", Integer.valueOf(x.a.a(pVar.d())));
            sQLiteDatabase.insert("transport_contexts", null, contentValues);
        }
        return null;
    }

    public static List x(SQLiteDatabase sQLiteDatabase) {
        return j0(sQLiteDatabase);
    }

    private Object x0(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.compileStatement("DELETE FROM log_event_dropped").execute();
        sQLiteDatabase.compileStatement("UPDATE global_log_event_state SET last_metrics_upload_ms=" + this.j.a()).execute();
        return null;
    }

    public static p.a y(M m, Map map, a.a aVar, Cursor cursor) {
        return m.m0(map, aVar, cursor);
    }

    private List y0(SQLiteDatabase sQLiteDatabase, m.p pVar, int i) {
        ArrayList arrayList = new ArrayList();
        Long lV = V(sQLiteDatabase, pVar);
        if (lV == null) {
            return arrayList;
        }
        G0(sQLiteDatabase.query("events", new String[]{"_id", "transport_name", "timestamp_ms", "uptime_ms", "payload_encoding", "payload", "code", "inline"}, "context_id = ?", new String[]{lV.toString()}, null, null, null, String.valueOf(i)), new v(this, arrayList, pVar));
        return arrayList;
    }

    public static Boolean z(Cursor cursor) {
        return u0(cursor);
    }

    private Map z0(SQLiteDatabase sQLiteDatabase, List list) {
        HashMap map = new HashMap();
        StringBuilder sb = new StringBuilder("event_id IN (");
        for (int i = 0; i < list.size(); i++) {
            sb.append(((k) list.get(i)).c());
            if (i < list.size() - 1) {
                sb.append(',');
            }
        }
        sb.append(')');
        G0(sQLiteDatabase.query("event_metadata", new String[]{"event_id", "name", "value"}, sb.toString(), null, null, null, null), new z(map));
        return map;
    }

    long P() {
        return S() * T();
    }

    SQLiteDatabase Q() {
        U u = this.i;
        Objects.requireNonNull(u);
        return (SQLiteDatabase) D0(new E(u), new F());
    }

    Object W(b bVar) {
        SQLiteDatabase sQLiteDatabaseQ = Q();
        sQLiteDatabaseQ.beginTransaction();
        try {
            Object objA = bVar.a(sQLiteDatabaseQ);
            sQLiteDatabaseQ.setTransactionSuccessful();
            return objA;
        } finally {
            sQLiteDatabaseQ.endTransaction();
        }
    }

    @Override
    public Object a(b.a aVar) {
        SQLiteDatabase sQLiteDatabaseQ = Q();
        N(sQLiteDatabaseQ);
        try {
            Object objExecute = aVar.execute();
            sQLiteDatabaseQ.setTransactionSuccessful();
            return objExecute;
        } finally {
            sQLiteDatabaseQ.endTransaction();
        }
    }

    @Override
    public int b() {
        return ((Integer) W(new I(this, this.j.a() - this.l.c()))).intValue();
    }

    @Override
    public boolean c(m.p pVar) {
        return ((Boolean) W(new n(this, pVar))).booleanValue();
    }

    @Override
    public void close() {
        this.i.close();
    }

    @Override
    public void d(Iterable iterable) {
        if (iterable.iterator().hasNext()) {
            Q().compileStatement("DELETE FROM events WHERE _id in " + F0(iterable)).execute();
        }
    }

    @Override
    public void e() {
        W(new p(this));
    }

    @Override
    public void f(long j, c.b bVar, String str) {
        W(new q(str, bVar, j));
    }

    @Override
    public void g(m.p pVar, long j) {
        W(new o(j, pVar));
    }

    @Override
    public p.a h() {
        return (p.a) W(new s(this, "SELECT log_source, reason, events_dropped_count FROM log_event_dropped", new HashMap(), p.a.e()));
    }

    @Override
    public void i(Iterable iterable) {
        if (iterable.iterator().hasNext()) {
            W(new K(this, "UPDATE events SET num_attempts = num_attempts + 1 WHERE _id in " + F0(iterable), "SELECT COUNT(*), transport_name FROM events WHERE num_attempts >= 16 GROUP BY transport_name"));
        }
    }

    @Override
    public k j(m.p pVar, m.j jVar) {
        q.a.c("SQLiteEventStore", "Storing event with priority=%s, name=%s for destination %s", pVar.d(), jVar.j(), pVar.b());
        long jLongValue = ((Long) W(new m(this, jVar, pVar))).longValue();
        if (jLongValue < 1) {
            return null;
        }
        return k.a(jLongValue, pVar, jVar);
    }

    @Override
    public long k(m.p pVar) {
        return ((Long) G0(Q().rawQuery("SELECT next_request_ms FROM transport_contexts WHERE backend_name = ? and priority = ?", new String[]{pVar.b(), String.valueOf(x.a.a(pVar.d()))}), new H())).longValue();
    }

    @Override
    public Iterable l(m.p pVar) {
        return (Iterable) W(new L(this, pVar));
    }

    @Override
    public Iterable m() {
        return (Iterable) W(new G());
    }
}
