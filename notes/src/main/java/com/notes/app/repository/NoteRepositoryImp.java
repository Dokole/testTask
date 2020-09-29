package com.notes.app.repository;

import com.notes.app.domain.Note;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class NoteRepositoryImp implements NoteRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public NoteRepositoryImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Note> findNoteById(String id) {
        Note note = mongoTemplate.findById(new ObjectId(id), Note.class);
        return Optional.ofNullable(note);
    }

    public List<Note> findAllNotes() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.find(query, Note.class);
    }

    @Override
    public List<Note> search(@NotNull List<String> fields, String textToSearch) {
        //In case search bar is empty
        if (textToSearch == null || textToSearch.isBlank()) {
            return findAllNotes();
        }
        List<Criteria> criterias = new ArrayList<>();
        String regex = MongoRegexCreator.INSTANCE.toRegularExpression(
                textToSearch, MongoRegexCreator.MatchMode.CONTAINING
        );
        for (String field : fields) {
            criterias.add(Criteria.where(field).regex(regex));
        }
        Criteria criteria = new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]));
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.find(query, Note.class);
    }

    @Override
    public Note saveNote(Note note) {
        return mongoTemplate.insert(note);
    }

    @Override
    public void deleteNoteById(String id) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        mongoTemplate.remove(query, Note.class);
    }

    @Override
    public void deleteAllNotes() {
        mongoTemplate.remove(new Query(), Note.class);
    }
}
