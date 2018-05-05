package com.softartdev.noteroom.db;

import com.softartdev.noteroom.model.Note;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DbStore {
    List<Note> getNotes();

    Note createNote();

    void saveNote(long id, String title, String text);

    Note loadNote(long noteId);

    void deleteNote(long id);

    boolean checkPass(String pass);

    boolean isEncryption();

    void changePass(String odlPass, String newPass);

    boolean isChanged(long id, @NotNull String title, @NotNull String text);

    boolean isEmpty(long id);
}
