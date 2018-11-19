package com.evernote.android.job.sample.sync

import android.content.Context
import androidx.concurrent.futures.ResolvableFuture
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import java.util.UUID

/**
 * @author rwondratschek
 */

interface UploadApi {
    fun uploadImage(image: Image): Completable
}

object DefaultUploadApi : UploadApi {
    override fun uploadImage(image: Image): Completable {
        return Completable.complete()
    }
}

class AsyncImageUploadWorker(context: Context, params: WorkerParameters) : ListenableWorker(context, params) {
    private val api: UploadApi = DefaultUploadApi
    private var disposable: Disposable? = null

    override fun startWork(): ListenableFuture<Payload> {
        val future = ResolvableFuture.create<Payload>()

        val imageId = inputData.getInt(EXTRA_IMAGE, -1)
        if (imageId <= 0) {
            future.set(Payload(Result.FAILURE))
            return future
        }

        disposable = api
            .uploadImage(Image((imageId)))
            .subscribe({
                future.set(Payload(Result.SUCCESS))
            }, { error ->
                future.setException(error)
            })

        return future
    }

    override fun onStopped() {
        disposable?.dispose()
    }

    companion object {
        fun schedule(image: Image): UUID {
            val uploadRequest = OneTimeWorkRequestBuilder<AsyncImageUploadWorker>()
                .setInputData(workDataOf(EXTRA_IMAGE to image.id))
                .build()

            WorkManager.getInstance().enqueue(uploadRequest)

            return uploadRequest.id
        }
    }
}

class TestWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Payload {
        return Payload(Result.SUCCESS)
    }
}