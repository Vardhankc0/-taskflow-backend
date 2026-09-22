package com.example.Task.Management.System.Controller;

import com.example.Task.Management.System.DTO.TaskRequestDTO;
import com.example.Task.Management.System.DTO.TaskResponseDTO;
import com.example.Task.Management.System.DTO.TaskStatsResponseDTO;

import com.example.Task.Management.System.Entites.TaskPriority;
import com.example.Task.Management.System.Entites.TaskStatus;

import com.example.Task.Management.System.Service.TaskService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;


    public TaskController(
            TaskService taskService
    ) {

        this.taskService = taskService;
    }


    // =========================================
    // CREATE TASK
    // POST /tasks
    // =========================================

    @PostMapping
    public TaskResponseDTO createTask(
            @Valid
            @RequestBody
            TaskRequestDTO request
    ) {

        return taskService.create(request);
    }


    // =========================================
    // GET ALL TASKS
    // GET /tasks?page=0&size=6
    // =========================================

    @GetMapping
    public Page<TaskResponseDTO> getAllTasks(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "6"
            )
            int size
    ) {

        return taskService.getAll(
                page,
                size
        );
    }


    // =========================================
    // DASHBOARD STATISTICS
    // GET /tasks/stats
    // =========================================

    @GetMapping("/stats")
    public TaskStatsResponseDTO getStats() {

        return taskService.getStats();
    }


    // =========================================
    // FILTER BY STATUS
    // GET /tasks/status?status=TODO
    // =========================================

    @GetMapping("/status")
    public List<TaskResponseDTO> getByStatus(

            @RequestParam
            TaskStatus status
    ) {

        return taskService.getByStatus(
                status
        );
    }


    // =========================================
    // FILTER BY PRIORITY
    // GET /tasks/priority?priority=HIGH
    // =========================================

    @GetMapping("/priority")
    public List<TaskResponseDTO> getByPriority(

            @RequestParam
            TaskPriority priority
    ) {

        return taskService.getByPriority(
                priority
        );
    }


    // =========================================
    // STATUS + PRIORITY FILTER
    //
    // GET
    // /tasks/filter?status=TODO&priority=HIGH
    // =========================================

    @GetMapping("/filter")
    public List<TaskResponseDTO>
    getByStatusAndPriority(

            @RequestParam
            TaskStatus status,

            @RequestParam
            TaskPriority priority
    ) {

        return taskService
                .getByStatusAndPriority(
                        status,
                        priority
                );
    }


    // =========================================
    // SEARCH TASKS
    //
    // GET /tasks/search?keyword=Spring
    // =========================================

    @GetMapping("/search")
    public List<TaskResponseDTO> searchTasks(

            @RequestParam
            String keyword
    ) {

        return taskService
                .searchByTitle(keyword);
    }


    // =========================================
    // OVERDUE TASKS
    // GET /tasks/overdue
    // =========================================

    @GetMapping("/overdue")
    public List<TaskResponseDTO>
    getOverdueTasks() {

        return taskService
                .getOverdueTasks();
    }


    // =========================================
    // GET TASK BY ID
    // GET /tasks/{id}
    // =========================================

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(

            @PathVariable
            Long id
    ) {

        return taskService.getById(id);
    }


    // =========================================
    // UPDATE TASK
    // PUT /tasks/{id}
    // =========================================

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            TaskRequestDTO request
    ) {

        return taskService.update(
                id,
                request
        );
    }


    // =========================================
    // DELETE TASK
    // DELETE /tasks/{id}
    // =========================================

    @DeleteMapping("/{id}")
    public void deleteTask(

            @PathVariable
            Long id
    ) {

        taskService.delete(id);
    }
}
