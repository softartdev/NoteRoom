package com.softartdev.noteroom.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.noteroom.util.MainCoroutineRule
import com.softartdev.noteroom.util.assertValues
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("EXPERIMENTAL_API_USAGE")
class NoteViewModelStubTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val noteUseCase = NoteUseCaseStub()

    private var noteViewModel = NoteViewModel(noteUseCase)

    private val id = 1L
    private val title: String = "title"
    private val text: String = "text"

    @Before
    fun setUp() {
        noteUseCase.inflate()
    }

    @After
    fun tearDown() {
        noteViewModel.resultLiveData.value = null
        noteUseCase.clear()
    }

    @Test
    fun createNote() = noteViewModel.resultLiveData.assertValues(
            NoteResult.Loading,
            NoteResult.Created(noteUseCase.notes.last().id.inc())
    ) {
        noteViewModel.createNote()
    }

    @Test
    fun loadNote() = noteViewModel.resultLiveData.assertValues(
            NoteResult.Loading,
            NoteResult.Loaded(noteUseCase.notes.first())
    ) {
        noteViewModel.loadNote(id)
    }

    @Test
    fun saveNoteEmpty() = noteViewModel.resultLiveData.assertValues(
            NoteResult.Loading,
            NoteResult.Empty
    ) {
        noteViewModel.saveNote("", "")
    }

    @Test
    fun saveNote() = noteViewModel.resultLiveData.assertValues(
            NoteResult.Loading,
            NoteResult.Saved(title)
    ) {
        noteViewModel.setIdForTest(id)
        noteViewModel.saveNote(title, text)
    }

    @Test
    fun editTitle() {
        noteViewModel.resultLiveData.assertValues(
                NoteResult.Loading,
                NoteResult.NavEditTitle(id),
                NoteResult.TitleUpdated(title)
        ) {
            noteViewModel.setIdForTest(id)
            noteViewModel.editTitle()
            runBlocking { noteUseCase.titleChannel.send(title) }
        }
    }

    @Test
    fun deleteNote() = noteViewModel.resultLiveData.assertValues(
            NoteResult.Loading,
            NoteResult.Deleted
    ) {
        noteViewModel.setIdForTest(id)
        noteViewModel.deleteNote()
    }

    @Test
    fun checkSaveChange() = mainCoroutineRule.runBlockingTest {
        noteViewModel.resultLiveData.assertValues(
                NoteResult.Loading,
                NoteResult.CheckSaveChange
        ) {
            noteViewModel.setIdForTest(id)
            noteViewModel.checkSaveChange(title, text)
        }
    }

    @Test
    fun saveNoteAndNavBack() = noteViewModel.resultLiveData.assertValues(
            NoteResult.Loading,
            NoteResult.NavBack
    ) {
        noteViewModel.setIdForTest(id)
        noteViewModel.saveNoteAndNavBack(title, text)
    }

    @Test
    fun doNotSaveAndNavBack() = mainCoroutineRule.runBlockingTest {
        noteViewModel.resultLiveData.assertValues(
                NoteResult.Loading,
                NoteResult.NavBack
        ) {
            noteViewModel.setIdForTest(id)
            noteViewModel.doNotSaveAndNavBack()
        }
    }

    @Test
    fun errorResult() {
        assertEquals(NoteResult.Error(null), noteViewModel.errorResult(Throwable()))
    }
}