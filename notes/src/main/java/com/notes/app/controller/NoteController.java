package com.notes.app.controller;

import com.notes.app.model.NoteModel;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import java.util.List;

public interface NoteController {

    String getNoteById(String id, Model model);

    String getAllNotes(List<String> fields, String textToSearch, Model model);

    String createNoteForm(Model model);

    String createNote(NoteModel noteModel, Errors errors, Model model);


    String deleteNoteById(String id, Model model);

    String deleteAllNotes(Model model);

}
