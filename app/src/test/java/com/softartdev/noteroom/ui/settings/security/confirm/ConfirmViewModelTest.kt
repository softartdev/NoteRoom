package com.softartdev.noteroom.ui.settings.security.confirm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.util.MainCoroutineRule
import com.softartdev.noteroom.util.StubEditable
import com.softartdev.noteroom.util.assertValues
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ConfirmViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val cryptUseCase = Mockito.mock(CryptUseCase::class.java)
    private val confirmViewModel = ConfirmViewModel(cryptUseCase)

    @Test
    fun conformCheckPasswordsNoMatchError() = confirmViewModel.resultLiveData.assertValues(
            ConfirmResult.Loading,
            ConfirmResult.PasswordsNoMatchError
    ) {
        confirmViewModel.conformCheck(StubEditable("pass"), StubEditable("new pass"))
    }

    @Test
    fun conformCheckEmptyPasswordError() = confirmViewModel.resultLiveData.assertValues(
            ConfirmResult.Loading,
            ConfirmResult.EmptyPasswordError
    ) {
        confirmViewModel.conformCheck(StubEditable(""), StubEditable(""))
    }

    @Test
    fun conformCheckSuccess() = confirmViewModel.resultLiveData.assertValues(
            ConfirmResult.Loading,
            ConfirmResult.Success
    ) {
        confirmViewModel.conformCheck(StubEditable("pass"), StubEditable("pass"))
    }

    @Test
    fun errorResult() {
        assertEquals(ConfirmResult.Error("err"), confirmViewModel.errorResult(Throwable("err")))
    }
}