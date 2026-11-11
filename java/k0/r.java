package k0;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class r {
    private final SharedPreferences a;

    public r(Context context, String str) {
        this.a = context.getSharedPreferences("FirebaseHeartBeat" + str, 0);
    }

    private synchronized void a() {
        try {
            long j = this.a.getLong("fire-count", 0L);
            String key = "";
            String str = null;
            for (Map.Entry<String, ?> entry : this.a.getAll().entrySet()) {
                if (entry.getValue() instanceof Set) {
                    for (String str2 : (Set) entry.getValue()) {
                        if (str == null || str.compareTo(str2) > 0) {
                            key = entry.getKey();
                            str = str2;
                        }
                    }
                }
            }
            HashSet hashSet = new HashSet(this.a.getStringSet(key, new HashSet()));
            hashSet.remove(str);
            this.a.edit().putStringSet(key, hashSet).putLong("fire-count", j - 1).commit();
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized String d(long j) {
        if (Build.VERSION.SDK_INT >= 26) {
            return q.a(o.a(n.a(l.a(new Date(j)), m.a())), p.a());
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(new Date(j));
    }

    private synchronized String e(String str) {
        for (Map.Entry<String, ?> entry : this.a.getAll().entrySet()) {
            if (entry.getValue() instanceof Set) {
                Iterator it = ((Set) entry.getValue()).iterator();
                while (it.hasNext()) {
                    if (str.equals((String) it.next())) {
                        return entry.getKey();
                    }
                }
            }
        }
        return null;
    }

    private synchronized void h(String str) {
        try {
            String strE = e(str);
            if (strE == null) {
                return;
            }
            HashSet hashSet = new HashSet(this.a.getStringSet(strE, new HashSet()));
            hashSet.remove(str);
            if (hashSet.isEmpty()) {
                this.a.edit().remove(strE).commit();
            } else {
                this.a.edit().putStringSet(strE, hashSet).commit();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized void b() {
        try {
            SharedPreferences.Editor editorEdit = this.a.edit();
            for (Map.Entry<String, ?> entry : this.a.getAll().entrySet()) {
                if (entry.getValue() instanceof Set) {
                    editorEdit.remove(entry.getKey());
                }
            }
            editorEdit.remove("fire-count");
            editorEdit.commit();
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized List c() {
        ArrayList arrayList;
        try {
            arrayList = new ArrayList();
            for (Map.Entry<String, ?> entry : this.a.getAll().entrySet()) {
                if (entry.getValue() instanceof Set) {
                    arrayList.add(s.a(entry.getKey(), new ArrayList((Set) entry.getValue())));
                }
            }
            l(System.currentTimeMillis());
        } catch (Throwable th) {
            throw th;
        }
        return arrayList;
    }

    synchronized boolean f(long j, long j2) {
        return d(j).equals(d(j2));
    }

    synchronized void g() {
        String strD = d(System.currentTimeMillis());
        this.a.edit().putString("last-used-date", strD).commit();
        h(strD);
    }

    synchronized boolean i(long j) {
        return j("fire-global", j);
    }

    synchronized boolean j(String str, long j) {
        if (!this.a.contains(str)) {
            this.a.edit().putLong(str, j).commit();
            return true;
        }
        if (f(this.a.getLong(str, -1L), j)) {
            return false;
        }
        this.a.edit().putLong(str, j).commit();
        return true;
    }

    synchronized void k(long j, String str) {
        try {
            String strD = d(j);
            if (this.a.getString("last-used-date", "").equals(strD)) {
                return;
            }
            long j2 = this.a.getLong("fire-count", 0L);
            if (j2 + 1 == 30) {
                a();
                j2 = this.a.getLong("fire-count", 0L);
            }
            HashSet hashSet = new HashSet(this.a.getStringSet(str, new HashSet()));
            hashSet.add(strD);
            this.a.edit().putStringSet(str, hashSet).putLong("fire-count", j2 + 1).putString("last-used-date", strD).commit();
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized void l(long j) {
        this.a.edit().putLong("fire-global", j).commit();
    }
}
