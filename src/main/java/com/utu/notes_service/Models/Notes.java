package com.utu.notes_service.Models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user_notes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notes {
    @Id
    private ObjectId id;

    private String userId;
    private String folderId;
    private String fileName;
    private String fileType ;
    private Long size ;
    private byte[] content;
}
