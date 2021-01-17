package com.backend.linzanova.dao.solution;

import com.backend.linzanova.entity.solution.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface SolutionDao extends JpaRepository<Solution, Integer>, JpaSpecificationExecutor<Solution> {

    @Query("SELECT COUNT(s) FROM Solution s")
    Long countSolutions();

    Page<Solution> findByNameContains(Pageable pageable, String name);

    Page<Solution> findSolutionsByBoolHyaluronateIsTrue(Pageable pageable);
}
