package com.softartdev.noteroom.ui


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.softartdev.noteroom.R
import com.softartdev.noteroom.db.RoomDbRepository
import com.softartdev.noteroom.ui.splash.SplashActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateRemove {

    @Rule
    @JvmField
    var activityTestRule = object : ActivityTestRule<SplashActivity>(SplashActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            val context = ApplicationProvider.getApplicationContext<Context>()
            context.deleteDatabase(RoomDbRepository.DB_NAME)
        }
    }

    @Test
    fun createRemove() {
        val floatingActionButton = onView(withId(R.id.add_note_fab))
        floatingActionButton.perform(click())

        Thread.sleep(500)

        val textInputEditText = onView(withId(R.id.note_title_edit_text))
        val titleText = "Lorem"
        textInputEditText.perform(replaceText(titleText))
        textInputEditText.perform(closeSoftKeyboard())

        pressBack()

        Thread.sleep(500)

        val alertDialogPositiveButton = onView(withId(android.R.id.button1))
        alertDialogPositiveButton.perform(click())

        Thread.sleep(500)

        val textView = onView(withId(R.id.item_note_title_text_view))
        textView.check(matches(withText(titleText)))

        textView.perform(click())

        val actionMenuItemView = onView(withId(R.id.action_delete_note))
        actionMenuItemView.perform(click())

        alertDialogPositiveButton.perform(click())

        Thread.sleep(500)

        val textView2 = onView(withId(R.id.text_message))
        textView2.check(matches(withText(R.string.label_empty_result)))
    }

}
