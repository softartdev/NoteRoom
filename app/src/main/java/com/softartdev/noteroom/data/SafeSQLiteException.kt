package com.softartdev.noteroom.data

import net.sqlcipher.database.SQLiteException

class SafeSQLiteException(message: String) : SQLiteException(message)