package com.ron.taskmanager.controller;

import com.ron.taskmanager.model.Task;
import com.ron.taskmanager.service.BulkImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Bulk Import", description = "APIs for Bulk Import tasks")
public class BulkImportController {
    private final BulkImportService bulkImportService;

    public BulkImportController(BulkImportService bulkImportService){
        this.bulkImportService = bulkImportService;
    }

    @PostMapping("/bulk-import")
    public ResponseEntity<String> bulkImportTasks(@RequestBody List<Task> tasks){
        bulkImportService.importTasks(tasks);
        return ResponseEntity
                .accepted()
                .body("Bulk import started for "+tasks.size()+" tasks");
    }
}
