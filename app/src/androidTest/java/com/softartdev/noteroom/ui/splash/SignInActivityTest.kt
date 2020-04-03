package com.softartdev.noteroom.ui.splash


import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.google.android.material.textfield.TextInputLayout
import com.softartdev.noteroom.R
import com.softartdev.noteroom.data.SafeRepo
import com.softartdev.noteroom.util.EspressoIdlingResource
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

@LargeTest
@RunWith(AndroidJUnit4::class)
class SignInActivityTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val password = "password"

    @Rule
    @JvmField
    var activityTestRule = object : ActivityTestRule<SplashActivity>(SplashActivity::class.java) {
        override fun beforeActivityLaunched() {
            val safeRepo by inject(SafeRepo::class.java)
            while (safeRepo.databaseState == SQLCipherUtils.State.DOES_NOT_EXIST) {
                val db = safeRepo.buildDatabaseInstanceIfNeed()
                val notes = runBlocking { db.noteDao().getNotes() }
                Timber.d("notes = %s", notes)
                Timber.d("databaseState = %s", safeRepo.databaseState.name)
            }
            safeRepo.encrypt(SpannableStringBuilder(password))
        }
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun signInActivityTest() {
        togglePasswordVisibility(R.id.password_text_input_layout)

        val passwordEditText = onView(withId(R.id.password_edit_text))
                .check(matches(isDisplayed()))
                .check(matches(withHint(R.string.enter_password)))

        passwordEditText.perform(replaceText("incorrect password"), closeSoftKeyboard())

        val signInButton = onView(withId(R.id.sign_in_button))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.sign_in)))
        signInButton.perform(scrollTo(), click())

        val passwordTextInputLayout = onView(withId(R.id.password_text_input_layout))
                .check(matches(isDisplayed()))
        passwordTextInputLayout.check(matches(withError(R.string.incorrect_password)))

        passwordEditText.perform(scrollTo(), replaceText(password))

        signInButton.perform(scrollTo(), click())

        val messageTextView = onView(withId(R.id.text_message))
        messageTextView.check(matches(withText(R.string.label_empty_result)))
        messageTextView.check(matches(isDisplayed()))
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
