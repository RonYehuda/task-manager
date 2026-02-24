package com.ron.taskmanager.repository;

import com.ron.taskmanager.model.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment,Long> {

    List<TaskAttachment> findByTaskId(Long taskId);
}
