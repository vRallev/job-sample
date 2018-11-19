package com.evernote.android.job.sample.sync

import android.support.test.InstrumentationRegistry
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author rwondratschek
 */
class ImageUploadTest {

    @Test
    fun verifyWorkerUploadsImages() {
        WorkManagerTestInitHelper.initializeTestWorkManager(InstrumentationRegistry.getTargetContext())

        val uuid = ImageUploadWorker.schedule(
            arrayOf(
                Image(5), Image(6), Image(7)
            )
        )

        val workInfo = WorkManager.getInstance().getWorkInfoById(uuid).get()

        assertEquals(workInfo?.state, WorkInfo.State.SUCCEEDED)
    }
}