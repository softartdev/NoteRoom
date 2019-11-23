package com.softartdev.noteroom

import android.content.Context
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.softartdev.noteroom.di.component.DaggerAppComponent
import com.softartdev.noteroom.util.PreferencesHelper
import com.softartdev.noteroom.util.ThemeHelper
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

class NoteRoomApp : DaggerApplication() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun applicationInjector(): AndroidInjector<NoteRoomApp> = DaggerAppComponent.builder().create(this)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val crashlyticsCore = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val crashlytics = Crashlytics.Builder().core(crashlyticsCore).build()
        Fabric.with(this, crashlytics)

        ThemeHelper.applyTheme(preferencesHelper.themeEntry, this)
    }

}
