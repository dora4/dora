/*
 * Copyright (C) 2024 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.keepalive.service;

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

import java.util.Random;

import dora.keepalive.KeepAlive;
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
        if (VERSION.SDK_INT >= 26 && KeepAlive.foregroundNotification != null) {
            localIntent = new Intent(this.getApplicationContext(), NotificationClickReceiver.class);
            localIntent.setAction("CLICK_NOTIFICATION");
            Notification notification = NotificationUtils.createNotification(this, KeepAlive.foregroundNotification.getTitle(), KeepAlive.foregroundNotification.getDescription(), KeepAlive.foregroundNotification.getIconRes(), localIntent);
            this.startForeground(new Random().nextInt(65535), notification);
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
