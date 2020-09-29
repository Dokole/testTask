package com.notes.app.util;

import com.notes.app.domain.Note;
import com.notes.app.model.NoteModel;
import org.springframework.stereotype.Component;

@Component
public class CastToModel {

    public NoteModel noteToModel(Note note) {
        return new NoteModel(note.getId(), note.getCreationDate(), note.getHeader(), note.getText());
    }

    public Note noteModelToDomain(NoteModel noteModel) {
        return new Note(noteModel.getId(), noteModel.getTime(),
                noteModel.getHeader(), noteModel.getText());
    }
}
