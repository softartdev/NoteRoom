package com.softartdev.noteroom.di.module

import android.app.Application
import android.content.Context
import com.softartdev.noteroom.db.DbStore
import com.softartdev.noteroom.db.RoomDbStore
import com.softartdev.noteroom.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun provideDbStore(@ApplicationContext context: Context): DbStore {
        return RoomDbStore(context)
    }
}
