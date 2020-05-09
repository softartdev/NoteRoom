package com.softartdev.noteroom.ui.signin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.util.MainCoroutineRule
import com.softartdev.noteroom.util.StubEditable
import com.softartdev.noteroom.util.anyObject
import com.softartdev.noteroom.util.assertValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val cryptUseCase = Mockito.mock(CryptUseCase::class.java)
    private val signInViewModel = SignInViewModel(cryptUseCase)

    @Test
    fun navMain() = mainCoroutineRule.runBlockingTest {
        val pass = StubEditable("pass")
        Mockito.`when`(cryptUseCase.checkPassword(pass)).thenReturn(true)
        signInViewModel.assertValues(
                SignInResult.ShowProgress,
                SignInResult.NavMain
        ) {
            signInViewModel.signIn(pass)
        }
    }

    @Test
    fun showEmptyPassError() = mainCoroutineRule.runBlockingTest {
        signInViewModel.assertValues(
                SignInResult.ShowProgress,
                SignInResult.ShowEmptyPassError
        ) {
            signInViewModel.signIn(pass = StubEditable(""))
        }
    }

    @Test
    fun showIncorrectPassError() = mainCoroutineRule.runBlockingTest {
        val pass = StubEditable("pass")
        Mockito.`when`(cryptUseCase.checkPassword(pass)).thenReturn(false)
        signInViewModel.assertValues(
                SignInResult.ShowProgress,
                SignInResult.ShowIncorrectPassError
        ) {
            signInViewModel.signIn(pass)
        }
    }

    @Test
    fun showError() = mainCoroutineRule.runBlockingTest {
        val throwable = Throwable()
        Mockito.`when`(cryptUseCase.checkPassword(anyObject())).then { throw throwable }
        signInViewModel.assertValues(
                SignInResult.ShowProgress,
                SignInResult.ShowError(throwable)
        ) {
            signInViewModel.signIn(StubEditable("pass"))
        }
    }
}
