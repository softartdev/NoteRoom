package com.softartdev.noteroom

import android.content.Context
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.softartdev.noteroom.di.component.DaggerAppComponent
import com.softartdev.noteroom.util.CrashlyticsTree
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

    override fun applicationInjector(): AndroidInjector<NoteRoomApp> = DaggerAppComponent.factory().create(this)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initLogging()
        ThemeHelper.applyTheme(preferencesHelper.themeEntry, this)
    }

    private fun initLogging() {
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsTree())

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val kit = Crashlytics.Builder().core(core).build()
        Fabric.with(this, kit, Answers())
    }

}
