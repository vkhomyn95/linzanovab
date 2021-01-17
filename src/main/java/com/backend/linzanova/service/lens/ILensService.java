package com.backend.linzanova.service.lens;

import com.backend.linzanova.dto.LensDto;
import com.backend.linzanova.dto.LensPageDTO;
import com.backend.linzanova.entity.lens.Lens;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ILensService {

    LensDto insertLens(Lens lens, String username);

    LensPageDTO getAllLenses(Pageable pageRequest);

    Lens getLens(int id);

    Lens updateLens(Lens lens, String username);

    void removeLens(int id);

    Long totalCount();

    LensPageDTO getLensesByName(Pageable pageable, String name);

    LensPageDTO getLensFilter(Pageable pageable, String colName, String name);
}
