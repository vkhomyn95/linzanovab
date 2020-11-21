package com.backend.linzanova.dto;

import com.backend.linzanova.entity.solution.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SolutionPageDTO {

    private List<Solution> solutions;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;
}
