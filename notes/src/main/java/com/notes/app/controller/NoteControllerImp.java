package com.notes.app.controller;

import com.notes.app.domain.Note;
import com.notes.app.model.NoteModel;
import com.notes.app.service.NoteService;
import com.notes.app.util.CastToModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notes")
public class NoteControllerImp implements NoteController {

    private final NoteService noteService;
    private final CastToModel castToModel;

    @Autowired
    public NoteControllerImp(NoteService noteService, CastToModel castToModel) {
        this.noteService = noteService;
        this.castToModel = castToModel;
    }

    @Override
    @GetMapping(value = "/{id}")
    public String getNoteById(@PathVariable("id") String id, Model model) {
        Note note = noteService.getNoteById(id);
        model.addAttribute("note", castToModel.noteToModel(note));
        return "/notes/note";
    }

    @Override
    @GetMapping(value = "/list")
    public String getAllNotes(@RequestParam(value = "fields", required = false) List<String> fields,
                              @RequestParam(value = "search", required = false) String textToSearch, Model model) {
        List<Note> notes;
        if (textToSearch != null && !textToSearch.isBlank()) {
            //Default search scope
            if (fields == null || fields.isEmpty()) {
                fields = List.of("header", "text");
            }
            notes = noteService.search(fields, textToSearch);
        } else {
            notes = noteService.getAllNotes();
        }
        List<NoteModel> noteModels = notes.stream().map(
                castToModel::noteToModel).collect(Collectors.toList());
        model.addAttribute("notesList", noteModels);
        return "/notes/notesList";
    }

    @Override
    @GetMapping(value = "/create")
    public String createNoteForm(Model model) {
        if (!model.containsAttribute("newNote")) {
            model.addAttribute("newNote", new NoteModel());
        }
        return "/notes/noteCreate";
    }

    @Override
    @PostMapping("/create")
    public String createNote(@Valid @ModelAttribute("newNote") NoteModel noteModel,
                             Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "/notes/noteCreate";
        }
        Note note = castToModel.noteModelToDomain(noteModel);
        note = noteService.createNote(note);
        noteModel = castToModel.noteToModel(note);

        model.addAttribute("note", noteModel);
        return "redirect:/notes/" + noteModel.getId();
    }

    @Override
    @GetMapping("/{id}/delete")
    public String deleteNoteById(@PathVariable String id, Model model) {
        noteService.deleteNoteById(id);
        return "redirect:/notes/list";
    }

    @Override
    @GetMapping("/deleteAll")
    public String deleteAllNotes(Model model) {
        noteService.deleteAllNotes();
        return "redirect:/notes/list";
    }
}
