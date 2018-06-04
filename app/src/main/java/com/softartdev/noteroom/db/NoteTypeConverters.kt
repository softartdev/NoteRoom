package com.softartdev.noteroom.db

import android.arch.persistence.room.TypeConverter
import java.util.*

object NoteTypeConverters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
