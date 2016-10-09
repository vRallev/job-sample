package com.evernote.android.job.sample;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.sample.sync.SyncJob;

/**
 * @author rwondratschek
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new ReminderJobCreator());

        SyncJob.schedule();
    }
}
