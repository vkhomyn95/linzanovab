package com.backend.linzanova.service.special;

import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.dto.SpecialPageDTO;
import com.backend.linzanova.entity.special.Special;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.NoSuchFileException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ISpecialService {

    Special insertSpecial(Special special, String username);

    SpecialPageDTO getAllSpecials(PageRequest pageRequest);

    Special getSpecialById(int id);

    Special updateSpecial(Special special, String username);

    void removeSpecial(int id);

    Long totalCount();

    SpecialPageDTO getSpecialByName(Pageable pageRequest, String name);

    PhotoResponseDTO insertPhoto(int offerId, MultipartFile file) throws AlreadyExistsException;

    byte[] getOfferImage(String name, String format) throws NoSuchFileException;
}
