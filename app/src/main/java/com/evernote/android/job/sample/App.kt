package com.evernote.android.job.sample

import android.app.Application
import com.evernote.android.job.JobApi
import com.evernote.android.job.JobConfig
import com.evernote.android.job.JobManager
import com.evernote.android.job.sample.sync.SyncJob

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

        SyncJob.schedule()
    }
}
