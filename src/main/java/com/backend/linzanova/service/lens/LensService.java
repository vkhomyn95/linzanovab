package com.backend.linzanova.service.lens;
import com.backend.linzanova.config.StorageProperties;
import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.lens.LensDao;
import com.backend.linzanova.dto.LensDto;
import com.backend.linzanova.dto.LensPageDTO;
import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.user.User;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.DoesNotExistException;
import com.backend.linzanova.exeption.NoSuchFileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.backend.linzanova.dao.lens.LensSpecification.byColumnNameAndValue;


@Service
@Slf4j
public class LensService implements ILensService {

    @Autowired
    private LensDao lensDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private StorageProperties storageProperties;

    private Path rootFolder;

    @PostConstruct
    public void init(){
        try {
            rootFolder = Paths.get(storageProperties.getLens()).toAbsolutePath().normalize();
            Files.createDirectory(rootFolder);
        } catch (IOException e) {
            log.error("Unable to create folder " + e.getMessage());
        }
    }

    @Override
    public LensDto insertLens(Lens lens, String username) {
        final User user = userDao.findByEmail(username);
        lens.setUser(user);
        if (lens.getDefaultBC() == 0){
            lens.setHasDefaultBC(false);
        }else {
            lens.setHasDefaultBC(true);
        }
        lens = lensDao.save(lens);
        return new LensDto(lens.getId(), lens.getName(), lens.getPrice(), user.getFirstName());
    }

    @Override
    public LensPageDTO getAllLenses(Pageable pageRequest) {
        final Page<Lens> all = lensDao.findAll(pageRequest);
        return new LensPageDTO(all.getContent(), all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public Lens getLens(int id) {
        return lensDao.findById(id).orElseThrow(() -> new DoesNotExistException("Товару у категорії лінзи з id: " + id + " не існує"));
    }

    @Override
    public Lens updateLens(Lens lens, String username) {
        final User user = userDao.findByEmail(username);
        lens.setCategory(1);
        lens.setUser(user);
        return lensDao.save(lens);
    }

    @Override
    public void removeLens(int id) {
        lensDao.deleteById(id);
    }

    @Override
    public Long totalCount() {
        return lensDao.countLenses();
    }

    @Override
    public LensPageDTO getLensesByName(Pageable pageable, String name) {
        final Page<Lens> byNameContains = lensDao.findByNameContains(pageable, name);
        return new LensPageDTO(byNameContains.getContent(), byNameContains.getTotalElements(), byNameContains.getSize(), byNameContains.isEmpty(), byNameContains.getTotalPages());
    }

    @Override
    public LensPageDTO getLensFilter(Pageable pageable, String colName, String nameValue) {
        final Page<Lens> byFIlter = lensDao.findAll(byColumnNameAndValue(colName, nameValue), pageable);

        return new LensPageDTO(byFIlter.getContent(), byFIlter.getTotalElements(), byFIlter.getSize(), byFIlter.isEmpty(), byFIlter.getTotalPages());
    }

    @Override
    public PhotoResponseDTO insertPhoto(int lensId, MultipartFile file) throws AlreadyExistsException {
        Lens lens = lensDao.findById(lensId).orElseThrow(() -> new DoesNotExistException("Товару у категорії лінзи з id: " + lensId + " не існує"));
        String name = lens.getName();
        try {
            final String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf("."));
            Path filePath = Paths.get(name + extension).normalize();
            lens.getPhoto().add(filePath.toString());
            Files.copy(file.getInputStream(), rootFolder.resolve(filePath));
            lensDao.save(lens);
            return new PhotoResponseDTO(name);
        } catch (IOException e) {
            log.error("File already exists " + e.getMessage());
            throw new AlreadyExistsException("Такий файл уже існує");
        }


    }

    @Override
    public byte[] getLensImage(String name, String format) throws NoSuchFileException {
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
