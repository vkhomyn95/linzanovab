package com.backend.linzanova.service.solution;

import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.solution.Solution;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.NoSuchFileException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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

    PhotoResponseDTO insertPhoto(int solutionId, MultipartFile file) throws AlreadyExistsException;

    byte[] getSolutionImage(String name, String format) throws NoSuchFileException;
}
