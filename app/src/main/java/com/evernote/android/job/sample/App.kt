package com.evernote.android.job.sample

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.evernote.android.job.JobApi
import com.evernote.android.job.JobConfig
import com.evernote.android.job.JobManager
import com.evernote.android.job.sample.dagger.ApiModule
import com.evernote.android.job.sample.dagger.SampleDaggerWorker
import com.evernote.android.job.sample.dagger.DaggerWorkerFactory
import com.evernote.android.job.sample.dagger.WorkerModule
import com.evernote.android.job.sample.sync.SyncJob
import dagger.Component
import dagger.Subcomponent
import java.util.UUID
import javax.inject.Singleton


/**
 * @author rwondratschek
 */
class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        instance = this

        super.onCreate()

        JobConfig.setApiEnabled(JobApi.WORK_MANAGER, false)
        JobManager.create(this).addJobCreator(ImageUploadJobCreator)

        val component = DaggerAppComponent.create()

        val configuration = Configuration.Builder()
            .setWorkerFactory(DaggerWorkerFactory(component))
            .build()

        WorkManager.initialize(this, configuration)

        SyncJob.schedule()
        SampleDaggerWorker.schedule()
    }

    fun observe() {
        val workId = UUID.randomUUID()

        WorkManager.getInstance().getWorkInfoByIdLiveData(workId).observeForever { info: WorkInfo? ->
            info?.state
        }

        WorkManager.getInstance().getWorkInfoById(workId).get()?.state
    }
}

@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun buildWorkerComponent(): WorkerComponent.Builder
}

@Subcomponent(modules = [WorkerModule::class])
interface WorkerComponent {

    fun sampleDaggerWorker(): SampleDaggerWorker

    @Subcomponent.Builder
    interface Builder {
        fun workerModule(module: WorkerModule): Builder
        fun build(): WorkerComponent
    }
}
