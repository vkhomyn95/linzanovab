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


@Service
public class LensService implements ILensService {

    @Autowired
    private LensDao lensDao;

    @Autowired
    private IUserDao userDao;

    @Override
    public LensDto insertLens(Lens lens, int userId) {
        final User user = userDao.getOne(userId);
        lens.setUser(user);
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
    public Lens updateLens(Lens lens, int userId) {
        final User user = userDao.getOne(userId);
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
}
