package com.ron.taskmanager.controller;

import com.ron.taskmanager.model.User;
import com.ron.taskmanager.service.JwtService;
import com.ron.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Register and Login endpoints")
public class AuthController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(BCryptPasswordEncoder bCryptPasswordEncoder,
                          UserService userService,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<String>  register(@RequestBody User user){
        String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userService.createNewUser(user);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@RequestBody User user){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        return ResponseEntity.ok(jwtService.generateToken(user.getUsername()));
    }
}
