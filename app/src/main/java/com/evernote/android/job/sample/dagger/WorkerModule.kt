package com.evernote.android.job.sample.dagger

import android.content.Context
import androidx.work.WorkerParameters
import dagger.Module
import dagger.Provides

/**
 * @author rwondratschek
 */
@Module
class WorkerModule(
    private val context: Context,
    private val params: WorkerParameters
) {
    @Provides fun provideContext(): Context = context
    @Provides fun provideParams(): WorkerParameters = params
}