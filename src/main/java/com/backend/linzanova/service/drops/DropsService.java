package com.backend.linzanova.service.drops;

import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dao.drops.DropsDao;
import com.backend.linzanova.dto.DropPageDTO;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropsService implements IDropsService{

    @Autowired
    private DropsDao dropsDao;

    @Autowired
    private IUserDao userDao;

    public DropsService(DropsDao dropsDao) {
        this.dropsDao = dropsDao;
    }

    @Override
    public Drops insertDrops(Drops drops, int userId) {
        final User user = userDao.getOne(userId);
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
        return dropsDao.findById(id).orElseThrow(() -> new RuntimeException("No drops found with current id"));
    }

    @Override
    public Drops updateDrops(Drops drops, int userId) {
        final User user = userDao.getOne(userId);
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
}
