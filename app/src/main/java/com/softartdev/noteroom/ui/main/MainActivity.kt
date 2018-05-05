package com.softartdev.noteroom.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.softartdev.noteroom.R
import com.softartdev.noteroom.model.Note
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.ui.note.NoteActivity
import io.github.tonnyl.spark.Spark
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView, MainAdapter.ClickListener {
    @Inject lateinit var mPresenter: MainPresenter
    @Inject lateinit var mAdapter: MainAdapter

    private lateinit var mSpark: Spark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent().inject(this)
        mPresenter.attachView(this)

        setSupportActionBar(main_toolbar)

        add_note_fab.setOnClickListener { mPresenter.addNote() }

        mAdapter.clickListener = this
        notes_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        if (mAdapter.itemCount == 0) {
            mPresenter.updateNotes()
        }

        mSpark = Spark.Builder()
                .setView(main_coordinator) // View or view group
                .setAnimList(Spark.ANIM_BLUE_PURPLE)
                .build()
    }

    override fun onResume() {
        super.onResume()
        mSpark.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        mSpark.stopAnimation()
    }

    override fun onUpdateNotes(notes: List<Note>) {
        add_note_text_view.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        mAdapter.mNotes = notes
    }

    override fun onNoteClick(noteId: Long) {
        startActivity(NoteActivity.getStartIntent(this, noteId))
    }

    override fun onAddNote() {
        startActivity(NoteActivity.getStartIntent(this, 0L))
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
