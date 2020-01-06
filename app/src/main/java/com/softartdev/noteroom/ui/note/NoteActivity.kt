package com.softartdev.noteroom.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NavUtils
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.softartdev.noteroom.R
import com.softartdev.noteroom.model.NoteResult
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.title.EditTitleDialog
import com.softartdev.noteroom.util.*
import kotlinx.android.synthetic.main.activity_note.*
import timber.log.Timber

class NoteActivity : BaseActivity(), Observer<NoteResult> {

    private val noteViewModel by viewModels<NoteViewModel> { viewModelFactory }

    private val noteId: Long
        get() = intent.getLongExtra(NOTE_ID, 0L)

    private val noteTitle: String?
        get() = when(noteId) {
            0L -> null
            else -> supportActionBar?.title?.toString().orEmpty()
        }

    private val noteText: String
        get() = note_edit_text.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteViewModel.noteLiveData.observe(this, this)

        savedInstanceState?.let { bundle ->
            bundle.getString(KEY_TITLE)?.let { supportActionBar?.title = it }
            bundle.getString(KEY_TEXT)?.let { note_edit_text.setText(it) }
        } ?: when (noteId) {
            0L -> noteViewModel.createNote()
            else -> noteViewModel.loadNote(noteId)
        }
    }

    override fun onChanged(noteResult: NoteResult) {
        note_progress_bar.invisible()
        when (noteResult) {
            NoteResult.Loading -> note_progress_bar.visible()
            is NoteResult.Created -> note_edit_text.showKeyboard()
            is NoteResult.Loaded -> {
                supportActionBar?.title = noteResult.result.title
                note_edit_text.setText(noteResult.result.text)
            }
            is NoteResult.Saved -> {
                val noteSaved = getString(R.string.note_saved) + ": " + noteResult.title
                Snackbar.make(note_edit_text, noteSaved, Snackbar.LENGTH_LONG).show()
            }
            NoteResult.Empty -> {
                Snackbar.make(note_edit_text, R.string.note_empty, Snackbar.LENGTH_LONG).show()
            }
            NoteResult.Deleted -> {
                Snackbar.make(note_edit_text, R.string.note_deleted, Snackbar.LENGTH_LONG).show()
                onNavBack()
            }
            NoteResult.CheckSaveChange -> onCheckSaveChange()
            NoteResult.NavBack -> onNavBack()
            is NoteResult.Error -> showError(noteResult.message)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
        val menuIconColor = getThemeColor(this, android.R.attr.textColorPrimary)
        menu.findItem(R.id.action_save_note).tintIcon(this, menuIconColor)
        menu.findItem(R.id.action_edit_title).tintIcon(this, menuIconColor)
        menu.findItem(R.id.action_delete_note).tintIcon(this, menuIconColor)
        menu.findItem(R.id.action_settings).tintIcon(this, menuIconColor)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            checkSaveChange()
            true
        }
        R.id.action_save_note -> {
            noteViewModel.saveNote(noteTitle, noteText)
            true
        }
        R.id.action_edit_title -> try {
            val editTitleDialog = EditTitleDialog.create(noteViewModel.noteId)
            editTitleDialog.show(supportFragmentManager, "EDIT_TITLE_DIALOG_TAG")
            true
        } catch (e: IllegalStateException) {
            Timber.e(e)
            showError(e.message)
            false
        }
        R.id.action_delete_note -> {
            showDeleteDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = checkSaveChange()

    private fun checkSaveChange() = noteViewModel.checkSaveChange(noteTitle, noteText)

    private fun showDeleteDialog() = with(AlertDialog.Builder(this)) {
        setTitle(R.string.action_delete_note)
        setMessage(R.string.note_delete_dialog_message)
        setPositiveButton(android.R.string.yes) { _, _ -> noteViewModel.deleteNote() }
        setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        show()
    }

    private fun onCheckSaveChange() {
        hideKeyboard()
        with(AlertDialog.Builder(this)) {
            setTitle(R.string.note_changes_not_saved_dialog_title)
            setMessage(R.string.note_save_change_dialog_message)
            setPositiveButton(R.string.yes) { _, _ ->
                noteViewModel.saveNoteAndNavBack(noteTitle, noteText)
            }
            setNegativeButton(R.string.no) { _, _ -> noteViewModel.doNotSaveAndNavBack() }
            setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            show()
        }
    }

    private fun onNavBack() = NavUtils.navigateUpFromSameTask(this)

    private fun showError(message: String?) = with(AlertDialog.Builder(this)) {
        setTitle(android.R.string.dialog_alert_title)
        setMessage(message)
        setNeutralButton(android.R.string.cancel, null)
        show(); Unit
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, supportActionBar?.title?.toString())
        outState.putString(KEY_TEXT, noteText)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_TITLE = "key_title"
        private const val KEY_TEXT = "key_text"

        const val NOTE_ID = "note_id"

        fun getStartIntent(context: Context, noteId: Long): Intent =
                Intent(context, NoteActivity::class.java)
                        .putExtra(NOTE_ID, noteId)
    }
}
