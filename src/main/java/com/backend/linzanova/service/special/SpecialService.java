package com.backend.linzanova.service.special;

import com.backend.linzanova.config.StorageProperties;
import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.special.SpecialDao;
import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.dto.SpecialPageDTO;
import com.backend.linzanova.entity.lens.Lens;
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

@Service
@Slf4j
public class SpecialService implements ISpecialService {

    @Autowired
    private SpecialDao specialDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private StorageProperties storageProperties;

    private Path rootFolder;

    @PostConstruct
    public void init(){
        try {
            rootFolder = Paths.get(storageProperties.getOffers()).toAbsolutePath().normalize();
            Files.createDirectory(rootFolder);
        } catch (IOException e) {
            log.error("Unable to create folder " + e.getMessage());
        }
    }

    @Override
    public Special insertSpecial(Special special, String username) {
        final User user = userDao.findByEmail(username);
        special.setUser(user);
        return specialDao.save(special);
    }

    @Override
    public SpecialPageDTO getAllSpecials(PageRequest pageRequest) {
        final Page<Special> allSpecialsItems = specialDao.findAll(pageRequest);
        return new SpecialPageDTO(allSpecialsItems.getContent(), allSpecialsItems.getTotalElements(),
                allSpecialsItems.getSize(), allSpecialsItems.isEmpty(), allSpecialsItems.getTotalPages());
    }

    @Override
    public Special getSpecialById(int id) {
        return specialDao.findById(id).orElseThrow(() -> new DoesNotExistException("Товару у категорії пропозиції з id: " + id + " не існує"));
    }

    @Override
    public Special updateSpecial(Special special, String username) {
        final User user = userDao.findByEmail(username);
        special.setCategory(3);
        special.setUser(user);
        return specialDao.save(special);
    }

    @Override
    public void removeSpecial(int id) {
        specialDao.deleteById(id);
    }

    @Override
    public Long totalCount() {
        return specialDao.totalCount();
    }

    @Override
    public SpecialPageDTO getSpecialByName(Pageable pageRequest, String name) {
        final Page<Special> bynameContains = specialDao.findByNameContains(pageRequest, name);
        return new SpecialPageDTO(bynameContains.getContent(), bynameContains.getTotalElements(),
                bynameContains.getSize(), bynameContains.isEmpty(), bynameContains.getTotalPages());
    }

    @Override
    public PhotoResponseDTO insertPhoto(int offerId, MultipartFile file) throws AlreadyExistsException {
        Special special = specialDao.findById(offerId).orElseThrow(() -> new DoesNotExistException("Товару у категорії пропозиції з id: " + offerId + " не існує"));
        String name = special.getName();
        try {
            final String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf("."));
            Path filePath = Paths.get(name + extension).normalize();
            special.getPhoto().add(filePath.toString());

            Files.copy(file.getInputStream(), rootFolder.resolve(filePath));
            specialDao.save(special);
            return new PhotoResponseDTO(name);
        } catch (IOException e) {
            log.error("File already exists " + e.getMessage());
            throw new AlreadyExistsException("Такий файл уже існує");
        }

    }

    @Override
    public byte[] getOfferImage(String name, String format) throws NoSuchFileException {
        try {
            Path destination = Paths.get(rootFolder+"/"+name+".jpg");
            return IOUtils.toByteArray(destination.toUri());
        } catch (IOException e) {
            log.error("No such media in directory " + e.getMessage());
            throw new NoSuchFileException("Media file doesn't seem to exists");
        }

    }
}
