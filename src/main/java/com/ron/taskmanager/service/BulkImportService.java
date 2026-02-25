package com.ron.taskmanager.service;

import com.ron.taskmanager.model.Task;
import com.ron.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkImportService {
    private final Logger logger = LoggerFactory.getLogger(BulkImportService.class);
    private final TaskRepository taskRepository;

    public BulkImportService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }
    @Async
    public void importTasks (List<Task> tasks){
        logger.info("Starting bulk import of {} tasks",tasks.size());
        for(Task task: tasks){
            taskRepository.save(task);
            logger.info("Imported task: {}",task.getTitle());
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                logger.error("Failed to import task: {}",task.getTitle());
            }
        }
        logger.info("Bulk import completed!");
    }
}
