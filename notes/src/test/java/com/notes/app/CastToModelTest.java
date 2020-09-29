package com.notes.app;

import com.notes.app.domain.Note;
import com.notes.app.model.NoteModel;
import com.notes.app.util.CastToModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CastToModel.class)
public class CastToModelTest {

    @Autowired
    private CastToModel castToModel;

    private static NoteModel modelNote;
    private static Note domainNote;

    @BeforeAll
    private static void init() {
        Note saved2 = new Note();
        saved2.setHeader("My first");
        saved2.setText("Test of first note");
        saved2.setId("5f72d6d3538f5d132fdc6e07");
        domainNote = saved2;

        NoteModel savedM2 = new NoteModel();
        savedM2.setHeader("My first");
        savedM2.setText("Test of first note");
        savedM2.setId("5f72d6d3538f5d132fdc6e07");
        modelNote = savedM2;
    }

    @Test
    public void noteToModelTest() {
        NoteModel resultModel = castToModel.noteToModel(domainNote);
        Assert.isTrue(resultModel.equals(modelNote), "Wrong note casting from domain to model");
    }

    @Test
    public void noteDomainToModelTest() {
        Note resultNote = castToModel.noteModelToDomain(modelNote);
        Assert.isTrue(resultNote.equals(domainNote), "Wrong note casting from model to domain");
    }
}
