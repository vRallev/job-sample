package com.evernote.android.job.sample

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.sample.reminder.ReminderJob
import com.evernote.android.job.sample.sync.SyncJob

/**
 * @author rwondratschek
 */
class ReminderJobCreator : JobCreator {

    override fun create(tag: String): Job? {
        return when (tag) {
            ReminderJob.TAG -> ReminderJob()
            SyncJob.TAG -> SyncJob()
            else -> null
        }
    }
}
