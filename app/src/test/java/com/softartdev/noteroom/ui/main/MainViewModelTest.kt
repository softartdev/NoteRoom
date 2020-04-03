package com.softartdev.noteroom.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.noteroom.data.NoteUseCase
import com.softartdev.noteroom.database.Note
import com.softartdev.noteroom.util.MainCoroutineRule
import com.softartdev.noteroom.util.assertValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.sqlcipher.database.SQLiteException
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val noteUseCase = Mockito.mock(NoteUseCase::class.java)

    @Test
    fun success() = mainCoroutineRule.runBlockingTest {
        val notes = emptyList<Note>()
        Mockito.`when`(noteUseCase.getNotes()).thenReturn(notes)
        pauseDispatcher()
        val mainViewModel = MainViewModel(noteUseCase)
        mainViewModel.resultLiveData.assertValues(
                NoteListResult.Loading,
                NoteListResult.Success(notes)
        ) {
            resumeDispatcher()
        }
    }

    @Test
    fun navMain() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(noteUseCase.getNotes()).thenThrow(SQLiteException::class.java)
        pauseDispatcher()
        val mainViewModel = MainViewModel(noteUseCase)
        mainViewModel.resultLiveData.assertValues(
                NoteListResult.Loading,
                NoteListResult.NavMain
        ){
            resumeDispatcher()
        }
    }

    @Test
    fun error() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(noteUseCase.getNotes()).thenAnswer { throw Throwable() }
        pauseDispatcher()
        val mainViewModel = MainViewModel(noteUseCase)
        mainViewModel.resultLiveData.assertValues(
                NoteListResult.Loading,
                NoteListResult.Error(null)
        ) {
            resumeDispatcher()
        }
    }
}