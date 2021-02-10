package com.backend.linzanova.controller.goods.drops;


import com.backend.linzanova.dto.DropPageDTO;
import com.backend.linzanova.dto.PhotoResponseDTO;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.exeption.AlreadyExistsException;
import com.backend.linzanova.exeption.NoRightsException;
import com.backend.linzanova.exeption.NoSuchFileException;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.drops.IDropsService;
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

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/drops")
public class DropsController {

    private IDropsService dropsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/count")
    public Long totalCount() {
        log.info("Handling GET /drops/count");
        return dropsService.totalCount();
    }

    @GetMapping
    public DropPageDTO getAllDrops(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        log.info("Handling GET /drops");
        PageRequest pageRequest = PageRequest.of(page, size);
        return dropsService.getAllDrops(pageRequest);
    }

    @GetMapping("/{dropId}")
    public Drops getDrop(@PathVariable int dropId) {
        log.info("Handling GET /drops/" + dropId);
        return dropsService.getDropById(dropId);
    }

    @GetMapping("/name")
    public DropPageDTO getDropsByName(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "2") int size,
                                      @RequestParam String name) {
        log.info("Handling GET /drops/name");
        Pageable pageRequest = PageRequest.of(page, size);
        return dropsService.getDropsByName(pageRequest, name);
    }

    @GetMapping("/filter")
    public DropPageDTO getDropsFilterOption(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "2") int size,
                                            @RequestParam String colName,
                                            @RequestParam String name){
        log.info("Handling GET /drops/filter");
        final Pageable pageRequest = PageRequest.of(page, size);
        return dropsService.getDropsFilter(pageRequest, colName, name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Drops saveDrops (@RequestHeader(value = "Authorization") String auth,
                            @RequestBody @Valid Drops drops) {
        log.info("Handling POST /drops" + drops);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            return dropsService.insertDrops(drops, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @PostMapping(value = "/{dropId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Drops updateDrop(@RequestHeader(value = "Authorization") String auth,
                            @RequestBody Drops drops,
                            @PathVariable int dropId) {
        log.info("Handling POST /drops/" + dropId);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            drops.setCategory(0);
            drops.setId(dropId);
            return dropsService.updateDrops(drops, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @PostMapping(value = "/{dropId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PhotoResponseDTO uploadDropPhoto(@PathVariable int dropId, MultipartFile file) throws AlreadyExistsException {
        log.info("Handling POST /drops/" + dropId + "/photo");
        return dropsService.insertPhoto(dropId, file);
    }

    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> getDropImage(@RequestParam String name,
                                               @RequestHeader(value = "format") String format) throws IOException, NoSuchFileException {
        log.info("Handling GET /drops/image/" + name);

        if (format.equals("jpeg")){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(dropsService.getDropImage(name, format));
        }else {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("image/webp"))
                    .body(dropsService.getDropImage(name, format));
        }
    }
}
