package com.backend.linzanova.service.special;

import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.special.SpecialDao;
import com.backend.linzanova.dto.SpecialPageDTO;
import com.backend.linzanova.entity.special.Special;
import com.backend.linzanova.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SpecialService implements ISpecialService {

    @Autowired
    private SpecialDao specialDao;

    @Autowired
    private IUserDao userDao;

    @Override
    public Special insertSpecial(Special special, int userId) {
        final User user = userDao.getOne(userId);
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
        return specialDao.findById(id).orElseThrow(() -> new RuntimeException("No special offer with id: " + id));
    }

    @Override
    public Special updateSpecial(Special special, int userId) {
        final User user = userDao.getOne(userId);
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
}
