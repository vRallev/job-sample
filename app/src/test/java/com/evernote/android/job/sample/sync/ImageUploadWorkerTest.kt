package com.evernote.android.job.sample.sync

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
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
        val context = mock<Application> { }

        val inputData = workDataOf(EXTRA_IMAGE to IntArray(5)) // fake image ID
        val workerParameters = mock<WorkerParameters> {
            on { this.inputData } doReturn inputData
        }

        val worker = ImageUploadWorker(context, workerParameters)

        val result = worker.doWork()

        assertThat(result).isEqualTo(ListenableWorker.Result.SUCCESS)
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