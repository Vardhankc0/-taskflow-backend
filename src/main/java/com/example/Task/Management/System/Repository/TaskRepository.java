package com.example.Task.Management.System.Repository;

import com.example.Task.Management.System.Entites.Task;
import com.example.Task.Management.System.Entites.TaskPriority;
import com.example.Task.Management.System.Entites.TaskStatus;
import com.example.Task.Management.System.Entites.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository
        extends JpaRepository<Task, Long> {

    Page<Task> findByUser(
            User user,
            Pageable pageable
    );

    Optional<Task> findByIdAndUser(
            Long id,
            User user
    );

    List<Task> findByUserAndStatus(
            User user,
            TaskStatus status
    );

    List<Task> findByUserAndPriority(
            User user,
            TaskPriority priority
    );

    List<Task> findByUserAndStatusAndPriority(
            User user,
            TaskStatus status,
            TaskPriority priority
    );

    List<Task> findByUserAndTitleContainingIgnoreCase(
            User user,
            String keyword
    );

    List<Task> findByUserAndDueDateBeforeAndStatusNot(
            User user,
            LocalDate date,
            TaskStatus status
    );


    // =========================
    // DASHBOARD STATISTICS
    // =========================

    long countByUser(
            User user
    );

    long countByUserAndStatus(
            User user,
            TaskStatus status
    );

    long countByUserAndDueDateBeforeAndStatusNot(
            User user,
            LocalDate date,
            TaskStatus status
    );
}
