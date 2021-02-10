package com.backend.linzanova.service.lens;

import com.backend.linzanova.dto.LensDto;
import com.backend.linzanova.dto.LensPageDTO;
import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.NoSuchFileException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface ILensService {

    LensDto insertLens(Lens lens, String username);

    LensPageDTO getAllLenses(Pageable pageRequest);

    Lens getLens(int id);

    Lens updateLens(Lens lens, String username);

    void removeLens(int id);

    Long totalCount();

    LensPageDTO getLensesByName(Pageable pageable, String name);

    LensPageDTO getLensFilter(Pageable pageable, String colName, String name);

    PhotoResponseDTO insertPhoto(int lensId, MultipartFile file) throws AlreadyExistsException;

    byte[] getLensImage(String name, String format) throws NoSuchFileException;
}
