package t;

import android.app.job.JobInfo;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import t.c;

public abstract class f {

    public static class a {
        private w.a a;
        private Map b = new HashMap();

        public a a(k.d dVar, b bVar) {
            this.b.put(dVar, bVar);
            return this;
        }

        public f b() {
            if (this.a == null) {
                throw new NullPointerException("missing required property: clock");
            }
            if (this.b.keySet().size() < k.d.values().length) {
                throw new IllegalStateException("Not all priorities have been configured");
            }
            Map map = this.b;
            this.b = new HashMap();
            return f.d(this.a, map);
        }

        public a c(w.a aVar) {
            this.a = aVar;
            return this;
        }
    }

    public static abstract class b {

        public static abstract class a {
            public abstract b a();

            public abstract a b(long j);

            public abstract a c(Set set);

            public abstract a d(long j);
        }

        public static a a() {
            return new c.b().c(Collections.emptySet());
        }

        abstract long b();

        abstract Set c();

        abstract long d();
    }

    public enum c {
        NETWORK_UNMETERED,
        DEVICE_IDLE,
        DEVICE_CHARGING
    }

    private long a(int i, long j) {
        return (long) (Math.pow(3.0d, i - 1) * j * Math.max(1.0d, Math.log(10000.0d) / Math.log((j > 1 ? j : 2L) * ((long) r7))));
    }

    public static a b() {
        return new a();
    }

    static f d(w.a aVar, Map map) {
        return new t.b(aVar, map);
    }

    public static f f(w.a aVar) {
        return b().a(k.d.DEFAULT, b.a().b(30000L).d(86400000L).a()).a(k.d.HIGHEST, b.a().b(1000L).d(86400000L).a()).a(k.d.VERY_LOW, b.a().b(86400000L).d(86400000L).c(i(c.DEVICE_IDLE)).a()).c(aVar).b();
    }

    private static Set i(Object... objArr) {
        return Collections.unmodifiableSet(new HashSet(Arrays.asList(objArr)));
    }

    private void j(JobInfo.Builder builder, Set set) {
        if (set.contains(c.NETWORK_UNMETERED)) {
            builder.setRequiredNetworkType(2);
        } else {
            builder.setRequiredNetworkType(1);
        }
        if (set.contains(c.DEVICE_CHARGING)) {
            builder.setRequiresCharging(true);
        }
        if (set.contains(c.DEVICE_IDLE)) {
            builder.setRequiresDeviceIdle(true);
        }
    }

    public JobInfo.Builder c(JobInfo.Builder builder, k.d dVar, long j, int i) {
        builder.setMinimumLatency(g(dVar, j, i));
        j(builder, ((b) h().get(dVar)).c());
        return builder;
    }

    abstract w.a e();

    public long g(k.d dVar, long j, int i) {
        long jA = j - e().a();
        b bVar = (b) h().get(dVar);
        return Math.min(Math.max(a(i, bVar.b()), jA), bVar.d());
    }

    abstract Map h();
}
