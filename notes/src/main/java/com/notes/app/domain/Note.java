package com.notes.app.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document(collection = "notes")
public class Note {

    @Id
    protected String id;
    @CreatedDate
    @Indexed(direction = IndexDirection.DESCENDING)
    protected Date creationDate;
    @TextIndexed(weight = 3)
    protected String header;
    @TextIndexed
    protected String text;

    public Note() {
    }

    public Note(Date creationDate, String header, String text) {
        this.creationDate = creationDate;
        this.header = header;
        this.text = text;
    }

    public Note(String id, Date creationDate, String header, String text) {
        this.id = id;
        this.creationDate = creationDate;
        this.header = header;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
        Note note = (Note) o;
        return Objects.equals(id, note.id) &&
                Objects.equals(creationDate, note.creationDate) &&
                Objects.equals(header, note.header) &&
                text.equals(note.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, header, text);
    }

    @Override
    public String toString() {
        return "Note{" +
                "header='" + header + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
