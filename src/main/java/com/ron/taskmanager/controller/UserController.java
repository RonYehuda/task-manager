package com.ron.taskmanager.controller;

import com.ron.taskmanager.model.User;
import com.ron.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@Tag(name = "User Management",description = "APIs for managing users - create, read, update, and delete tasks")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> users(){
        return ResponseEntity.ok(userService.users());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id){
        return userService.findUserById(id)
                .map(ResponseEntity::ok).
                orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        userService.createNewUser(user);
        return ResponseEntity.status(201).body(user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        userService.updateUser(id, user);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/order")
    public ResponseEntity<List<User>> allUserOrderByUsername(){
        return ResponseEntity.ok(userService.allUserOrderByUsername());
    }

}
