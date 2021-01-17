package com.backend.linzanova.controller.goods.special;

import com.backend.linzanova.dto.SpecialPageDTO;
import com.backend.linzanova.entity.special.Special;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.special.ISpecialService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/special")
public class SpecialController {

    private ISpecialService specialService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

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
    public Special getOffer(@PathVariable int offerId) {
        return specialService.getSpecialById(offerId);
    }

    @GetMapping("/name")
    public SpecialPageDTO getSpecialOffersByName(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "2") int size,
                                         @RequestParam String name) {
        Pageable pageRequest = PageRequest.of(page, size);
        return specialService.getSpecialByName(pageRequest, name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Special saveSpecialOffer(@RequestHeader(value = "Authorization") String auth,
                                    @RequestBody @Valid Special offer) {
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            offer.setCategory(3);
            return specialService.insertSpecial(offer, userDetails.getUsername());
        }else{
            throw new RuntimeException("No rights");
        }
    }

    @PostMapping(value = "/{specialOfferId}")
    public Special updateSpecialOffer(@RequestHeader(value = "Authorization") String auth,
                                      @RequestBody Special offer,
                                      @PathVariable int specialOfferId) {
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            offer.setId(specialOfferId);
            return specialService.updateSpecial(offer, userDetails.getUsername());
        }else{
            throw new RuntimeException("No rights");
        }
    }

}
