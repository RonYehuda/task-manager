package com.ron.taskmanager.service;

import com.ron.taskmanager.model.Task;
import com.ron.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Page<Task> findAllPaginated(int page, int size, String sortBy, String sortDirection){
        Sort sort = sortDirection.equals("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size,sort);
        return taskRepository.findAll(pageable);
    }

    public Page<Task> findAllFiltered(int page, int size, String sortBy, String sortDirection,
                                      Boolean completed, Long userId, String title){
        Sort sort = sortDirection.equals("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);

        // Initializes the specification with a "1=1" condition (always true)
        //to avoid null issues and allow dynamic chaining of further "AND" predicates.
        Specification<Task> taskSpecification = (root,
                                                 query,
                                                 cb) -> cb.conjunction();
        if (completed != null){
            taskSpecification = taskSpecification.and(
                    (root, query, cb) ->
                    cb.equal(root.get("completed"), completed));
        }
        if (userId != null) {
            taskSpecification = taskSpecification.and(
                    (root,query, cb)->
                    cb.equal(root.get("user").get("id"),userId));
        }
        if (title != null){
            taskSpecification = taskSpecification.and(
                    ((root, query, cb) ->
                            cb.like(root.get("title"),"%"+title+"%"))
            );
        }
        return taskRepository.findAll(taskSpecification,pageable);
    }

    public Page<Task> findDateFilter(int page, int size, String sortBy, String sortDirection, LocalDate createAt){
        Sort sort = sortDirection.equals("asc")?
                Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);

        Specification<Task> spec = ((root, query, cb) -> cb.conjunction());

        if (createAt != null){
            spec = spec.and(
                    ((root,
                      query,
                      cb) -> cb.greaterThanOrEqualTo(root.get("createAt"),createAt)));
        }
        return taskRepository.findAll(spec,pageable);
    }

}

