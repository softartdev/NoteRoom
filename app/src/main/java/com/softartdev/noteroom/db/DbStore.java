package com.softartdev.noteroom.db;

import android.text.Editable;

import com.softartdev.noteroom.model.Note;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DbStore {
    List<Note> getNotes();

    Long createNote(String title, String text);

    void saveNote(long id, String title, String text);

    Note loadNote(long noteId);

    void deleteNote(long id);

    boolean checkPass(Editable pass);

    boolean isEncryption();

    void changePass(@Nullable Editable oldPass, @Nullable Editable newPass);

    boolean isChanged(long id, @NotNull String title, @NotNull String text);

    boolean isEmpty(long id);
}
