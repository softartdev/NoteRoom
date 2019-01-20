package com.softartdev.noteroom

import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.Assert.assertEquals

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        assertEquals("com.softartdev.noteroom", getApplicationContext<Context>().packageName)
    }
}
