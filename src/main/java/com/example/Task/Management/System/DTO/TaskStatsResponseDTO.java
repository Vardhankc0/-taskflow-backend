package com.example.Task.Management.System.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskStatsResponseDTO {

    private long total;

    private long todo;

    private long inProgress;

    private long completed;

    private long overdue;
}