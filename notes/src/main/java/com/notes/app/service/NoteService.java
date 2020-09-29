package com.notes.app.service;

import com.notes.app.domain.Note;

import java.util.List;

public interface NoteService {

    Note getNoteById(String id);

    List<Note> getAllNotes();

    List<Note> search(List<String> fields, String textToSearch);

    Note createNote(Note note);

    void deleteNoteById(String id);

    void deleteAllNotes();
}
