package dora.keepalive;

import android.app.Notification;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;

import dora.keepalive.receiver.NotificationClickReceiver;
import dora.util.NotificationUtils;
import dora.util.ProcessUtils;

public final class JobHandlerService extends JobService {

    private JobScheduler mJobScheduler;
    private int mJobId = 100;

    public JobHandlerService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startService(this);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mJobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            this.mJobScheduler.cancel(this.mJobId);
            Builder builder = new Builder(this.mJobId, new ComponentName(this.getPackageName(), JobHandlerService.class.getName()));
            if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setMinimumLatency(30000L);
                builder.setOverrideDeadline(30000L);
                builder.setMinimumLatency(30000L);
                builder.setBackoffCriteria(30000L, JobInfo.BACKOFF_POLICY_LINEAR);
            } else {
                builder.setPeriodic(30000L);
            }

            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setPersisted(true);
            this.mJobScheduler.schedule(builder.build());
        }
        return START_STICKY;
    }

    private void startService(Context context) {
        Intent localIntent;
        if (VERSION.SDK_INT >= 26 && KeepLive.foregroundNotification != null) {
            localIntent = new Intent(this.getApplicationContext(), NotificationClickReceiver.class);
            localIntent.setAction("CLICK_NOTIFICATION");
            Notification notification = NotificationUtils.createNotification(this, KeepLive.foregroundNotification.getTitle(), KeepLive.foregroundNotification.getDescription(), KeepLive.foregroundNotification.getIconRes(), localIntent);
            this.startForeground(13691, notification);
        }
        localIntent = new Intent(context, LocalService.class);
        Intent guardIntent = new Intent(context, RemoteService.class);
        this.startService(localIntent);
        this.startService(guardIntent);
    }

    public boolean onStartJob(JobParameters jobParameters) {
        if (!ProcessUtils.isTaskRunning(this.getApplicationContext(), "dora.keepalive.service.LocalService") || !ProcessUtils.isRunningTaskExists(this.getApplicationContext(), this.getPackageName() + ":remote")) {
            this.startService(this);
        }
        return false;
    }

    public boolean onStopJob(JobParameters jobParameters) {
        if (!ProcessUtils.isTaskRunning(this.getApplicationContext(), "dora.keepalive.service.LocalService") || !ProcessUtils.isRunningTaskExists(this.getApplicationContext(), this.getPackageName() + ":remote")) {
            this.startService(this);
        }
        return false;
    }
}
