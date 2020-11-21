package com.backend.linzanova.controller.goods.lens;

import com.backend.linzanova.dto.LensDto;
import com.backend.linzanova.dto.LensPageDTO;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.service.lens.ILensService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class LensController {

    private ILensService lensService;

    @Autowired
    public LensController(ILensService lensService) {
        this.lensService = lensService;
    }

    @GetMapping(value = "/api/lenses/count")
    public Long getLensesCount() {
        return lensService.totalCount();
    }

    @GetMapping(value = "/api/lenses")
    public LensPageDTO getAllLenses(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "2") int size) {
        final Pageable pageRequest = PageRequest.of(page, size);
        return lensService.getAllLenses(pageRequest);
    }

    @GetMapping(value = "/api/lenses/name")
    public LensPageDTO getLensesByName(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "2") int size,
                                       @RequestParam String name) {
        final Pageable pageRequest = PageRequest.of(page, size);
        return lensService.getLensesByName(pageRequest, name);
    }

    @GetMapping(value = "/api/lenses/{lensId}")
    public Lens getLens(@PathVariable int lensId) {
        return lensService.getLens(lensId);
    }

    @PostMapping(value = "/api/lens/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LensDto saveLens(@RequestBody @Valid Lens lens, @PathVariable int userId) {
        log.info("Handling POST /lens with object " + lens);
        lens.setCategory(1);
        return lensService.insertLens(lens, userId);
    }

    @PostMapping(value = "/api/lenses/{lensId}/user/{userId}")
    public Lens updateLens(@PathVariable int lensId, @PathVariable int userId, @RequestBody Lens lens) {
        lens.setId(lensId);
        return lensService.updateLens(lens, userId);
    }
}
