package com.ron.taskmanager.controller;

import com.ron.taskmanager.model.Task;
import com.ron.taskmanager.model.User;
import com.ron.taskmanager.service.TaskService;

import com.ron.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management",description = "APIs for managing tasks - create, read, update, and delete tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<Task>> tasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc" ) String sortDirection){
        return ResponseEntity.ok(taskService.findAllPaginated(page,size,sortBy,sortDirection));
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
    public ResponseEntity<Task> create(@RequestBody Task task, @RequestParam Long userId){
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            task.setUser(user);
            taskService.create(task);
            return ResponseEntity.status(201).body(task);
        }
        throw new IllegalArgumentException("User not exist");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task){
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

    @GetMapping("/filter")
    public ResponseEntity<Page<Task>> tasksFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String title){
        Page<Task> taskPage = taskService.findAllFiltered(page, size, sortBy, sortDirection, completed, userId, title);
        return ResponseEntity.ok(taskPage);
    }

    @GetMapping("/filter/date")
    public ResponseEntity<Page<Task>> taskDateFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam LocalDate createAt){
        Page<Task> taskPage = taskService.findDateFilter(page, size, sortBy, sortDirection, createAt);
        return ResponseEntity.ok(taskPage);
    }
}
