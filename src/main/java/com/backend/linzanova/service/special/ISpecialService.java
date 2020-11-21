package com.backend.linzanova.service.special;

import com.backend.linzanova.dto.SpecialPageDTO;
import com.backend.linzanova.entity.special.Special;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface ISpecialService {

    Special insertSpecial(Special special, int userId);

    SpecialPageDTO getAllSpecials(PageRequest pageRequest);

    Special getSpecialById(int id);

    Special updateSpecial(Special special, int userId);

    void removeSpecial(int id);

    Long totalCount();

    SpecialPageDTO getSpecialByName(Pageable pageRequest, String name);


}
