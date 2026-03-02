package com.ron.taskmanager.service;

import com.ron.taskmanager.model.User;
import com.ron.taskmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Implementation of Spring Security's UserDetailsService.
 * Responsible for loading a user from the database by username.
 * Used by Spring Security during the authentication process
 * to verify the user's identity.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username){
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent()) return optionalUser.get();
        throw new UsernameNotFoundException(
                "the user with the username: "+username+" is not found");
    }
}
