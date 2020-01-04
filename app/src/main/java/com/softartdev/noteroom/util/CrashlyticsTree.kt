package com.softartdev.noteroom.util

import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (tag != null) {
            Crashlytics.log(priority, tag, message)
        }
        if (t != null) {
            Crashlytics.logException(t)
        }
    }

}
