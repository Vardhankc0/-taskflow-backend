package com.example.Task.Management.System.Service;

import com.example.Task.Management.System.DTO.TaskRequestDTO;
import com.example.Task.Management.System.DTO.TaskResponseDTO;
import com.example.Task.Management.System.DTO.TaskStatsResponseDTO;

import com.example.Task.Management.System.Entites.Task;
import com.example.Task.Management.System.Entites.TaskPriority;
import com.example.Task.Management.System.Entites.TaskStatus;
import com.example.Task.Management.System.Entites.User;

import com.example.Task.Management.System.Exception.TaskNotFoundException;

import com.example.Task.Management.System.Repository.TaskRepository;
import com.example.Task.Management.System.Repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;


    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {

        this.taskRepository = taskRepository;

        this.userRepository = userRepository;
    }


    // =========================================
    // CREATE TASK
    // =========================================

    public TaskResponseDTO create(
            TaskRequestDTO request
    ) {

        User user = getCurrentUser();


        Task task = new Task();

        task.setTitle(
                request.getTitle()
        );

        task.setDescription(
                request.getDescription()
        );

        task.setStatus(
                request.getStatus()
        );

        task.setPriority(
                request.getPriority()
        );

        task.setDueDate(
                request.getDueDate()
        );


        task.setCreatedAt(
                LocalDateTime.now()
        );

        task.setUpdatedAt(
                LocalDateTime.now()
        );


        // Important:
        // connect task with logged-in user

        task.setUser(user);


        Task savedTask =
                taskRepository.save(task);


        return convertToResponseDTO(
                savedTask
        );
    }


    // =========================================
    // GET ALL TASKS
    // =========================================

    public Page<TaskResponseDTO> getAll(
            int page,
            int size
    ) {

        User user =
                getCurrentUser();


        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );


        Page<Task> tasks =
                taskRepository.findByUser(
                        user,
                        pageable
                );


        return tasks.map(
                this::convertToResponseDTO
        );
    }


    // =========================================
    // GET TASK BY ID
    // =========================================

    public TaskResponseDTO getById(
            Long id
    ) {

        User user =
                getCurrentUser();


        Task task =
                taskRepository
                        .findByIdAndUser(
                                id,
                                user
                        )
                        .orElseThrow(
                                () ->
                                        new TaskNotFoundException(
                                                "Task not found with id: "
                                                        + id
                                        )
                        );


        return convertToResponseDTO(
                task
        );
    }


    // =========================================
    // UPDATE TASK
    // =========================================

    public TaskResponseDTO update(
            Long id,
            TaskRequestDTO request
    ) {

        User user =
                getCurrentUser();


        Task existingTask =
                taskRepository
                        .findByIdAndUser(
                                id,
                                user
                        )
                        .orElseThrow(
                                () ->
                                        new TaskNotFoundException(
                                                "Task not found with id: "
                                                        + id
                                        )
                        );


        existingTask.setTitle(
                request.getTitle()
        );

        existingTask.setDescription(
                request.getDescription()
        );

        existingTask.setStatus(
                request.getStatus()
        );

        existingTask.setPriority(
                request.getPriority()
        );

        existingTask.setDueDate(
                request.getDueDate()
        );


        existingTask.setUpdatedAt(
                LocalDateTime.now()
        );


        Task updatedTask =
                taskRepository.save(
                        existingTask
                );


        return convertToResponseDTO(
                updatedTask
        );
    }


    // =========================================
    // DELETE TASK
    // =========================================

    public void delete(
            Long id
    ) {

        User user =
                getCurrentUser();


        Task task =
                taskRepository
                        .findByIdAndUser(
                                id,
                                user
                        )
                        .orElseThrow(
                                () ->
                                        new TaskNotFoundException(
                                                "Task not found with id: "
                                                        + id
                                        )
                        );


        taskRepository.delete(task);
    }


    // =========================================
    // FILTER BY STATUS
    // =========================================

    public List<TaskResponseDTO> getByStatus(
            TaskStatus status
    ) {

        User user =
                getCurrentUser();


        return taskRepository
                .findByUserAndStatus(
                        user,
                        status
                )
                .stream()
                .map(
                        this::convertToResponseDTO
                )
                .toList();
    }


    // =========================================
    // FILTER BY PRIORITY
    // =========================================

    public List<TaskResponseDTO> getByPriority(
            TaskPriority priority
    ) {

        User user =
                getCurrentUser();


        return taskRepository
                .findByUserAndPriority(
                        user,
                        priority
                )
                .stream()
                .map(
                        this::convertToResponseDTO
                )
                .toList();
    }


    // =========================================
    // STATUS + PRIORITY
    // =========================================

    public List<TaskResponseDTO>
    getByStatusAndPriority(
            TaskStatus status,
            TaskPriority priority
    ) {

        User user =
                getCurrentUser();


        return taskRepository
                .findByUserAndStatusAndPriority(
                        user,
                        status,
                        priority
                )
                .stream()
                .map(
                        this::convertToResponseDTO
                )
                .toList();
    }


    // =========================================
    // SEARCH BY TITLE
    // =========================================

    public List<TaskResponseDTO> searchByTitle(
            String keyword
    ) {

        User user =
                getCurrentUser();


        return taskRepository
                .findByUserAndTitleContainingIgnoreCase(
                        user,
                        keyword
                )
                .stream()
                .map(
                        this::convertToResponseDTO
                )
                .toList();
    }


    // =========================================
    // OVERDUE TASKS
    // =========================================

    public List<TaskResponseDTO>
    getOverdueTasks() {

        User user =
                getCurrentUser();


        return taskRepository
                .findByUserAndDueDateBeforeAndStatusNot(
                        user,
                        LocalDate.now(),
                        TaskStatus.COMPLETED
                )
                .stream()
                .map(
                        this::convertToResponseDTO
                )
                .toList();
    }


    // =========================================
    // DASHBOARD STATISTICS
    // =========================================

    public TaskStatsResponseDTO getStats() {

        User user =
                getCurrentUser();


        // All tasks belonging to user

        long total =
                taskRepository.countByUser(
                        user
                );


        // TODO tasks

        long todo =
                taskRepository
                        .countByUserAndStatus(
                                user,
                                TaskStatus.TODO
                        );


        // IN_PROGRESS tasks

        long inProgress =
                taskRepository
                        .countByUserAndStatus(
                                user,
                                TaskStatus.IN_PROGRESS
                        );


        // COMPLETED tasks

        long completed =
                taskRepository
                        .countByUserAndStatus(
                                user,
                                TaskStatus.COMPLETED
                        );


        // Due date is before today
        // AND status is not COMPLETED

        long overdue =
                taskRepository
                        .countByUserAndDueDateBeforeAndStatusNot(
                                user,
                                LocalDate.now(),
                                TaskStatus.COMPLETED
                        );


        return new TaskStatsResponseDTO(
                total,
                todo,
                inProgress,
                completed,
                overdue
        );
    }


    // =========================================
    // CURRENT LOGGED-IN USER
    // =========================================

    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();


        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "User not found"
                                )
                );
    }


    // =========================================
    // ENTITY -> RESPONSE DTO
    // =========================================

    private TaskResponseDTO
    convertToResponseDTO(
            Task task
    ) {

        return new TaskResponseDTO(

                task.getId(),

                task.getTitle(),

                task.getDescription(),

                task.getStatus(),

                task.getPriority(),

                task.getDueDate(),

                task.getCreatedAt(),

                task.getUpdatedAt()
        );
    }
}