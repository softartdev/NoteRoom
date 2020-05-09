package com.softartdev.noteroom.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.noteroom.data.CryptUseCase
import com.softartdev.noteroom.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val cryptUseCase = Mockito.mock(CryptUseCase::class.java)
    private val settingsViewModel = SettingsViewModel(cryptUseCase)

    @Test
    fun checkEncryptionTrue() = assertEncryption(true)

    @Test
    fun checkEncryptionFalse() = assertEncryption(false)

    private fun assertEncryption(encryption: Boolean) = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenReturn(encryption)
        settingsViewModel.checkEncryption()
        launch {
            assertEquals(settingsViewModel.flow.first(), SecurityResult.EncryptEnable(encryption))
        }.cancel()
    }

    @Test
    fun changeEncryptionSetPasswordDialog() = mainCoroutineRule.runBlockingTest {
        settingsViewModel.changeEncryption(true)
        launch {
            assertEquals(settingsViewModel.flow.first(), SecurityResult.SetPasswordDialog)
        }.cancel()
    }

    @Test
    fun changeEncryptionPasswordDialog() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenReturn(true)
        settingsViewModel.changeEncryption(false)
        launch {
            assertEquals(settingsViewModel.flow.first(), SecurityResult.PasswordDialog)
        }.cancel()
    }

    @Test
    fun changeEncryptionEncryptEnableFalse() = mainCoroutineRule.launch {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenReturn(false)
        settingsViewModel.changeEncryption(false)
        assertEquals(settingsViewModel.flow.first(), SecurityResult.EncryptEnable(false))
    }.cancel()

    @Test
    fun changePasswordChangePasswordDialog() = mainCoroutineRule.launch {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenReturn(true)
        settingsViewModel.changePassword()
        assertEquals(settingsViewModel.flow.first(), SecurityResult.ChangePasswordDialog)
    }.cancel()

    @Test
    fun changePasswordSetPasswordDialog() = mainCoroutineRule.launch {
        Mockito.`when`(cryptUseCase.dbIsEncrypted()).thenReturn(false)
        settingsViewModel.changePassword()
        assertEquals(settingsViewModel.flow.first(), SecurityResult.SetPasswordDialog)
    }.cancel()

    @Test
    fun errorResult() {
        assertEquals(SecurityResult.Error("err"), settingsViewModel.errorResult(Throwable("err")))
    }
}