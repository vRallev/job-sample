package com.evernote.android.job.sample.sync;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author rwondratschek
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SyncJobJobScheduler extends JobService {

    private static final int JOB_ID = 1;

    public static void schedule(Context context) {
        long interval = TimeUnit.HOURS.toMillis(6);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(context, SyncJobJobScheduler.class))
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            long flex = TimeUnit.HOURS.toMillis(3);
            builder.setPeriodic(interval, flex);
        } else {
            builder.setPeriodic(interval);
        }

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // called on main thread
        new Thread(() -> {
            try {
                new SyncEngine().syncReminders();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                jobFinished(params, false); // don't forget to call
            }
        }).start();

        return true; // we have background work
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false; // don't reschedule
    }
}
