package com.softartdev.noteroom.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.splash.SplashActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateRemove {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun createRemove() {
        val floatingActionButton = onView(
                allOf(withId(R.id.add_note_fab),
                        childAtPosition(
                                allOf(withId(R.id.main_frame),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()))
        floatingActionButton.perform(click())

        Thread.sleep(500)

        val textInputEditText = onView(
                allOf(withId(R.id.note_title_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.note_title_text_input_layout),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText.perform(replaceText("Lorem"))
        textInputEditText.perform(closeSoftKeyboard())

        pressBack()

        Thread.sleep(500)

        val appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)))
        appCompatButton.perform(scrollTo(), click())

        Thread.sleep(500)

        val textView = onView(
                allOf(withId(R.id.item_note_title_text_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_note_card_view),
                                        0),
                                0),
                        isDisplayed()))
        textView.check(matches(withText("Lorem")))

        textView.perform(click())

        val actionMenuItemView = onView(
                allOf(withId(R.id.action_delete_note), withContentDescription("Delete note"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.note_toolbar),
                                        2),
                                0),
                        isDisplayed()))
        actionMenuItemView.perform(click())

        val appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)))
        appCompatButton2.perform(scrollTo(), click())

        Thread.sleep(500)

        val textView2 = onView(
                allOf(withId(R.id.text_message),
                        isDisplayed()))
        textView2.check(matches(withText("The list is empty")))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
