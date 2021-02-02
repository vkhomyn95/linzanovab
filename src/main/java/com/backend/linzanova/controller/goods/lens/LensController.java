package com.backend.linzanova.controller.goods.lens;

import com.backend.linzanova.dto.LensDto;
import com.backend.linzanova.dto.LensPageDTO;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.exeption.NoRightsException;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.lens.ILensService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class LensController {

    private ILensService lensService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public LensController(ILensService lensService) {
        this.lensService = lensService;
    }

    @GetMapping(value = "/lenses/count")
    public Long getLensesCount() {
        log.info("Handling GET /lenses/count");
        return lensService.totalCount();
    }

    @GetMapping(value = "/lenses")
    public LensPageDTO getAllLenses(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "2") int size) {
        log.info("Handling GET /lenses");
        final Pageable pageRequest = PageRequest.of(page, size);
        return lensService.getAllLenses(pageRequest);
    }

    @GetMapping(value = "/lenses/name")
    public LensPageDTO getLensesByName(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "2") int size,
                                       @RequestParam String name) {
        log.info("Handling GET /lenses/name");
        final Pageable pageRequest = PageRequest.of(page, size);
        return lensService.getLensesByName(pageRequest, name);
    }

    @GetMapping(value = "/lenses/filter")
    public LensPageDTO getLensFilterOption(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "2") int size,
                                          @RequestParam String colName,
                                          @RequestParam String name) {
        log.info("Handling GET /lenses/filter");
        final Pageable pageRequest = PageRequest.of(page, size);
        return lensService.getLensFilter(pageRequest, colName, name);
    }

    @GetMapping(value = "/lenses/{lensId}")
    public Lens getLens(@PathVariable int lensId) {
        log.info("Handling GET /lenses/" + lensId);
        return lensService.getLens(lensId);
    }

    @PostMapping(value = "/lens")
    @ResponseStatus(HttpStatus.CREATED)
    public LensDto saveLens(@RequestHeader(value = "Authorization") String auth,
                            @RequestBody @Valid Lens lens) {
        log.info("Handling POST /lens with object " + lens);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            lens.setCategory(1);
            return lensService.insertLens(lens, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @PostMapping(value = "/lenses/{lensId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Lens updateLens(@RequestHeader(value = "Authorization") String auth,
                           @RequestBody Lens lens,
                           @PathVariable int lensId) {
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            lens.setId(lensId);
            return lensService.updateLens(lens, userDetails.getUsername());
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }
}
