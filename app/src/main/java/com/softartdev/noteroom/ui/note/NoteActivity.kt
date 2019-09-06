package com.softartdev.noteroom.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.NavUtils
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.util.hideKeyboard
import com.softartdev.noteroom.util.showKeyboard
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.content_note.*
import javax.inject.Inject

class NoteActivity : BaseActivity(), NoteView {
    @Inject lateinit var notePresenter: NotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        activityComponent().inject(this)
        notePresenter.attachView(this)

        setSupportActionBar(note_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        save_note_fab.setOnClickListener { saveNote() }

        val noteId = intent.getLongExtra(NOTE_ID, 0L)
        savedInstanceState?.let {
            note_title_edit_text.setText(it.getString(KEY_TITLE))
            note_edit_text.setText(it.getString(KEY_TEXT))
        } ?: if (noteId == 0L) {
            notePresenter.createNote()
        } else {
            notePresenter.loadNote(noteId)
        }
    }

    override fun onLoadNote(title: String, text: String) {
        if (title.isNotEmpty()) {
            note_title_edit_text.setText(title)
            supportActionBar?.title = title
        } else {
            note_title_edit_text.showKeyboard()
        }
        note_edit_text.setText(text)
    }

    override fun onSaveNote(title: String) {
        val noteSaved = getString(R.string.note_saved) + ": " + title
        Snackbar.make(save_note_fab, noteSaved, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    override fun onEmptyNote() = Snackbar
            .make(save_note_fab, R.string.note_empty, Snackbar.LENGTH_LONG)
            .show()

    override fun onDeleteNote() = Snackbar
            .make(save_note_fab, R.string.note_deleted, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                checkSaveChange()
                true
            }
            R.id.action_delete_note -> {
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() = checkSaveChange()

    private fun checkSaveChange() = notePresenter.checkSaveChange(
            title = note_title_edit_text.text.toString(),
            text = note_edit_text.text.toString())

    private fun showDeleteDialog() = with(AlertDialog.Builder(this)) {
        setTitle(R.string.action_delete_note)
        setMessage(R.string.note_delete_dialog_message)
        setPositiveButton(android.R.string.yes) { _, _ -> notePresenter.deleteNote() }
        setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        show()
    }

    override fun onCheckSaveChange() {
        hideKeyboard()
        with(AlertDialog.Builder(this)) {
            setTitle(R.string.note_changes_not_saved_dialog_title)
            setMessage(R.string.note_save_change_dialog_message)
            setPositiveButton(R.string.yes) { _, _ ->
                saveNote()
                onNavBack()
            }
            setNegativeButton(R.string.no) { _, _ -> onNavBack() }
            setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            show()
        }
    }

    private fun saveNote() = notePresenter.saveNote(
            title = note_title_edit_text.text.toString(),
            text = note_edit_text.text.toString())

    override fun onNavBack() = NavUtils.navigateUpFromSameTask(this)

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, note_title_edit_text.text.toString())
        outState.putString(KEY_TEXT, note_edit_text.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        notePresenter.detachView()
        super.onDestroy()
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
