package com.softartdev.noteroom.shared.data

import net.sqlcipher.database.SQLiteException

class SafeSQLiteException(message: String) : SQLiteException(message)