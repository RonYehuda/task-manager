package com.ron.taskmanager.repository;

import com.ron.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // SQL: SELECT * FROM tasks WHERE completed = false
    List<Task> findByCompletedIsFalse();

    // SQL: SELECT * FROM tasks WHERE title = ?
    Optional<Task> findByTitle(String title);

    // SQL: SELECT * FROM tasks WHERE description LIKE %?%
    List<Task> findByDescriptionContaining(String description);

    // SQL: SELECT COUNT(*) FROM tasks WHERE completed = ?
    long countByCompleted(boolean completed);

    // SQL: SELECT * FROM tasks ORDER BY title ASC
    List<Task> findAllByOrderByTitleAsc();
}
