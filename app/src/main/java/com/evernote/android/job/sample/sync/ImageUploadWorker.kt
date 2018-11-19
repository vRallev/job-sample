package com.evernote.android.job.sample.sync

import android.content.Context
import androidx.work.ArrayCreatingInputMerger
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.setInputMerger
import androidx.work.workDataOf
import java.util.UUID

/**
 * @author rwondratschek
 */

const val EXTRA_IMAGE = "EXTRA_IMAGE"

class ImageCompressWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val imageId = inputData.getInt(EXTRA_IMAGE, -1)
        if (imageId <= 0) return Result.FAILURE

        val newImageId = compressImage(imageId)
        outputData = workDataOf(EXTRA_IMAGE to newImageId)
        return Result.SUCCESS
    }

    private fun compressImage(imageId: Int): Int = imageId

}

class ImageUploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val imageId = inputData.getIntArray(EXTRA_IMAGE) ?: return Result.FAILURE

        imageId.forEach { uploadImage(it) }

        return Result.SUCCESS
    }

    private fun uploadImage(imageId: Int) {}

    companion object {

        fun schedule(images: Array<Image>): UUID {
            val compressRequests = images.map { image ->
                OneTimeWorkRequestBuilder<ImageCompressWorker>()
                    .setInputData(
                        workDataOf(
                            EXTRA_IMAGE to image.id
                        )
                    )
                    .build()
            }

            val uploadRequest = OneTimeWorkRequestBuilder<ImageUploadWorker>()
                .setInputMerger(ArrayCreatingInputMerger::class)
                .build()

            WorkManager.getInstance()
                .beginWith(compressRequests)
                .then(uploadRequest)
                .enqueue()

            return uploadRequest.id
        }
    }
}