package com.softartdev.noteroom.ui


import android.content.Context
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matcher
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
@UseExperimental(ExperimentalCoroutinesApi::class)
class CreateRemoveNoteWithDaoTest {

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun createRemoveNoteWithDao() = runBlockingTest {
        val messageTextView = onView(withId(R.id.text_message))
        messageTextView.check(matches(withText(R.string.label_empty_result)))

        var act = noteDao.getNotes()
        var exp = emptyList<Note>()
        assertEquals(exp, act)

        val expId = note.id + 1
        val actId = noteDao.insertNote(note)
        assertEquals(expId, actId)

        act = noteDao.getNotes()
        exp = listOf(note.copy(id = expId))
        assertEquals(exp, act)

        val swipeRefresh = onView(withId(R.id.main_swipe_refresh))
        swipeRefresh.perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))

        messageTextView.check(matches(withEffectiveVisibility(Visibility.GONE)))

        val noteTitleTextView = onView(withId(R.id.item_note_title_text_view))
        noteTitleTextView.check(matches(withText(note.title)))

        val actDeleted = noteDao.deleteNoteById(expId)
        val expDeleted = 1
        assertEquals(expDeleted, actDeleted)

        act = noteDao.getNotes()
        exp = emptyList()
        assertEquals(exp, act)

        swipeRefresh.perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))

        messageTextView.check(matches(isDisplayed()))
    }

    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> = constraints
        override fun getDescription(): String = action.description
        override fun perform(uiController: UiController?, view: View?) = action.perform(uiController, view)
    }
}
