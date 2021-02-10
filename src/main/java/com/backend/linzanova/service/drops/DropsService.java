package com.backend.linzanova.service.drops;

import com.backend.linzanova.config.StorageProperties;
import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.drops.DropsDao;
import com.backend.linzanova.dto.DropPageDTO;
import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.solution.Solution;
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

import static com.backend.linzanova.dao.drops.DropsSpecification.byColumnNameAndValue;


@Service
@Slf4j
public class DropsService implements IDropsService{

    @Autowired
    private DropsDao dropsDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private StorageProperties storageProperties;

    private Path rootFolder;

    @PostConstruct
    public void init(){
        try {
            rootFolder = Paths.get(storageProperties.getDrops()).toAbsolutePath().normalize();
            Files.createDirectory(rootFolder);
        } catch (IOException e) {
            log.error("Unable to create folder " + e.getMessage());
        }
    }

    public DropsService(DropsDao dropsDao) {
        this.dropsDao = dropsDao;
    }

    @Override
    public Drops insertDrops(Drops drops, String username) {
        final User user = userDao.findByEmail(username);
        drops.setCategory(0);
        drops.setUser(user);
        return dropsDao.save(drops);
    }

    @Override
    public DropPageDTO getAllDrops(PageRequest pageRequest) {
        final Page<Drops> all = dropsDao.findAll(pageRequest);
        return new DropPageDTO(all.getContent(), all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public Drops getDropById(int id) {
        return dropsDao.findById(id).orElseThrow(() -> new DoesNotExistException("Товару у категорії догляд з id: " + id + " не існує"));
    }

    @Override
    public Drops updateDrops(Drops drops, String username) {
        final User user = userDao.findByEmail(username);
        drops.setUser(user);
        return dropsDao.save(drops);
    }

    @Override
    public void removeDrops(int id) {
        dropsDao.deleteById(id);
    }

    @Override
    public Long totalCount() {
        return dropsDao.totalCount();
    }

    @Override
    public DropPageDTO getDropsByName(Pageable pageRequest, String name) {
        final Page<Drops> byNameContains = dropsDao.findByNameContains(pageRequest, name);
        return new DropPageDTO(byNameContains.getContent(), byNameContains.getTotalElements(), byNameContains.getSize(), byNameContains.isEmpty(), byNameContains.getTotalPages());
    }

    @Override
    public DropPageDTO getDropsFilter(Pageable pageable, String colName, String nameValue) {
        final Page<Drops> byFIlter = dropsDao.findAll(byColumnNameAndValue(colName, nameValue), pageable);
        return new DropPageDTO(byFIlter.getContent(), byFIlter.getTotalElements(), byFIlter.getSize(), byFIlter.isEmpty(), byFIlter.getTotalPages());
    }

    @Override
    public PhotoResponseDTO insertPhoto(int dropId, MultipartFile file) throws AlreadyExistsException {
        Drops drop = dropsDao.findById(dropId).orElseThrow(() -> new DoesNotExistException("Товару у категорії догляд з id: " + dropId + " не існує"));
        String name = drop.getName();
        try {
            final String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf("."));
            Path filePath = Paths.get(name + extension).normalize();
            drop.getPhoto().add(filePath.toString());

            Files.copy(file.getInputStream(), rootFolder.resolve(filePath));
            dropsDao.save(drop);
            return new PhotoResponseDTO(name);
        } catch (IOException e) {
            log.error("File already exists " + e.getMessage());
            throw new AlreadyExistsException("Такий файл уже існує");
        }
    }

    @Override
    public byte[] getDropImage(String name, String format) throws NoSuchFileException {
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
