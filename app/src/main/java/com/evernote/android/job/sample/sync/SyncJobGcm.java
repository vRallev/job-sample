package com.evernote.android.job.sample.sync;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author rwondratschek
 */
public class SyncJobGcm extends GcmTaskService {

    private static final String TAG = "SyncJob";

    public static void schedule(Context context) {
        long interval = TimeUnit.HOURS.toMillis(6);
        long flex = TimeUnit.HOURS.toMillis(3);

        PeriodicTask task = new PeriodicTask.Builder()
                .setTag(TAG)
                .setService(SyncJobGcm.class)
                .setRequiresCharging(true)
                .setRequiredNetwork(Task.NETWORK_STATE_UNMETERED)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setPeriod(interval / 1_000)
                .setFlex(flex / 1_000)
                .build();

        GcmNetworkManager.getInstance(context).schedule(task);
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        try {
            new SyncEngine().syncReminders();
            return GcmNetworkManager.RESULT_SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return GcmNetworkManager.RESULT_FAILURE;
        }
    }
}
