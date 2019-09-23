package com.softartdev.noteroom.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.softartdev.noteroom.R
import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.common.OnReloadClickListener
import com.softartdev.noteroom.ui.note.NoteActivity
import com.softartdev.noteroom.ui.signin.SignInActivity
import com.softartdev.noteroom.util.gone
import com.softartdev.noteroom.util.tintIcon
import com.softartdev.noteroom.util.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_error.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView, MainAdapter.ClickListener, OnReloadClickListener {
    @Inject lateinit var mainPresenter: MainPresenter
    @Inject lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent().inject(this)
        mainPresenter.attachView(this)

        main_swipe_refresh.apply {
            setProgressBackgroundColorSchemeResource(R.color.colorPrimary)
            setColorSchemeResources(R.color.on_primary)
            setOnRefreshListener { mainPresenter.updateNotes() }
        }
        mainAdapter.clickListener = this
        notes_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
        add_note_fab.setOnClickListener {
            startActivity(NoteActivity.getStartIntent(this, 0L))
        }
        main_error_view.reloadClickListener = this
        if (mainAdapter.itemCount == 0) {
            mainPresenter.updateNotes()
        }
    }

    override fun onUpdateNotes(noteList: List<Note>) {
        mainAdapter.apply {
            notes = noteList
            notifyDataSetChanged()
        }
    }

    override fun onNoteClick(noteId: Long) {
        startActivity(NoteActivity.getStartIntent(this, noteId))
    }

    override fun showProgress(show: Boolean) {
        if (main_swipe_refresh.isRefreshing) {
            main_swipe_refresh.isRefreshing = show
        } else {
            main_progress_view.apply { if (show) visible() else gone() }
        }
    }

    override fun showEmpty() = main_empty_view.visible()

    override fun showError(error: Throwable) {
        main_error_view.apply {
            visible()
            text_error_message.text = error.message
        }
        Timber.e(error, "There was an error main")
    }

    override fun onReloadClick() {
        main_empty_view.gone()
        main_error_view.gone()
        mainPresenter.updateNotes()
    }

    override fun navSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_settings).tintIcon(this)
        return true
    }

    override fun onDestroy() {
        mainPresenter.detachView()
        mainAdapter.clickListener = null
        notes_recycler_view.adapter = null
        super.onDestroy()
    }
}
