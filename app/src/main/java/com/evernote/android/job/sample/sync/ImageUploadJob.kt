package com.evernote.android.job.sample.sync

import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat

/**
 * @author rwondratschek
 */
class ImageUploadJob : Job() {
    override fun onRunJob(params: Params): Result {
        return Result.SUCCESS
    }

    companion object {

        const val TAG = "ImageUploadJob"
        private const val EXTRA_IMAGE = "EXTRA_IMAGE"

        fun schedule(image: Image) {
            val request = JobRequest.Builder(TAG)
                .startNow()
                .setExtras(
                    PersistableBundleCompat().apply {
                        putInt(EXTRA_IMAGE, image.id)
                    })
                .build()

            JobManager.instance().schedule(request)
        }
    }
}

data class Image(val id: Int)