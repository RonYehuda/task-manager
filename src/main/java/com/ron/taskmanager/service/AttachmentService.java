package com.ron.taskmanager.service;

import com.ron.taskmanager.model.Task;
import com.ron.taskmanager.model.TaskAttachment;
import com.ron.taskmanager.repository.TaskAttachmentRepository;
import com.ron.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    private final TaskAttachmentRepository taskAttachmentRepository;
    private final TaskRepository taskRepository;
    private final S3Service s3Service;
    public AttachmentService(TaskAttachmentRepository taskAttachmentRepository,
                             TaskRepository taskRepository,
                             S3Service s3Service){
        this.taskAttachmentRepository = taskAttachmentRepository;
        this.taskRepository = taskRepository;
        this.s3Service = s3Service;
    }

    public TaskAttachment uploadAttachment(Long taskId, MultipartFile file) throws IOException {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()){
            Task task = optionalTask.get();
            String s3key = "attachments/task"+task.getId()+"/"+file.getOriginalFilename();
            TaskAttachment taskAttachment = new TaskAttachment();
            taskAttachment.setTask(task);
            taskAttachment.setFileName(file.getOriginalFilename());
            taskAttachment.setFileSize(file.getSize());
            taskAttachment.setFileType(file.getContentType());
            taskAttachment.setS3Key(s3key);
            s3Service.uploadFile(file,taskAttachment.getFileType(),s3key);
            taskAttachmentRepository.save(taskAttachment);
            return taskAttachment;
        }
        throw new IOException("Task is not exist");
    }

    public List<TaskAttachment>getAttachments(Long taskId) throws IOException {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()){
            return taskAttachmentRepository.findByTaskId(taskId);
        }
        throw new IOException("Task is not exist");
    }

    public String generateDownloadUrl(Long attachmentId) throws IOException {
        Optional<TaskAttachment> optionalTaskAttachment = taskAttachmentRepository.findById(attachmentId);
        if (optionalTaskAttachment.isPresent()){
            TaskAttachment taskAttachment = optionalTaskAttachment.get();
            return s3Service.generatePresignedUrl(taskAttachment.getS3Key());
        }
        throw new IOException("Attachment not exist");
    }

    public void deleteAttachment(Long attachmentId) throws IOException{
        Optional<TaskAttachment> optionalTaskAttachment = taskAttachmentRepository.findById(attachmentId);
        if (optionalTaskAttachment.isPresent()){
            s3Service.deleteFile(optionalTaskAttachment.get().getS3Key());
            taskAttachmentRepository.deleteById(attachmentId);
        }
        else {throw new IOException("Attachment not exist");}
    }
}
