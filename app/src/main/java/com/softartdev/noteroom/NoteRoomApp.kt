package com.softartdev.noteroom

import androidx.multidex.MultiDexApplication
import com.softartdev.noteroom.di.appModule
import com.softartdev.noteroom.di.mvvmModule
import com.softartdev.noteroom.util.PreferencesHelper
import com.softartdev.noteroom.util.ThemeHelper
import com.softartdev.noteroom.util.log.CrashlyticsTree
import com.softartdev.noteroom.util.log.TimberKoinLogger
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class NoteRoomApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsTree())
        startKoin {
            logger(TimberKoinLogger(Level.ERROR))//TODO revert to Level.DEBUG after update Koin version above 2.1.6
            androidContext(this@NoteRoomApp)
            modules(appModule + mvvmModule)
        }
        val preferencesHelper: PreferencesHelper = get()
        ThemeHelper.applyTheme(preferencesHelper.themeEntry, this)
    }

}
