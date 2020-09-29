package com.notes.app.repository;

import com.notes.app.domain.Note;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;
    private Note testNote1;
    private Note testNote2;
    private Note testNote2Double;


    @BeforeEach
    public void init() {
        Note note = new Note();
        note.setHeader("My first");
        note.setText("Test of first note");
        testNote1 = noteRepository.saveNote(note);

        note = new Note();
        note.setHeader("My second");
        note.setText("Test of second note");
        testNote2 = noteRepository.saveNote(note);

        note = new Note();
        note.setHeader("My second note double");
        note.setText("Test of second note double");
        testNote2Double = noteRepository.saveNote(note);


        Assert.isTrue(Lists.newArrayList(noteRepository.findAllNotes()).size() == 3,
                "All notes weren't saved");
    }
    @AfterEach
    public void cleaning() {
        noteRepository.deleteAllNotes();
        Assert.isTrue(Lists.newArrayList(noteRepository.findAllNotes()).size() == 0,
                "All notes weren't deleted");
    }

    @Test
    public void saveNoteTest() {
        Assert.notNull(testNote1.getId(), "note wasn't saved");
        Assert.notNull(testNote2.getId(), "note wasn't saved");
        Assert.notNull(testNote2Double.getId(), "note wasn't saved");
    }

    @Test
    public void findNoteByIdTest() {
        Note note = noteRepository.findNoteById(testNote1.getId()).orElse(null);
        Assert.notNull(note, "Note wasn't found by id");
    }

    @Test
    public void findAllNotesTest() {
        List<Note> notes = noteRepository.findAllNotes();
        Assert.isTrue(notes.size() == 3, "All notes weren't received");
    }

    @Test
    public void deleteNoteByIdTest() {
        noteRepository.deleteNoteById(testNote1.getId());
        List<Note> notes = noteRepository.findAllNotes();
        Assert.isTrue(notes.size() == 2, "Note wasn't deleted by id");
    }

    @Test
    public void deleteAllNotesTest() {
        noteRepository.deleteAllNotes();
        List<Note> notes = noteRepository.findAllNotes();
        Assert.isTrue(notes.size() == 0, "All notes weren't deleted");
    }

    @Test
    public void searchTest() {
        List<Note> notes = noteRepository.search(List.of("header"), "");
        Assert.isTrue(notes.size() == 3, "Blank search field didn't found all");
        Assertions.assertThrows(Exception.class, () -> noteRepository.search(new ArrayList<>(), "first"));
        notes = noteRepository.search(List.of("header", "text"), "second");
        Assert.isTrue(notes.size() == 2, "Search by 'second' went wrong");
    }
}
