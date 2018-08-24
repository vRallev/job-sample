package com.evernote.android.job.sample.reminder

import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit

/**
 * @author rwondratschek
 */
class DailyUpdateJob : DailyJob() {
    override fun onRunDailyJob(params: Params): DailyJobResult {
        // download and show updates
        return DailyJobResult.SUCCESS
    }

    companion object {

        const val TAG = "DailyUpdateJob"

        fun schedule() {
            if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
                return // already scheduled
            }

            val builder = JobRequest.Builder(TAG) // add more requirements
            DailyJob.scheduleAsync(
                builder,
                TimeUnit.HOURS.toMillis(5), // between 5am and 8am
                TimeUnit.HOURS.toMillis(8)
            )
        }
    }
}