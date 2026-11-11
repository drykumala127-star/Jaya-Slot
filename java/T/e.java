package t;

import android.app.job.JobParameters;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService;

public final class e implements Runnable {
    public final JobInfoSchedulerService i;
    public final JobParameters j;

    public e(JobInfoSchedulerService jobInfoSchedulerService, JobParameters jobParameters) {
        this.i = jobInfoSchedulerService;
        this.j = jobParameters;
    }

    @Override
    public final void run() {
        JobInfoSchedulerService.a(this.i, this.j);
    }
}
