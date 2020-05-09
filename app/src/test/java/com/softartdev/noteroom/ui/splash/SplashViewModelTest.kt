package com.softartdev.noteroom.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.data.SafeSQLiteException
import com.softartdev.noteroom.util.MainCoroutineRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SplashViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val cryptUseCase = Mockito.mock(CryptUseCase::class.java)

    @Test
    fun navSignIn() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenReturn(true)
        val splashViewModel = SplashViewModel(cryptUseCase)
        launch {
            assertEquals(splashViewModel.flow.first(), SplashResult.NavSignIn)
        }.cancel()
    }

    @Test
    fun navMain() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenReturn(false)
        val splashViewModel = SplashViewModel(cryptUseCase)
        launch {
            assertEquals(splashViewModel.flow.first(), SplashResult.NavMain)
        }.cancel()
    }

    @Test
    fun showError() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenThrow(SafeSQLiteException::class.java)
        val splashViewModel = SplashViewModel(cryptUseCase)
        launch {
            assertEquals(splashViewModel.flow.first(), SplashResult.ShowError(null))
        }
    }
}