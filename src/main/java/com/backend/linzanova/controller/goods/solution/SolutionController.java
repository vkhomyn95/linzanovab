package com.backend.linzanova.controller.goods.solution;

import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.solution.Solution;
import com.backend.linzanova.service.solution.ISolutionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/solution")
public class SolutionController {

    private ISolutionService solutionService;

    @GetMapping(value = "/count")
    public Long countAllSolutions() {
        return solutionService.totalCount();
    }

    @PostMapping(value = "/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Solution saveSolution(@RequestBody @Valid Solution solution, @PathVariable int userId) {
        solution.setCategory(2);
        solution.setAvailability(true);
        return solutionService.insertSolution(solution, userId);
    }

    @GetMapping
    public SolutionPageDTO getAllSolution(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return solutionService.getAllSolutions(pageRequest);
    }

    @GetMapping("/name")
    public SolutionPageDTO getSolutionsByName(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "2") int size,
                                              @RequestParam String name) {
        Pageable pageRequest = PageRequest.of(page, size);
        return solutionService.getSolutionsByName(pageRequest, name);
    }

    @GetMapping(value = "/{solutionId}")
    public Solution getSolution(@PathVariable int solutionId) {
        return solutionService.getSolutionById(solutionId);
    }

    @PostMapping("/{solutionId}/user/{userId}")
    public Solution updateSolution(@RequestBody Solution solution, @PathVariable int solutionId, @PathVariable int userId) {
        solution.setId(solutionId);
        return solutionService.updateSolution(solution, userId);
    }
}
