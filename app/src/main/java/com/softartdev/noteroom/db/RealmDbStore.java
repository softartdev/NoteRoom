package com.softartdev.noteroom.db;

import android.content.Context;
import android.text.TextUtils;

import com.softartdev.noteroom.model.Note;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmResults;
import timber.log.Timber;

public class RealmDbStore extends RealmDbRepository {

    public RealmDbStore(Context context) {
        super(context);
    }

    @Override
    public List<Note> getNotes() {
        RealmResults<Note> noteResults = getRealm().where(Note.class).findAll();
        return getRealm().copyFromRealm(noteResults);
    }

    @Override
    public Note createNote() {
        final long noteId = UUID.randomUUID().getLeastSignificantBits();
        getRealm().executeTransaction(realm -> {
            Note note = realm.createObject(Note.class, noteId);
            note.setTitle("");
            note.setText("");
        });
        return loadNote(noteId);
    }

    @Override
    public void saveNote(final long noteId, final String title, final String text) {
        getRealm().executeTransaction(realm -> {
            Note note = realm.where(Note.class).equalTo("id", noteId).findFirst();
            if (note == null) {
                note = realm.createObject(Note.class, noteId);
            }
            note.setTitle(title);
            note.setText(text);
            note.setDateModified(new Date());
            realm.copyToRealmOrUpdate(note);
        });
    }

    @Override
    public Note loadNote(long noteId) {
        Note managedNote = getRealm().where(Note.class).equalTo("id", noteId).findFirst();
        Note unmanagedNote = getRealm().copyFromRealm(managedNote);
        Timber.d("Unmanaged note: %s", unmanagedNote.getTitle());
        return unmanagedNote;
    }

    @Override
    public void deleteNote(final long noteId) {
        getRealm().executeTransaction(realm -> {
            Note note = realm.where(Note.class).equalTo("id", noteId).findFirst();
            note.deleteFromRealm();
        });
    }

    @Override
    public boolean isChanged(long id, @NotNull String title, @NotNull String text) {
        Note note = loadNote(id);
        String savedTitle = note.getTitle();
        String savedText = note.getText();
        return !title.equals(savedTitle) || !text.equals(savedText);
    }

    @Override
    public boolean isEmpty(long id) {
        Note note = loadNote(id);
        String savedTitle = note.getTitle();
        String savedText = note.getText();
        return TextUtils.isEmpty(savedTitle) && TextUtils.isEmpty(savedText);
    }
}
