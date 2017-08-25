package com.evernote.android.job.sample.sync;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.sample.BuildConfig;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author rwondratschek
 */
@SuppressWarnings("UnusedReturnValue")
public class SyncJob extends Job {

    public static final String TAG = "SyncJob";

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static int schedule() {
        Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(TAG);
        if (!jobRequests.isEmpty()) {
            return jobRequests.iterator().next().getJobId();
        }

        long interval = TimeUnit.HOURS.toMillis(6); // every 6 hours
        long flex = TimeUnit.HOURS.toMillis(3); // wait 3 hours before job runs again

        if (DEBUG) {
            interval = JobRequest.MIN_INTERVAL;
            flex = JobRequest.MIN_FLEX;
        }

        return new JobRequest.Builder(TAG)
                .setPeriodic(interval, flex)
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .setRequirementsEnforced(true)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        try {
            new SyncEngine().syncReminders();
            return Result.SUCCESS;

        } catch (IOException e) {
            Log.e("Sync", e.getMessage(), e);
            return Result.FAILURE;
        }
    }
}
