package com.ron.taskmanager.controller;

import com.ron.taskmanager.model.Task;
import com.ron.taskmanager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management",description = "APIs for managing tasks - create, read, update, and delete tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> tasks(){
        return ResponseEntity.ok(taskService.findAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isPresent()){
            return ResponseEntity.ok(optionalTask.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<Task> create(@RequestBody Task task){
        taskService.create(task);
        return ResponseEntity.status(201).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
            @PathVariable Long id,
            @RequestBody Task task){
        Task updated = taskService.update(id,task);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Mark task as completed")
    public ResponseEntity<Task> completed(@PathVariable Long id){
        Task task = taskService.markAsCompleted(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> incompleteTasks(){
        return ResponseEntity.ok(taskService.incompleteTasks());
    }

    @GetMapping("/search/title")
    public ResponseEntity<Task> findByTitle(@RequestParam String title) {
        return taskService.findByTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<Task>> findTasksByDescription(@RequestParam String description){
        return ResponseEntity.ok(taskService.findByDescription(description));
    }

    @GetMapping("/count/completed")
    public ResponseEntity<Long> amountOfCompletedTasks(){
        return ResponseEntity.ok(taskService.amountOfCompletedTasks(true));
    }

    @GetMapping("/count/incomplete")
    public ResponseEntity<Long> amountOfIncompletedTasks(){
        return ResponseEntity.ok(taskService.amountOfCompletedTasks(false));
    }

    @GetMapping("/ordered")
    public ResponseEntity<List<Task>> allTasksOrderByTitles(){
        return ResponseEntity.ok(taskService.allTasksOrderByTitle());
    }
}
