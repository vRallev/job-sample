package com.evernote.android.job.sample;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.sample.reminder.ReminderJob;
import com.evernote.android.job.sample.sync.SyncJob;

/**
 * @author rwondratschek
 */
public class ReminderJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case ReminderJob.TAG:
                return new ReminderJob();

            case SyncJob.TAG:
                return new SyncJob();

            default:
                return null;
        }
    }
}
