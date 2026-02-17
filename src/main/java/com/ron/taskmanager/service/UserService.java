package com.ron.taskmanager.service;

import com.ron.taskmanager.model.User;
import com.ron.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    public List<User> users(){
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }

    public User createNewUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){ //update user
            user.setId(id);
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("user don't exist");
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }
    public List<User> findByUsername(String username){
        if(userRepository.findByUsername(username).isEmpty()) {
            System.out.println("User don't found");
            return userRepository.findByUsername(username);
        }
        return userRepository.findByUsername(username);
    }

    public List<User> allUserOrderByUsername(){
        return userRepository.findAllByOrderByUsernameAsc();
    }


}
