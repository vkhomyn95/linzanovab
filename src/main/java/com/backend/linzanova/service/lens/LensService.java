package com.backend.linzanova.service.lens;
import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.lens.LensDao;
import com.backend.linzanova.dto.LensDto;
import com.backend.linzanova.dto.LensPageDTO;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.backend.linzanova.dao.lens.LensSpecification.byColumnNameAndValue;


@Service
public class LensService implements ILensService {

    @Autowired
    private LensDao lensDao;

    @Autowired
    private IUserDao userDao;

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
        return lensDao.findById(id).orElseThrow(() -> new RuntimeException("Does not exist lens with id: " + id));
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
}
