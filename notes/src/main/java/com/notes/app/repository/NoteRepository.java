package com.notes.app.repository;

import com.notes.app.domain.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {

    Optional<Note> findNoteById(String id);

    List<Note> findAllNotes();

    List<Note> search(List<String> fields, String textToSearch);

    Note saveNote(Note note);

    void deleteNoteById(String id);

    void deleteAllNotes();
}
