package com.softartdev.noteroom.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Note : RealmObject() {
    @PrimaryKey var id: Long = 0
    var title: String = ""
    var text: String = ""
    var dateCreated = Date()
    var dateModified = Date()
}
