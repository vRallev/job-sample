package com.evernote.android.job.sample.reminder;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

/**
 * @author rwondratschek
 */
public class ReminderJob extends Job {

    public static final String TAG = "ReminderJob";

    private static final String EXTRA_ID = "EXTRA_ID";

    /*package*/ static int schedule(@NonNull Reminder reminder) {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putInt(EXTRA_ID, reminder.getId());

        long time = Math.max(1L, reminder.getTimestamp() - System.currentTimeMillis());

        return new JobRequest.Builder(TAG)
                .setExact(time)
                .setExtras(extras)
                .setUpdateCurrent(false)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        int id = params.getExtras().getInt(EXTRA_ID, -1);
        if (id < 0) {
            return Result.FAILURE;
        }

        Reminder reminder = ReminderEngine.instance().getReminderById(id);
        if (reminder == null) {
            return Result.FAILURE;
        }

        int index = ReminderEngine.instance().getReminders().indexOf(reminder);

        ReminderEngine.instance().showReminder(reminder);
        ReminderEngine.instance().removeReminder(index, false);

        return Result.SUCCESS;
    }
}
