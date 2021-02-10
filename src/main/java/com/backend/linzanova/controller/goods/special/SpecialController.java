package com.backend.linzanova.controller.goods.special;

import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.dto.SpecialPageDTO;
import com.backend.linzanova.entity.special.Special;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.NoRightsException;
import com.backend.linzanova.exeption.NoSuchFileException;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.special.ISpecialService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
@Slf4j
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
        log.info("Handling GET /special/count");
        return specialService.totalCount();
    }

    @GetMapping
    public SpecialPageDTO getAllSpecialOffers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "2") int size) {
        log.info("Handling GET /special");
        PageRequest pageRequest = PageRequest.of(page, size);
        return specialService.getAllSpecials(pageRequest);
    }

    @GetMapping("/{offerId}")
    public Special getOffer(@PathVariable int offerId) {
        log.info("Handling GET /special/"+ offerId);
        return specialService.getSpecialById(offerId);
    }

    @GetMapping("/name")
    public SpecialPageDTO getSpecialOffersByName(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "2") int size,
                                         @RequestParam String name) {
        log.info("Handling GET /special/name with name: " + name);
        Pageable pageRequest = PageRequest.of(page, size);
        return specialService.getSpecialByName(pageRequest, name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Special saveSpecialOffer(@RequestHeader(value = "Authorization") String auth,
                                    @RequestBody @Valid Special offer) {
        log.info("Handling GET /special save request with object: " + offer);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            offer.setCategory(3);
            return specialService.insertSpecial(offer, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @PostMapping(value = "/{specialOfferId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Special updateSpecialOffer(@RequestHeader(value = "Authorization") String auth,
                                      @RequestBody Special offer,
                                      @PathVariable int specialOfferId) {
        log.info("Handling GET /special update request with object: " + offer);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            offer.setId(specialOfferId);
            return specialService.updateSpecial(offer, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @PostMapping(value = "/{offerId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PhotoResponseDTO uploadOfferPhoto(@PathVariable int offerId, MultipartFile file) throws AlreadyExistsException {
        log.info("Handling GET /special photo upload request with object");
        return specialService.insertPhoto(offerId, file);
    }

    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> getOfferImage(@RequestParam String name,
                                                @RequestHeader(value = "format") String format) throws IOException, NoSuchFileException {
        log.info("Handling GET /special/image/" + name);

    return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(specialService.getOfferImage(name, format));
    }

}
