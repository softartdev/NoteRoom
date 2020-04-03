package com.softartdev.noteroom.ui.settings.security.enter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.util.MainCoroutineRule
import com.softartdev.noteroom.util.StubEditable
import com.softartdev.noteroom.util.assertValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class EnterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val cryptUseCase = Mockito.mock(CryptUseCase::class.java)
    private val enterViewModel = EnterViewModel(cryptUseCase)

    @Test
    fun enterCheckSuccess() = mainCoroutineRule.runBlockingTest {
        val pass = StubEditable("pass")
        Mockito.`when`(cryptUseCase.checkPassword(pass)).thenReturn(true)
        enterViewModel.resultLiveData.assertValues(
                EnterResult.Loading,
                EnterResult.Success
        ) {
            enterViewModel.enterCheck(pass)
        }
    }

    @Test
    fun enterCheckIncorrectPasswordError() = mainCoroutineRule.runBlockingTest {
        val pass = StubEditable("pass")
        Mockito.`when`(cryptUseCase.checkPassword(pass)).thenReturn(false)
        enterViewModel.resultLiveData.assertValues(
                EnterResult.Loading,
                EnterResult.IncorrectPasswordError
        ) {
            enterViewModel.enterCheck(pass)
        }
    }

    @Test
    fun enterCheckEmptyPasswordError() = enterViewModel.resultLiveData.assertValues(
            EnterResult.Loading,
            EnterResult.EmptyPasswordError
    ) {
        enterViewModel.enterCheck(StubEditable(""))
    }

    @Test
    fun errorResult() {
        assertEquals(EnterResult.Error("err"), enterViewModel.errorResult(Throwable("err")))
    }
}