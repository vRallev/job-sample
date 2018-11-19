package com.evernote.android.job.sample.dagger

import com.evernote.android.job.sample.sync.DefaultUploadApi
import com.evernote.android.job.sample.sync.UploadApi
import dagger.Module
import dagger.Provides

/**
 * @author rwondratschek
 */
@Module
object ApiModule {
    @Provides
    @JvmStatic
    fun provideApi(): UploadApi = DefaultUploadApi
}