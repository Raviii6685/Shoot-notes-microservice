package com.utu.notes_service.Repository;


import com.utu.notes_service.Models.Notes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotesRepository extends MongoRepository<Notes, ObjectId> {
    Notes getNotesById(String fileId);
}
