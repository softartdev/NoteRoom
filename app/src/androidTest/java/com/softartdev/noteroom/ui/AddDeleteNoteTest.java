package com.softartdev.noteroom.ui;


import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.softartdev.noteroom.R;
import com.softartdev.noteroom.ui.splash.SplashActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddDeleteNoteTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void addDeleteNoteTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_note_fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.note_title_edit_text), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.note_title_edit_text), isDisplayed()));
        appCompatEditText3.perform(replaceText("Lorem ipsum"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.note_edit_text), isDisplayed()));
        appCompatEditText5.perform(replaceText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."), closeSoftKeyboard());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.save_note_fab), isDisplayed()));
        floatingActionButton2.perform(click());

        pressBack();

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_note_title_text_view), withText("Lorem ipsum"),
                        childAtPosition(
                                allOf(withId(R.id.item_note_card_view),
                                        childAtPosition(
                                                withId(R.id.notes_recycler_view),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Lorem ipsum")));

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.add_note_fab), isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.note_title_edit_text), isDisplayed()));
        appCompatEditText7.perform(replaceText("Sed ut perspiciatis"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.note_edit_text), isDisplayed()));
        appCompatEditText9.perform(replaceText("Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas sit, aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos, qui ratione voluptatem sequi nesciunt, neque porro quisquam est, qui dolorem ipsum, quia dolor sit, amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt, ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit, qui in ea voluptate velit esse, quam nihil molestiae consequatur, vel illum, qui dolorem eum fugiat, quo voluptas nulla pariatur? At vero eos et accusamus et iusto odio dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti atque corrupti, quos dolores et quas molestias excepturi sint, obcaecati cupiditate non provident, similique sunt in culpa, qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio, cumque nihil impedit, quo minus id, quod maxime placeat, facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet, ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat."), closeSoftKeyboard());

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.save_note_fab), isDisplayed()));
        floatingActionButton4.perform(click());

        pressBack();

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.item_note_title_text_view), withText("Sed ut perspiciatis"),
                        childAtPosition(
                                allOf(withId(R.id.item_note_card_view),
                                        childAtPosition(
                                                withId(R.id.notes_recycler_view),
                                                1)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Sed ut perspiciatis")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.notes_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText(R.string.action_delete_note), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText(android.R.string.yes)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.notes_recycler_view), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText(R.string.action_delete_note), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText(android.R.string.yes)));
        appCompatButton2.perform(scrollTo(), click());

        onView(withId(R.id.add_note_text_view)).check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
