package com.evernote.android.job.sample.sync

import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.sample.BuildConfig
import java.util.concurrent.TimeUnit

/**
 * @author rwondratschek
 */
class SyncJob : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        SyncEngine(context).syncReminders()
        return Job.Result.SUCCESS
    }

    companion object {

        const val TAG = "SyncJob"

        fun schedule(): Int {
            val jobRequests = JobManager.instance().getAllJobRequestsForTag(TAG)
            if (!jobRequests.isEmpty()) {
                return jobRequests.iterator().next().jobId
            }

            return JobRequest.Builder(TAG)
                .apply {
                    if (BuildConfig.DEBUG) {
                        setPeriodic(JobRequest.MIN_INTERVAL, JobRequest.MIN_FLEX)
                    } else {
                        setPeriodic(
                            TimeUnit.HOURS.toMillis(6), // every 6 hours
                            TimeUnit.HOURS.toMillis(3) // wait 3 hours before job runs again
                        )
                    }
                }
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .setRequirementsEnforced(true)
                .build()
                .schedule()
        }
    }
}
