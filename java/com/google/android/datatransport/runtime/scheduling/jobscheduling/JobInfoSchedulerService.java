package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Base64;
import m.p;
import m.u;
import t.e;
import x.a;

public class JobInfoSchedulerService extends JobService {
    public static void a(JobInfoSchedulerService jobInfoSchedulerService, JobParameters jobParameters) {
        jobInfoSchedulerService.b(jobParameters);
    }

    private void b(JobParameters jobParameters) {
        jobFinished(jobParameters, false);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        String string = jobParameters.getExtras().getString("backendName");
        String string2 = jobParameters.getExtras().getString("extras");
        int i = jobParameters.getExtras().getInt("priority");
        int i2 = jobParameters.getExtras().getInt("attemptNumber");
        u.f(getApplicationContext());
        p.a aVarD = p.a().b(string).d(a.b(i));
        if (string2 != null) {
            aVarD.c(Base64.decode(string2, 0));
        }
        u.c().e().v(aVarD.a(), i2, new e(this, jobParameters));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
