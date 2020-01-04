package com.softartdev.noteroom.di.module

import android.content.Context
import com.softartdev.noteroom.NoteRoomApp
import com.softartdev.noteroom.util.PreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    internal fun provideAppContext(app: NoteRoomApp): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    internal fun providePreferencesHelper(context: Context): PreferencesHelper {
        return PreferencesHelper(context)
    }

}
