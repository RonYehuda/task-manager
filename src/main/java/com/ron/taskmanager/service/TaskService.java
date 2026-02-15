package com.ron.taskmanager.service;

import com.ron.taskmanager.model.Task;
import com.ron.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){

        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id){
        return taskRepository.findById(id);
    }

    public Task create(Task task){
        if (task.getTitle() == null || task.getTitle().isEmpty()){
            throw new IllegalArgumentException("Title can't be empty");
        }
        return taskRepository.save(task);
    }

    public Task update(Long id, Task task){
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()){
            task.setId(id);
            return taskRepository.save(task);
        }
        throw new IllegalArgumentException("Task not found");
    }

    public Task markAsCompleted(Long id){
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()){
            Task task = optionalTask.get();
            task.setCompleted(true);
            return taskRepository.save(task);
        }
        throw new IllegalArgumentException("Task not found");
    }

    public void delete(Long id){
        taskRepository.deleteById(id);
    }

    public List<Task> incompleteTasks(){
        return taskRepository.findByCompletedIsFalse();
    }

    public Optional<Task> findByTitle(String title){
        return taskRepository.findByTitle(title);
    }

    public List<Task> findByDescription(String description){
        return taskRepository.findByDescriptionContaining(description);
    }

    public long amountOfCompletedTasks(boolean completed){
        return taskRepository.countByCompleted(completed);
    }

    public List<Task> allTasksOrderByTitle(){
        return taskRepository.findAllByOrderByTitleAsc();
    }
}

