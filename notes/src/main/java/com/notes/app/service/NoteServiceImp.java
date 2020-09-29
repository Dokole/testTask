package com.notes.app.service;

import com.notes.app.domain.Note;
import com.notes.app.exceptions.BadRequestException;
import com.notes.app.exceptions.NotFoundException;
import com.notes.app.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "notes")
public class NoteServiceImp implements NoteService{

    private final Logger logger = LoggerFactory.getLogger(NoteServiceImp.class);

    protected final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImp(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    @Cacheable(key = "#root.method.name.concat(#id)")
    @Transactional(readOnly = true)
    public Note getNoteById(String id) {
        Note note = noteRepository.findNoteById(id).orElse(null);
        if (note == null) {
            throw new NotFoundException("No note found by id=" + id);
        }
        return note;
    }

    @Override
    @Cacheable(key = "#root.method.name")
    @Transactional(readOnly = true)
    public List<Note> getAllNotes() {
        List<Note> notes = noteRepository.findAllNotes();
        if (notes == null || notes.isEmpty()) {
            return new ArrayList<>();
        }
        return notes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> search(List<String> fields, String textToSearch) {
        List<Note> notes = noteRepository.search(fields, textToSearch);
        if (notes == null) {
            return new ArrayList<>();
        }
        return notes;
    }

    @Override
    @CacheEvict(cacheNames = "notes", allEntries = true)
    public Note createNote(Note note) {
        if (note.getId() != null) {
            throw new BadRequestException("Id=" + note.getId() + " should be null to create a note. Can't be saved.");
        }
        return noteRepository.saveNote(note);
    }

    @Override
    @CacheEvict(cacheNames = "notes", allEntries = true)
    public void deleteNoteById(String id) {
        noteRepository.deleteNoteById(id);
    }

    @Override
    @CacheEvict(cacheNames = "notes", allEntries = true)
    public void deleteAllNotes() {
        noteRepository.deleteAllNotes();
    }
}
