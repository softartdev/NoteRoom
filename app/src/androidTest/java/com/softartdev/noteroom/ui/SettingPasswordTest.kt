package com.softartdev.noteroom.ui


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.splash.SplashActivity
import com.softartdev.noteroom.util.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingPasswordTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun settingPasswordTest() {
        val settingsActionMenuItemView = onView(allOf(
                withId(R.id.action_settings),
                withContentDescription(R.string.settings),
                isDisplayed()))
        settingsActionMenuItemView.perform(click())

        val switch = onView(allOf(
                withId(android.R.id.switch_widget),
                childAtPosition(
                        parentMatcher = allOf(
                                withId(android.R.id.widget_frame),
                                childAtPosition(
                                        parentMatcher = IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                        position = 2)),
                        position = 0),
                isDisplayed()))
        switch.check(matches(not(isChecked())))
        switch.perform(click())

        val confirmPasswordEditText = onView(withId(R.id.set_password_edit_text))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        confirmPasswordEditText.check(matches(withHint(R.string.enter_password)))

        val confirmRepeatPasswordEditText = onView(withId(R.id.repeat_set_password_edit_text))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        confirmRepeatPasswordEditText.check(matches(withHint(R.string.confirm_password)))

        togglePasswordVisibility(R.id.set_password_text_input_layout)

        val confirmOkButton = onView(allOf(
                withId(android.R.id.button1),
                withText(android.R.string.ok),
                childAtPosition(
                        parentMatcher = childAtPosition(
                                parentMatcher = withId(R.id.buttonPanel),
                                position = 0),
                        position = 3)))
        confirmOkButton.perform(click())

        val confirmPasswordTextInputLayout = onView(withId(R.id.set_password_text_input_layout))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        confirmPasswordTextInputLayout.check(matches(withError(R.string.empty_password)))

        confirmPasswordEditText.perform(replaceText("1"), closeSoftKeyboard())

        togglePasswordVisibility(R.id.repeat_set_password_text_input_layout)

        confirmOkButton.perform(click())
        val confirmRepeatPasswordTextInputLayout = onView(withId(R.id.repeat_set_password_text_input_layout))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        confirmRepeatPasswordTextInputLayout.check(matches(withError(R.string.passwords_do_not_match)))

        confirmRepeatPasswordEditText.perform(replaceText("2"), closeSoftKeyboard())

        confirmOkButton.perform(click())

        confirmRepeatPasswordTextInputLayout.check(matches(withError(R.string.passwords_do_not_match)))

        confirmRepeatPasswordEditText.perform(replaceText("1"), closeSoftKeyboard())

        confirmOkButton.perform(click())

        switch.check(matches(isChecked()))

        val passwordPref = onView(allOf(
                childAtPosition(
                        parentMatcher = allOf(
                                withId(R.id.recycler_view),
                                childAtPosition(
                                        parentMatcher = withId(android.R.id.list_container),
                                        position = 0)),
                        position = 4),
                isDisplayed()))
        passwordPref.perform(click())

        val changeOldEditText = onView(withId(R.id.old_password_edit_text))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        changeOldEditText.check(matches(withHint(R.string.enter_old_password)))

        val changeNewEditText = onView(withId(R.id.new_password_edit_text))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        changeNewEditText.check(matches(withHint(R.string.enter_new_password)))

        val changeRepeatNewEditText = onView(withId(R.id.repeat_new_password_edit_text))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        changeRepeatNewEditText.check(matches(withHint(R.string.repeat_new_password)))

        togglePasswordVisibility(R.id.old_password_text_input_layout)

        val changeOkButton = onView(allOf(
                withId(android.R.id.button1),
                withText(android.R.string.ok),
                childAtPosition(
                        parentMatcher = childAtPosition(
                                parentMatcher = withId(R.id.buttonPanel),
                                position = 0),
                        position = 3)))
        changeOkButton.perform(click())

        val changeOldPasswordTextInputLayout = onView(withId(R.id.old_password_text_input_layout))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        changeOldPasswordTextInputLayout.check(matches(withError(R.string.empty_password)))

        changeOldEditText.perform(replaceText("2"), closeSoftKeyboard())

        togglePasswordVisibility(R.id.new_password_text_input_layout)

        changeOkButton.perform(click())
        val changeNewPasswordTextInputLayout = onView(withId(R.id.new_password_text_input_layout))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        changeNewPasswordTextInputLayout.check(matches(withError(R.string.empty_password)))

        changeNewEditText.perform(replaceText("2"), closeSoftKeyboard())

        togglePasswordVisibility(R.id.repeat_new_password_text_input_layout)

        changeOkButton.perform(click())
        val changeRepeatPasswordTextInputLayout = onView(withId(R.id.repeat_new_password_text_input_layout))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        changeRepeatPasswordTextInputLayout.check(matches(withError(R.string.passwords_do_not_match)))

        changeRepeatNewEditText.perform(replaceText("2"), closeSoftKeyboard())

        changeOkButton.perform(click())

        changeOldPasswordTextInputLayout.check(matches(withError(R.string.incorrect_password)))

        changeOldEditText.perform(replaceText("1"), closeSoftKeyboard())

        changeOkButton.perform(click())

        switch.check(matches(isChecked()))

        switch.perform(click())

        val enterPasswordEditText = onView(withId(R.id.enter_password_edit_text))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        enterPasswordEditText.check(matches(withHint(R.string.enter_password)))

        togglePasswordVisibility(R.id.enter_password_text_input_layout)

        val enterOkButton = onView(allOf(
                withId(android.R.id.button1),
                withText(android.R.string.ok),
                childAtPosition(
                        parentMatcher = childAtPosition(
                                parentMatcher = withId(R.id.buttonPanel),
                                position = 0),
                        position = 3)))
        enterOkButton.perform(click())

        val enterPasswordTextInputLayout = onView(withId(R.id.enter_password_text_input_layout))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        enterPasswordTextInputLayout.check(matches(withError(R.string.empty_password)))

        enterPasswordEditText.perform(replaceText("1"), closeSoftKeyboard())

        enterOkButton.perform(click())

        enterPasswordTextInputLayout.check(matches(withError(R.string.incorrect_password)))

        enterPasswordEditText.perform(replaceText("2"), closeSoftKeyboard())

        enterOkButton.perform(click())

        switch.check(matches(isDisplayed()))
        switch.check(matches(not(isChecked())))
    }

    private fun togglePasswordVisibility(textInputLayoutResId: Int) {
        val textInputLayoutFrameLayoutRoot = childAtPosition(
                parentMatcher = withId(textInputLayoutResId),
                position = 0)
        val checkableImageButtonFrameLayoutRoot = childAtPosition(
                parentMatcher = textInputLayoutFrameLayoutRoot,
                position = 2
        )
        val checkableImageButton = onView(allOf(
                withId(R.id.text_input_end_icon),
                childAtPosition(
                        parentMatcher = checkableImageButtonFrameLayoutRoot,
                        position = 0),
                isDisplayed()))
        checkableImageButton.perform(click())
    }

    private fun withError(expectedErrorTextResId: Int): Matcher<View> = withError(
            expectedErrorText = context.getString(expectedErrorTextResId)
    )

    private fun withError(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {
        override fun matchesSafely(item: View): Boolean = when (item) {
            is TextInputLayout -> {
                val actualErrorText = item.error.toString()
                Timber.d("Actual error text: $actualErrorText")
                expectedErrorText == actualErrorText
            }
            else -> false
        }

        override fun describeTo(description: Description) {
            description.appendText("Expected error text: $expectedErrorText")
        }
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> = object : TypeSafeMatcher<View>() {
        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(position)
        }

        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }
    }
}
