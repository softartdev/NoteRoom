package com.softartdev.noteroom.db

import com.softartdev.noteroom.model.Note
import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import java.util.*

internal class NoteMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        // DynamicRealm exposes an editable schema
        val schema = realm.schema

        // Migrate to version 1: Add a dates created & modified.
        if (oldVersion == 0L) {
            schema.get(Note::class.java.simpleName)?.apply {
                setRequired(Note::title.name, true)
                setRequired(Note::text.name, true)
                addField(Note::dateCreated.name, Date::class.java, FieldAttribute.REQUIRED)
                addField(Note::dateModified.name, Date::class.java, FieldAttribute.REQUIRED)
            }
        }

    }
}
