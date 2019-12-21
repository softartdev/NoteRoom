package com.softartdev.noteroom.di.module

import android.content.Context
import com.softartdev.noteroom.data.DataManager
import com.softartdev.noteroom.db.DbStore
import com.softartdev.noteroom.db.RoomDbStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideDbStore(context: Context): DbStore {
        return RoomDbStore(context)
    }

    @Provides
    @Singleton
    internal fun provideDataManager(dbStore: DbStore): DataManager {
        return DataManager(dbStore)
    }
}