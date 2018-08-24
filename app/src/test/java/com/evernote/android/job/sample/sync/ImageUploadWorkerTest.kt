package com.evernote.android.job.sample.sync

import androidx.work.Worker
import com.evernote.android.job.Job
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

/**
 * @author rwondratschek
 */
class ImageUploadWorkerTest {

    @Test
    fun `verify worker uploads images`() {
        val worker = ImageUploadWorker()
//        worker.inputData = workDataOf() // doesn't work :(

        val result = worker.doWork()

        assertThat(result).isEqualTo(Worker.Result.SUCCESS)
    }

    @Test
    fun `verify job uploads images`() {
        val extras = PersistableBundleCompat().apply {
            putInt(ImageUploadJob.EXTRA_IMAGE, 5)
        }
        val params = mock<Job.Params> { on { this.extras } doReturn extras }

        val result = ImageUploadJob().onRunJob(params)

        assertThat(result).isEqualTo(Job.Result.SUCCESS)
    }
}