package com.notes.app.service;

import com.notes.app.domain.Note;
import com.notes.app.exceptions.BadRequestException;
import com.notes.app.exceptions.NotFoundException;
import com.notes.app.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImp noteService;

    private static Note unsavedNote;
    private static Note savedNote;

    @BeforeAll
    private static void init() {
        Note saved2 = new Note();
        saved2.setHeader("My first");
        saved2.setText("Test of first note");
        saved2.setId("5f72d6d3538f5d132fdc6e07");
        savedNote = saved2;

        Note unsaved2 = new Note();
        unsaved2.setHeader("My first");
        unsaved2.setText("Test of first note");
        unsavedNote = unsaved2;
    }

    @Test
    public void getNoteByIdTest() {
        when(noteRepository.findNoteById("5f72d6d3538f5d132fdc6e07")).thenReturn(Optional.of(savedNote));
        when(noteRepository.findNoteById("anyId")).thenReturn(Optional.empty());

        Assert.isTrue(noteService.getNoteById("5f72d6d3538f5d132fdc6e07").equals(savedNote), "Error while getting note by id");
        Assertions.assertThrows(NotFoundException.class, () -> noteService.getNoteById("anyId"));
    }

    @Test
    public void getAllNotesTest() {
        when(noteRepository.findAllNotes()).thenReturn(List.of(savedNote));
        Assert.isTrue(noteService.getAllNotes().size() == 1, "Error while getting all notes");
    }

    @Test
    public void getAllNotes_ErrorTest() {
        when(noteRepository.findAllNotes()).thenReturn(List.of());
        Assert.isTrue(noteService.getAllNotes().size() == 0,
                "Error while testing getting all notes as an empty list");
    }

    @Test
    public void createNoteTest() {
        when(noteRepository.saveNote(unsavedNote)).thenReturn(savedNote);
        Note result = noteService.createNote(unsavedNote);
        Assert.isTrue(result.equals(savedNote), "Note wasn't saved");
    }

    @Test
    public void createNote_WithExistedIdTest() {
        when(noteRepository.saveNote(savedNote)).thenReturn(savedNote);
        Assertions.assertThrows(BadRequestException.class, () -> noteService.createNote(savedNote));
        verifyNoInteractions(noteRepository);
    }

    @Test
    public void searchTest() {
        when(noteRepository.search(List.of("header"), "first")).thenReturn(List.of(savedNote));
        Assert.isTrue(noteService.search(List.of("header"), "first")
                .size() == 1, "Note wasn't found");
    }

    @Test
    public void searchWithNullResultTest() {
        when(noteRepository.search(List.of("header"), "first")).thenReturn(null);
        Assert.isTrue(noteService.search(List.of("header"), "first")
                .equals(new ArrayList<>()), "Null handling went wrong");
    }
}
