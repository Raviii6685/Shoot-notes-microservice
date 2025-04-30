package com.utu.notes_service.Services;
import com.notes.grpc.*;
import com.utu.notes_service.Models.Notes;
import com.utu.notes_service.Repository.NotesRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class NotesService extends notesServiceGrpc.notesServiceImplBase {

    private final NotesRepository notesRepository;

    @Override
    public void getFile(FileDownloadRequest request, StreamObserver<FileDownloadResponse> responseObserver) {
        try {
            ObjectId id  = new ObjectId(request.getFileId());
            Optional<Notes> response = notesRepository.findById(id);
            if (response.isPresent()) {
                Notes note = response.get();
                FileDownloadResponse fileDownloadResponse = FileDownloadResponse.newBuilder()
                        .setFileName(note.getFileName())
                        .setFileType(note.getFileType())
                        .setContent(com.google.protobuf.ByteString.copyFrom(note.getContent()))
                        .build();

                responseObserver.onNext(fileDownloadResponse);
                responseObserver.onCompleted();
            } else {
                throw new Exception("Note not found");
            }
        } catch (Exception e) {
            StatusRuntimeException exception = Status.INTERNAL
                    .withDescription("Something went wrong while fetching file")
                    .withCause(e)
                    .asRuntimeException();

            responseObserver.onError(exception);
        }
    }

    @Override
    public void uploadFile(requestFileUpload request, StreamObserver<responseFileUpload> responseObserver) {
        try {
            Notes note = Notes.builder().fileName(request.getFileName())
                    .fileType(request.getFileType())
                    .content(request.getContent().toByteArray())
                    .folderId(request.getFolderId())
                    .userId(request.getUserId())
                    .build();
            ObjectId id =notesRepository.save(note).getId();
            responseFileUpload response = responseFileUpload.newBuilder()
                    .setStatus("Success")
                    .setMessage("File uploaded successfully.")
                    .setFileId(id.toHexString())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            StatusRuntimeException exception = Status.INTERNAL
                    .withDescription("Something went wrong while uploading")
                    .withCause(e)
                    .asRuntimeException();

            responseObserver.onError(exception);
        }




    }
}