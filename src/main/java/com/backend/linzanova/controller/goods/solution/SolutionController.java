package com.backend.linzanova.controller.goods.solution;

import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.solution.Solution;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.solution.ISolutionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/solution")
public class SolutionController {

    private ISolutionService solutionService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping(value = "/count")
    public Long countAllSolutions() {
        return solutionService.totalCount();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Solution saveSolution(@RequestHeader(value = "Authorization") String auth,
                                 @RequestBody @Valid Solution solution) {
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            solution.setCategory(2);
            solution.setAvailability(true);
            return solutionService.insertSolution(solution, userDetails.getUsername());
        }else{
            throw new RuntimeException("No rights");
        }

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

    @GetMapping("/filter")
    public SolutionPageDTO getSolutionsFilterOption(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "2") int size,
                                                    @RequestParam String colName,
                                                    @RequestParam String name){
        final Pageable pageRequest = PageRequest.of(page, size);

        if (name.equals("true")){
            return solutionService.getSolutionsBoolHyaluronate(pageRequest);
        }else {
            return solutionService.getSolutionFilter(pageRequest, colName, name);
        }

    }

    @GetMapping(value = "/{solutionId}")
    public Solution getSolution(@PathVariable int solutionId) {
        return solutionService.getSolutionById(solutionId);
    }

    @PostMapping("/{solutionId}")
    public Solution updateSolution(@RequestHeader(value = "Authorization") String auth,
                                   @RequestBody Solution solution,
                                   @PathVariable int solutionId) {
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            solution.setCategory(2);
            solution.setId(solutionId);
            return solutionService.updateSolution(solution, userDetails.getUsername());
        }else{
            throw new RuntimeException("No rights");
        }
    }
}
