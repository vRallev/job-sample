package com.evernote.android.job.sample.dagger

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.sample.sync.Image
import com.evernote.android.job.sample.sync.ImageCompressWorker
import com.evernote.android.job.sample.sync.UploadApi
import com.evernote.android.job.util.support.PersistableBundleCompat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author rwondratschek
 */
class SampleDaggerWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters,
    private val api: UploadApi
) : Worker(context, workerParams) {

    override fun doWork(): Result = Result.SUCCESS


    // ...

    companion object {
        fun schedule() {
            val request = OneTimeWorkRequestBuilder<SampleDaggerWorker>()
                .setInitialDelay(3, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance().enqueue(request)
        }
    }
}