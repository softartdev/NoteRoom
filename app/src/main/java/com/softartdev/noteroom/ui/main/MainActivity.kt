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
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView, MainAdapter.ClickListener {
    @Inject lateinit var mainPresenter: MainPresenter
    @Inject lateinit var mainAdapter: MainAdapter

    private lateinit var mainSpark: Spark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent().inject(this)
        mainPresenter.attachView(this)

        add_note_fab.setOnClickListener { startActivity(NoteActivity.getStartIntent(this, 0L)) }

        mainAdapter.clickListener = this
        notes_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        mainSpark = Spark.Builder()
                .setView(main_frame) // View or view group
                .setAnimList(Spark.ANIM_BLUE_PURPLE)
                .build()

        if (mainAdapter.itemCount == 0) {
            mainPresenter.updateNotes()
        }
    }

    override fun onResume() {
        super.onResume()
        mainSpark.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        mainSpark.stopAnimation()
    }

    override fun onUpdateNotes(noteList: List<Note>) {
        add_note_text_view.visibility = if (noteList.isEmpty()) View.VISIBLE else View.GONE
        mainAdapter.apply {
            notes = noteList
            notifyDataSetChanged()
        }
    }

    override fun onNoteClick(noteId: Long) {
        startActivity(NoteActivity.getStartIntent(this, noteId))
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.detachView()
    }
}
