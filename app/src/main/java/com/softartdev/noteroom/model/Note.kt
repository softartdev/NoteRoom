package com.softartdev.noteroom.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.softartdev.noteroom.db.NoteTypeConverters
import java.util.*

@Entity
@TypeConverters(NoteTypeConverters::class)
data class Note(
        @PrimaryKey(autoGenerate = true) val id: Long,
        var title: String,
        var text: String,
        val dateCreated: Date,
        var dateModified: Date
)
