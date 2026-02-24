package com.ron.taskmanager.controller;

import com.ron.taskmanager.model.TaskAttachment;
import com.ron.taskmanager.service.AttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Attachment Management", description = "APIs for managing task attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService){
        this.attachmentService =attachmentService;
    }

    @GetMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<List<TaskAttachment>>taskAttachments(@PathVariable Long taskId) throws IOException {
        return ResponseEntity.ok(attachmentService.getAttachments(taskId));
    }

    @GetMapping("/attachments/{id}/download")
    public ResponseEntity<String>getPresignedURL(@PathVariable Long id)throws IOException{
        return ResponseEntity.ok(attachmentService.generateDownloadUrl(id));
    }

    // accept file upload only
    @PostMapping(value = "/tasks/{taskId}/attachments", consumes = "multipart/form-data")
    // "file" must match the field name sent by client
    public ResponseEntity<TaskAttachment> addAttachment(@PathVariable Long taskId,
                                              @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.status(201).body(attachmentService.uploadAttachment(taskId,file));
    }

    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) throws IOException {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.status(204).build();
    }
}
