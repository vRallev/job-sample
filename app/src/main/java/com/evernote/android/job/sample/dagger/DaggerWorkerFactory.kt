package com.evernote.android.job.sample.dagger

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.evernote.android.job.sample.AppComponent

/**
 * @author rwondratschek
 */
class DaggerWorkerFactory(
    private val appComponent: AppComponent
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            SampleDaggerWorker::class.java.name -> appComponent
                .buildWorkerComponent()
                .workerModule(WorkerModule(appContext, workerParameters))
                .build()
                .sampleDaggerWorker()

            else -> null
        }
    }
}

//object CustomWorkerFactory : WorkerFactory() {
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters): ListenableWorker? {
//        return // ...
//    }
//}