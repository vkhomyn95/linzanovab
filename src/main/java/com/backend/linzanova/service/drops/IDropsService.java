package com.backend.linzanova.service.drops;

import com.backend.linzanova.dto.DropPageDTO;
import com.backend.linzanova.entity.drops.Drops;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDropsService {

    Drops insertDrops(Drops drops, String username);

    DropPageDTO getAllDrops(PageRequest pageRequest);

    Drops getDropById(int id);

    Drops updateDrops(Drops drops, String username);

    void removeDrops(int id);

    Long totalCount();

    DropPageDTO getDropsByName(Pageable pageRequest, String name);

    DropPageDTO getDropsFilter(Pageable pageRequest, String colName, String name);

}
