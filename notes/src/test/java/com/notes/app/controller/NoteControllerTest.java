package com.notes.app.controller;

import com.notes.app.domain.Note;
import com.notes.app.exceptions.NotFoundException;
import com.notes.app.model.NoteModel;
import com.notes.app.service.NoteService;
import com.notes.app.util.CastToModel;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = NoteControllerImp.class)
@ExtendWith(SpringExtension.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;
    @MockBean
    private CastToModel castToModel;

    @InjectMocks
    private NoteControllerImp noteControllerImp;

    private static Note unsavedNote1;
    private static Note savedNote1;
    private static Note unsavedNote2;
    private static Note savedNote2;
    private static NoteModel unsavedNoteModel1;
    private static NoteModel unsavedNoteModel2;
    private static NoteModel savedNoteModel1;
    private static NoteModel savedNoteModel2;

    @BeforeAll
    public static void init() {
        Note saved1 = new Note();
        saved1.setText("Test of first note");
        saved1.setId("5f72d6d3538f5d132fdc6e05");
        savedNote1 = saved1;

        Note unsaved1 = new Note();
        unsaved1.setText("Test of first note");
        unsavedNote1 = unsaved1;

        Note saved2 = new Note();
        saved2.setHeader("My second");
        saved2.setText("Test of second note");
        saved2.setId("5f72d6d3538f5d132fdc6e07");
        savedNote2 = saved2;

        Note unsaved2 = new Note();
        unsaved2.setHeader("My second");
        unsaved2.setText("Test of second note");
        unsavedNote2 = unsaved2;

        NoteModel savedModel1 = new NoteModel();
        savedModel1.setText("Test of first note");
        savedModel1.setId("5f72d6d3538f5d132fdc6e05");
        savedNoteModel1 = savedModel1;

        NoteModel unsavedModel1 = new NoteModel();
        unsavedModel1.setText("Test of first note");
        unsavedNoteModel1 = unsavedModel1;

        NoteModel savedModel2 = new NoteModel();
        savedModel2.setHeader("My second");
        savedModel2.setText("Test of second note");
        savedModel2.setId("5f72d6d3538f5d132fdc6e07");
        savedNoteModel2 = savedModel2;

        NoteModel unsavedModel2 = new NoteModel();
        unsavedModel2.setHeader("My second");
        unsavedModel2.setText("Test of second note");
        unsavedNoteModel2 = unsavedModel2;
    }

    @Test
    public void getNoteByIdTest() throws Exception {

        when(noteService.getNoteById("5f72d6d3538f5d132fdc6e05")).thenReturn(savedNote1);
        when(noteService.getNoteById("5f72d6d3538f5d132fdc6e01")).thenThrow(NotFoundException.class);
        when(castToModel.noteToModel(savedNote1)).thenReturn(savedNoteModel1);

        mockMvc.perform(get("/notes/5f72d6d3538f5d132fdc6e01"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("customErrorMessage"))
                .andExpect(model().attributeExists("errorMessage"));
        verify(noteService, times(1)).getNoteById("5f72d6d3538f5d132fdc6e01");
        verifyNoInteractions(castToModel);

        mockMvc.perform(get("/notes/5f72d6d3538f5d132fdc6e05"))
                .andExpect(status().isOk())
                .andExpect(view().name("/notes/note"))
                .andExpect(model().attribute("note", savedNoteModel1));

        verify(noteService, times(1)).getNoteById("5f72d6d3538f5d132fdc6e05");
        verify(castToModel, times(1)).noteToModel(savedNote1);

    }

    @Test
    public void getAllNotesWithNoParametersTest() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Lists.newArrayList(savedNote1, savedNote2));
        when(castToModel.noteToModel(savedNote1)).thenReturn(savedNoteModel1);
        when(castToModel.noteToModel(savedNote2)).thenReturn(savedNoteModel2);
        when(noteService.search(null, "")).thenReturn(List.of(savedNote1, savedNote2));

        mockMvc.perform(get("/notes/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/notes/notesList"))
                .andExpect(model().attribute("notesList", hasSize(2)))
                .andExpect(model().attribute("notesList", hasItem(
                        allOf(
                                hasProperty("id", is("5f72d6d3538f5d132fdc6e05")),
                                hasProperty("header", is(nullValue())),
                                hasProperty("text", is("Test of first note"))
                        )
                )));
        verify(noteService, times(1)).getAllNotes();
        verify(castToModel, times(1)).noteToModel(savedNote1);
        verify(castToModel, times(1)).noteToModel(savedNote2);

    }

    @Test
    public void getAllNotesWithParametersWithoutFieldsTest() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Lists.newArrayList(savedNote1, savedNote2));
        when(castToModel.noteToModel(savedNote1)).thenReturn(savedNoteModel1);
        when(castToModel.noteToModel(savedNote2)).thenReturn(savedNoteModel2);
        when(noteService.search(List.of("header", "text"), "first")).thenReturn(List.of(savedNote1));

        mockMvc.perform(get("/notes/list?search=first"))
                .andExpect(status().isOk())
                .andExpect(view().name("/notes/notesList"))
                .andExpect(model().attribute("notesList", hasSize(1)))
                .andExpect(model().attribute("notesList", hasItem(
                        allOf(
                                hasProperty("id", is("5f72d6d3538f5d132fdc6e05")),
                                hasProperty("header", is(nullValue())),
                                hasProperty("text", is("Test of first note"))
                        )
                )));
        verify(castToModel, times(1)).noteToModel(savedNote1);
        verify(noteService, times(1)).search(List.of("header", "text"), "first");

    }

    @Test
    public void getAllNotesWithParametersWithFieldsTest() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Lists.newArrayList(savedNote1, savedNote2));
        when(castToModel.noteToModel(savedNote1)).thenReturn(savedNoteModel1);
        when(castToModel.noteToModel(savedNote2)).thenReturn(savedNoteModel2);
        when(noteService.search(List.of("header"), "My")).thenReturn(List.of(savedNote2));

        mockMvc.perform(get("/notes/list?search=My&fields=header"))
                .andExpect(status().isOk())
                .andExpect(view().name("/notes/notesList"))
                .andExpect(model().attribute("notesList", hasSize(1)))
                .andExpect(model().attribute("notesList", hasItem(
                        allOf(
                                hasProperty("id", is("5f72d6d3538f5d132fdc6e07")),
                                hasProperty("header", is("My second")),
                                hasProperty("text", is("Test of second note"))
                        )
                )));
        verify(castToModel, times(1)).noteToModel(savedNote2);
        verify(noteService, times(1)).search(List.of("header"), "My");

    }

    @Test
    public void createNoteFormTest() throws Exception {

        mockMvc.perform(get("/notes/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/notes/noteCreate"))
                .andExpect(model().attributeExists("newNote"));
    }

    @Test
    public void createNoteTest() throws Exception {
        when(castToModel.noteModelToDomain(any())).thenReturn(unsavedNote1);
        when(noteService.createNote(unsavedNote1)).thenReturn(savedNote1);
        when(castToModel.noteToModel(savedNote1)).thenReturn(savedNoteModel1);

        mockMvc.perform(post("/notes/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("header", "Test unsaved note model")
                .param("text", "Some text")
                .sessionAttr("newNote", new NoteModel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/notes/5f72d6d3538f5d132fdc6e05"))
                .andExpect(model().attribute("note",
                        hasProperty("text", is("Test of first note"))));

        mockMvc.perform(post("/notes/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("header", "Te")
                .param("text", "")
                .sessionAttr("newNote", new NoteModel()))
                .andExpect(model().attributeHasFieldErrors("newNote", "text"));
    }


    @Test
    public void deleteNoteByIdTest() throws Exception {
        mockMvc.perform(get("/notes/5f72d6d3538f5d132fdc6e05/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/notes/list"));
    }

}
