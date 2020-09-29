package com.notes.app.model;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;

public class NoteModel {

    protected String id;

    protected Date time;

    protected String header;
    @NotBlank(message = "Text field cannot be empty")
    protected String text;

    public NoteModel() {
    }

    public NoteModel(String id, Date time, String header, String text) {
        this.id = id;
        this.time = time;
        this.header = header;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteModel noteModel = (NoteModel) o;
        return Objects.equals(id, noteModel.id) &&
                Objects.equals(time, noteModel.time) &&
                Objects.equals(header, noteModel.header) &&
                text.equals(noteModel.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, header, text);
    }

    @Override
    public String toString() {
        return "Note{" +
                "header='" + header + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
