package com.backend.linzanova.service.solution;

import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.solution.SolutionDao;
import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.solution.Solution;
import com.backend.linzanova.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



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
    public Solution insertSolution(Solution solution, int userId) {
        final User user = userDao.getOne(userId);
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
    public Solution updateSolution(Solution solution, int userId) {
        final User user = userDao.getOne(userId);
        solution.setCategory(2);
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
}
