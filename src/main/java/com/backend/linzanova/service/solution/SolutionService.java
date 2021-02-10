package com.backend.linzanova.service.solution;

import com.backend.linzanova.config.StorageProperties;
import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.solution.SolutionDao;
import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.dto.SolutionPageDTO;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.solution.Solution;
import com.backend.linzanova.entity.special.Special;
import com.backend.linzanova.entity.user.User;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.DoesNotExistException;
import com.backend.linzanova.exeption.NoSuchFileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.backend.linzanova.dao.solution.SolutionSpecification.byColumnNameAndValue;


@Service
@Slf4j
public class SolutionService implements ISolutionService {

    @Autowired
    private SolutionDao solutionDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private StorageProperties storageProperties;

    private Path rootFolder;

    @PostConstruct
    public void init(){
        try {
            rootFolder = Paths.get(storageProperties.getSolutions()).toAbsolutePath().normalize();
            Files.createDirectory(rootFolder);
        } catch (IOException e) {
            log.error("Unable to create folder " + e.getMessage());
        }
    }

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
        return solutionDao.findById(id).orElseThrow(() -> new DoesNotExistException("Товару у категорії розчини з id: " + id + " не існує"));
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

    @Override
    public PhotoResponseDTO insertPhoto(int solutionId, MultipartFile file) throws AlreadyExistsException {
        Solution solution = solutionDao.findById(solutionId).orElseThrow(() -> new DoesNotExistException("Товару у категорії розчини з id: " + solutionId + " не існує"));
        String name = solution.getName();
        try {
            final String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf("."));
            Path filePath = Paths.get(name + extension).normalize();
            solution.getPhoto().add(filePath.toString());

            Files.copy(file.getInputStream(), rootFolder.resolve(filePath));
            solutionDao.save(solution);
            return new PhotoResponseDTO(name);
        } catch (IOException e) {
            log.error("File already exists " + e.getMessage());
            throw new AlreadyExistsException("Такий файл уже існує");
        }
    }

    @Override
    public byte[] getSolutionImage(String name, String format) throws NoSuchFileException {
        if (format.equals("jpeg")){
            try {
                Path destination = Paths.get(rootFolder+"/"+name+".jpg");
                return IOUtils.toByteArray(destination.toUri());
            } catch (IOException e) {
                log.error("No such media in directory " + e.getMessage());
                throw new NoSuchFileException("Media file doesn't seem to exists");
            }
        }else {
            try {
                Path destination = Paths.get(rootFolder+"/"+name+".webp");
                return IOUtils.toByteArray(destination.toUri());
            } catch (IOException e) {
                log.error("No such media in directory " + e.getMessage());
                throw new NoSuchFileException("Media file doesn't seem to exists");
            }
        }
    }
}
