package com.softartdev.noteroom.ui


import android.content.Context
import android.view.View
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.softartdev.noteroom.R
import com.softartdev.noteroom.db.NoteDao
import com.softartdev.noteroom.db.NoteDatabase
import com.softartdev.noteroom.db.RoomDbRepository
import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.ui.splash.SplashActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateRemoveNoteWithDaoTest {

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()

    @Rule
    @JvmField
    var activityTestRule = object : ActivityTestRule<SplashActivity>(SplashActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            context.deleteDatabase(RoomDbRepository.DB_NAME)
        }
    }
    private lateinit var db: NoteDatabase
    private lateinit var noteDao: NoteDao
    private val note = Note(
            id = 0,
            title = "Test title",
            text = "Test text",
            dateCreated = Date(),
            dateModified = Date()
    )

    @Before
    fun createDb() {
        db = Room.databaseBuilder(context, NoteDatabase::class.java, RoomDbRepository.DB_NAME).build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun createRemoveNoteWithDao() {
        val messageTextView = onView(withId(R.id.text_message))
        messageTextView.check(matches(withText(R.string.label_empty_result)))

        val expId = note.id + 1
        noteDao.insertNote(note)
                .test()
                .assertValue(expId)
        val exp = note.copy(id = expId)
        noteDao.getNoteById(expId)
                .test()
                .assertValue(exp)

        val swipeRefresh = onView(withId(R.id.main_swipe_refresh))
        swipeRefresh.perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))

        messageTextView.check(matches(withEffectiveVisibility(Visibility.GONE)))

        val noteTitleTextView = onView(withId(R.id.item_note_title_text_view))
        noteTitleTextView.check(matches(withText(note.title)))

        noteDao.deleteNoteById(expId)
                .test()
                .assertNoErrors()
                .assertTerminated()

        swipeRefresh.perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))

        messageTextView.check(matches(isDisplayed()))
    }

    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> = constraints
        override fun getDescription(): String = action.description
        override fun perform(uiController: UiController?, view: View?) = action.perform(uiController, view)
    }
}
