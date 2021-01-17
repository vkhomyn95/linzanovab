package com.backend.linzanova.service.solution;

import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.solution.SolutionDao;
import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.solution.Solution;
import com.backend.linzanova.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.backend.linzanova.dao.solution.SolutionSpecification.byColumnNameAndValue;


@Service
public class SolutionService implements ISolutionService {

    @Autowired
    private SolutionDao solutionDao;

    @Autowired
    private IUserDao userDao;

    public SolutionService(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    @Override
    public Solution insertSolution(Solution solution, String username) {
        final User user = userDao.findByEmail(username);
        solution.setUser(user);
        return solutionDao.save(solution);
    }

    @Override
    public SolutionPageDTO getAllSolutions(PageRequest pageRequest) {
        final Page<Solution> all = solutionDao.findAll(pageRequest);
        return new SolutionPageDTO(all.getContent(), all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public Solution getSolutionById(int id) {
        return solutionDao.getOne(id);
    }

    @Override
    public Solution updateSolution(Solution solution, String username) {
        final User user = userDao.findByEmail(username);
        solution.setUser(user);
        return solutionDao.save(solution);
    }

    @Override
    public void removeSolution(int id) {
        solutionDao.deleteById(id);
    }

    @Override
    public Long totalCount() {
        return solutionDao.countSolutions();
    }

    @Override
    public SolutionPageDTO getSolutionsByName(Pageable pageable, String name) {
        final Page<Solution> byNameContains = solutionDao.findByNameContains(pageable, name);
        return new SolutionPageDTO(byNameContains.getContent(), byNameContains.getTotalElements(), byNameContains.getSize(), byNameContains.isEmpty(), byNameContains.getTotalPages());
    }

    @Override
    public SolutionPageDTO getSolutionFilter(Pageable pageable, String colName, String nameValue) {
        final Page<Solution> byFIlter = solutionDao.findAll(byColumnNameAndValue(colName, nameValue), pageable);
        return new SolutionPageDTO(byFIlter.getContent(), byFIlter.getTotalElements(), byFIlter.getSize(), byFIlter.isEmpty(), byFIlter.getTotalPages());
    }

    @Override
    public SolutionPageDTO getSolutionsBoolHyaluronate(Pageable pageable) {
        final Page<Solution> byHyaluronate = solutionDao.findSolutionsByBoolHyaluronateIsTrue(pageable);
        return new SolutionPageDTO(byHyaluronate.getContent(), byHyaluronate.getTotalElements(), byHyaluronate.getSize(), byHyaluronate.isEmpty(), byHyaluronate.getTotalPages());
    }
}
