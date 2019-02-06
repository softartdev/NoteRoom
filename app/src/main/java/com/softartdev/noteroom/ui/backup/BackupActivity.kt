package com.softartdev.noteroom.ui.backup

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.softartdev.noteroom.R
import com.softartdev.noteroom.ui.base.BaseActivity
import com.softartdev.noteroom.util.gone
import com.softartdev.noteroom.util.visible
import io.github.tonnyl.spark.Spark
import kotlinx.android.synthetic.main.activity_backup.*
import kotlinx.android.synthetic.main.view_error.*
import timber.log.Timber
import javax.inject.Inject

class BackupActivity : BaseActivity(), BackupView {
    @Inject lateinit var backupPresenter: BackupPresenter

    private lateinit var mainSpark: Spark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activityComponent().inject(this)
        backupPresenter.attachView(this)

        mainSpark = Spark.Builder()
                .setView(backup_frame)
                .setAnimList(Spark.ANIM_RED_PURPLE)
                .build()

        backup_swipe_refresh.setOnRefreshListener { backupPresenter.backup() }
        backup_button.setOnClickListener { backupPresenter.backup() }
        backup_error_view.setOnReloadClickListener { backupPresenter.backup() }
    }

    override fun onResume() {
        super.onResume()
        mainSpark.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        mainSpark.stopAnimation()
    }

    override fun showProgress(show: Boolean) {
        if (backup_swipe_refresh.isRefreshing) {
            backup_swipe_refresh.isRefreshing = show
        } else {
            backup_progress_view.apply { if (show) visible() else gone() }
        }
    }

    override fun showBackup() {
        Snackbar.make(backup_frame, R.string.backup, Snackbar.LENGTH_LONG).show()
    }

    override fun showError(error: Throwable) {
        backup_error_view.apply {
            visible()
            text_error_message.text = error.message
        }
        Timber.e(error, "There was an error main")
    }

    override fun onDestroy() {
        backupPresenter.detachView()
        super.onDestroy()
    }
}
