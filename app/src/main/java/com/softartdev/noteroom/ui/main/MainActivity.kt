package com.softartdev.noteroom.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.softartdev.noteroom.R
import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.model.NoteListResult
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.note.NoteActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import com.softartdev.noteroom.util.autoCleared
import com.softartdev.noteroom.util.gone
import com.softartdev.noteroom.util.tintIcon
import com.softartdev.noteroom.util.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_error.view.*

class MainActivity : BaseActivity(), MainAdapter.ClickListener, Observer<NoteListResult> {

    private val mainViewModel by viewModels<MainViewModel> { viewModelFactory }
    private var mainAdapter by autoCleared<MainAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_swipe_refresh.apply {
            setProgressBackgroundColorSchemeResource(R.color.colorPrimary)
            setColorSchemeResources(R.color.on_primary)
            setOnRefreshListener { mainViewModel.updateNotes() }
        }
        mainAdapter = MainAdapter()
        mainAdapter.clickListener = this
        notes_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
        add_note_fab.setOnClickListener {
            startActivity(NoteActivity.getStartIntent(this, 0L))
        }
        main_error_view.button_reload.setOnClickListener { mainViewModel.updateNotes() }

        mainViewModel.notesLiveData.observe(this, this)
    }

    override fun onChanged(noteListResult: NoteListResult) = when (noteListResult) {
        NoteListResult.Loading -> showProgress(true)
        is NoteListResult.Success -> {
            showProgress(false)
            if (noteListResult.result.isNotEmpty()) {
                onUpdateNotes(noteListResult.result)
            } else {
                showEmpty()
            }
        }
        is NoteListResult.Error -> {
            showProgress(false)
            showError(noteListResult.error)
        }
        NoteListResult.NavMain -> navSignIn()
    }

    private fun onUpdateNotes(noteList: List<Note>) {
        mainAdapter.apply {
            notes = noteList
            notifyDataSetChanged()
        }
    }

    override fun onNoteClick(noteId: Long) {
        startActivity(NoteActivity.getStartIntent(this, noteId))
    }

    private fun showProgress(show: Boolean) {
        if (main_swipe_refresh.isRefreshing) {
            main_swipe_refresh.isRefreshing = show
        } else {
            main_progress_view.apply { if (show) visible() else gone() }
        }
    }

    private fun showEmpty() = main_empty_view.visible()

    private fun showError(message: String?) {
        main_error_view.apply {
            visible()
            text_error_message.text = message
        }
    }

    private fun navSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_settings).tintIcon(this)
        return true
    }

}
