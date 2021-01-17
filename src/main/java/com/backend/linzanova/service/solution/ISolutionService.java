package com.backend.linzanova.service.solution;

import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.solution.Solution;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface ISolutionService {

    Solution insertSolution(Solution solution, String username);

    SolutionPageDTO getAllSolutions(PageRequest pageRequest);

    Solution getSolutionById(int id);

    Solution updateSolution(Solution solution, String username);

    void removeSolution(int id);

    Long totalCount();

    SolutionPageDTO getSolutionsByName(Pageable pageable, String name);

    SolutionPageDTO getSolutionFilter(Pageable pageable, String colName, String name);

    SolutionPageDTO getSolutionsBoolHyaluronate(Pageable pageable);
}
