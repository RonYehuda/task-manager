package com.ron.taskmanager.repository;

import com.ron.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByUsername(String user);

    List<User> findAllByOrderByUsernameAsc();

    Optional<User> findUserByUsername(String username);
}
