package com.backend.linzanova.service.lens;

import com.backend.linzanova.dto.LensDto;
import com.backend.linzanova.dto.LensPageDTO;
import com.backend.linzanova.entity.lens.Lens;
import org.springframework.data.domain.Pageable;


public interface ILensService {

    LensDto insertLens(Lens lens, int userId);

    LensPageDTO getAllLenses(Pageable pageRequest);

    Lens getLens(int id);

    Lens updateLens(Lens lensm, int userId);

    void removeLens(int id);

    Long totalCount();

    LensPageDTO getLensesByName(Pageable pageable, String name);
}
