package com.evernote.android.job.sample.reminder

import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat

/**
 * @author rwondratschek
 */
class ReminderJob : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        val id = params.extras.getInt(EXTRA_ID, -1)
        if (id < 0) {
            return Job.Result.FAILURE
        }

        val reminder = ReminderEngine.getReminderById(id) ?: return Job.Result.FAILURE
        val index = ReminderEngine.getReminders().indexOf(reminder)

        ReminderEngine.showReminder(reminder)
        ReminderEngine.removeReminder(index, false)

        return Job.Result.SUCCESS
    }

    companion object {

        const val TAG = "ReminderJob"
        private const val EXTRA_ID = "EXTRA_ID"

        fun schedule(reminder: Reminder): Int {
            val time = Math.max(1L, reminder.timestamp - System.currentTimeMillis())

            return JobRequest.Builder(TAG)
                .setExact(time)
                .setExtras(PersistableBundleCompat().apply {
                    putInt(EXTRA_ID, reminder.id)
                })
                .setUpdateCurrent(false)
                .build()
                .schedule()
        }
    }
}
