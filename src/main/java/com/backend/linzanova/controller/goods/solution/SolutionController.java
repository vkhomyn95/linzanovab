package com.backend.linzanova.controller.goods.solution;

import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.solution.Solution;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.NoRightsException;
import com.backend.linzanova.exeption.NoSuchFileException;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.solution.ISolutionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
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
        log.info("Handling GET /solution/count");
        return solutionService.totalCount();
    }


    @GetMapping
    public SolutionPageDTO getAllSolution(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        log.info("Handling GET /solution");
        PageRequest pageRequest = PageRequest.of(page, size);
        return solutionService.getAllSolutions(pageRequest);
    }

    @GetMapping("/name")
    public SolutionPageDTO getSolutionsByName(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "2") int size,
                                              @RequestParam String name) {
        log.info("Handling GET /solution/name with name " + name);
        Pageable pageRequest = PageRequest.of(page, size);
        return solutionService.getSolutionsByName(pageRequest, name);
    }

    @GetMapping("/filter")
    public SolutionPageDTO getSolutionsFilterOption(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "2") int size,
                                                    @RequestParam String colName,
                                                    @RequestParam String name){
        log.info("Handling GET /solution/filter with colname: " + colName + " and name: " + name);
        final Pageable pageRequest = PageRequest.of(page, size);

        if (name.equals("true")){
            return solutionService.getSolutionsBoolHyaluronate(pageRequest);
        }else {
            return solutionService.getSolutionFilter(pageRequest, colName, name);
        }
    }

    @GetMapping(value = "/{solutionId}")
    public Solution getSolution(@PathVariable int solutionId) {
        log.info("Handling GET /solution/" + solutionId);
        return solutionService.getSolutionById(solutionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Solution saveSolution(@RequestHeader(value = "Authorization") String auth,
                                 @RequestBody @Valid Solution solution) {
        log.info("Handling POST /solution save request with object " + solution);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            solution.setCategory(2);
            solution.setAvailability(true);
            return solutionService.insertSolution(solution, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @PostMapping("/{solutionId}")
    public Solution updateSolution(@RequestHeader(value = "Authorization") String auth,
                                   @RequestBody Solution solution,
                                   @PathVariable int solutionId) {
        log.info("Handling POST /solution update request with object " + solution);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            solution.setCategory(2);
            solution.setId(solutionId);
            return solutionService.updateSolution(solution, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @PostMapping(value = "/{solutionId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PhotoResponseDTO uploadSolutionPhoto(@PathVariable int solutionId, MultipartFile file) throws AlreadyExistsException {
        log.info("Handling POST /solution image request with id " + solutionId);
        return solutionService.insertPhoto(solutionId, file);
    }

    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> getSolutionImage(@RequestParam String name,
                                                   @RequestHeader(value = "format") String format) throws IOException, NoSuchFileException {
        log.info("Handling GET /solution/image/" + name);

        if (format.equals("jpeg")){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(solutionService.getSolutionImage(name, format));
        }else {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("image/webp"))
                    .body(solutionService.getSolutionImage(name, format));
        }
    }
}
