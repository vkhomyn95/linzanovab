package com.backend.linzanova.controller.goods.special;

import com.backend.linzanova.dto.SpecialPageDTO;
import com.backend.linzanova.entity.special.Special;
import com.backend.linzanova.service.special.ISpecialService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/special")
public class SpecialController {

    private ISpecialService specialService;

    @GetMapping("/count")
    public Long totalCount() {
        return specialService.totalCount();
    }

    @GetMapping
    public SpecialPageDTO getAllSpecialOffers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "2") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return specialService.getAllSpecials(pageRequest);
    }

    @GetMapping("/{offerId}")
    public Special getDrop(@PathVariable int offerId) {
        return specialService.getSpecialById(offerId);
    }

    @GetMapping("/name")
    public SpecialPageDTO getSpecialOffersByName(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "2") int size,
                                         @RequestParam String name) {
        Pageable pageRequest = PageRequest.of(page, size);
        return specialService.getSpecialByName(pageRequest, name);
    }

    @PostMapping(value = "/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Special saveSpecialOffer(@RequestBody @Valid Special offer, @PathVariable int userId) {
        offer.setCategory(3);
        return specialService.insertSpecial(offer, userId);
    }

    @PostMapping(value = "/{specialOfferId}/user/{userId}")
    public Special updateDrop(@RequestBody Special offer, @PathVariable int specialOfferId, @PathVariable int userId) {
        offer.setId(specialOfferId);
        return specialService.updateSpecial(offer, userId);
    }

}
