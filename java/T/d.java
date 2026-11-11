package t;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Base64;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.zip.Adler32;

public class d implements x {
    private final Context a;
    private final u.d b;
    private final f c;

    public d(Context context, u.d dVar, f fVar) {
        this.a = context;
        this.b = dVar;
        this.c = fVar;
    }

    private boolean d(JobScheduler jobScheduler, int i, int i2) {
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            int i3 = jobInfo.getExtras().getInt("attemptNumber");
            if (jobInfo.getId() == i) {
                return i3 >= i2;
            }
        }
        return false;
    }

    @Override
    public void a(m.p pVar, int i) {
        b(pVar, i, false);
    }

    @Override
    public void b(m.p pVar, int i, boolean z) {
        ComponentName componentName = new ComponentName(this.a, (Class<?>) JobInfoSchedulerService.class);
        JobScheduler jobScheduler = (JobScheduler) this.a.getSystemService("jobscheduler");
        int iC = c(pVar);
        if (!z && d(jobScheduler, iC, i)) {
            q.a.b("JobInfoScheduler", "Upload for context %s is already scheduled. Returning...", pVar);
            return;
        }
        long jK = this.b.k(pVar);
        JobInfo.Builder builderC = this.c.c(new JobInfo.Builder(iC, componentName), pVar.d(), jK, i);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt("attemptNumber", i);
        persistableBundle.putString("backendName", pVar.b());
        persistableBundle.putInt("priority", x.a.a(pVar.d()));
        if (pVar.c() != null) {
            persistableBundle.putString("extras", Base64.encodeToString(pVar.c(), 0));
        }
        builderC.setExtras(persistableBundle);
        q.a.c("JobInfoScheduler", "Scheduling upload for context %s with jobId=%d in %dms(Backend next call timestamp %d). Attempt %d", pVar, Integer.valueOf(iC), Long.valueOf(this.c.g(pVar.d(), jK, i)), Long.valueOf(jK), Integer.valueOf(i));
        jobScheduler.schedule(builderC.build());
    }

    int c(m.p pVar) {
        Adler32 adler32 = new Adler32();
        adler32.update(this.a.getPackageName().getBytes(Charset.forName("UTF-8")));
        adler32.update(pVar.b().getBytes(Charset.forName("UTF-8")));
        adler32.update(ByteBuffer.allocate(4).putInt(x.a.a(pVar.d())).array());
        if (pVar.c() != null) {
            adler32.update(pVar.c());
        }
        return (int) adler32.getValue();
    }
}
