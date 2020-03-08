package com.softartdev.noteroom

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.softartdev.noteroom.di.appModule
import com.softartdev.noteroom.di.mvvmModule
import com.softartdev.noteroom.util.PreferencesHelper
import com.softartdev.noteroom.util.ThemeHelper
import com.softartdev.noteroom.util.log.CrashlyticsTree
import com.softartdev.noteroom.util.log.TimberKoinLogger
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class NoteRoomApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initLogging()
        startKoin {
            logger(TimberKoinLogger(Level.DEBUG))
            androidContext(this@NoteRoomApp)
            modules(appModule + mvvmModule)
        }
        val preferencesHelper: PreferencesHelper = get()
        ThemeHelper.applyTheme(preferencesHelper.themeEntry, this)
    }

    private fun initLogging() {
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsTree())

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val kit = Crashlytics.Builder().core(core).build()
        Fabric.with(this, kit, Answers())
    }

}
