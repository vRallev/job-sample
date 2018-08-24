package com.evernote.android.job.sample

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.sample.sync.ImageUploadJob
import com.evernote.android.job.sample.sync.SyncJob

/**
 * @author rwondratschek
 */
object ImageUploadJobCreator : JobCreator {

    override fun create(tag: String): Job? {
        return when (tag) {
            ImageUploadJob.TAG -> ImageUploadJob()
            SyncJob.TAG -> SyncJob()
            else -> null
        }
    }
}
