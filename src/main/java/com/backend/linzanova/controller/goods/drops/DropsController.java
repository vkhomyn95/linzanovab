package com.backend.linzanova.controller.goods.drops;


import com.backend.linzanova.dto.DropPageDTO;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.drops.IDropsService;
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
@RequestMapping("/api/drops")
public class DropsController {

    private IDropsService dropsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/count")
    public Long totalCount() {
        return dropsService.totalCount();
    }

    @GetMapping
    public DropPageDTO getAllDrops(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return dropsService.getAllDrops(pageRequest);
    }

    @GetMapping("/{dropId}")
    public Drops getDrop(@PathVariable int dropId) {
        return dropsService.getDropById(dropId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Drops saveDrops (@RequestHeader(value = "Authorization") String auth,
                            @RequestBody @Valid Drops drops) {
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            return dropsService.insertDrops(drops, userDetails.getUsername());
        }else{
            throw new RuntimeException("No rights");
        }
    }

    @PostMapping(value = "/{dropId}")
    public Drops updateDrop(@RequestHeader(value = "Authorization") String auth,
                            @RequestBody Drops drops,
                            @PathVariable int dropId) {
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            drops.setCategory(0);
            drops.setId(dropId);
            return dropsService.updateDrops(drops, userDetails.getUsername());
        }else{
            throw new RuntimeException("No rights");
        }

    }

    @GetMapping("/name")
    public DropPageDTO getDropsByName(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "2") int size,
                                      @RequestParam String name) {
        Pageable pageRequest = PageRequest.of(page, size);
        return dropsService.getDropsByName(pageRequest, name);
    }

    @GetMapping("/filter")
    public DropPageDTO getDropsFilterOption(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "2") int size,
                                            @RequestParam String colName,
                                            @RequestParam String name){
        final Pageable pageRequest = PageRequest.of(page, size);
        return dropsService.getDropsFilter(pageRequest, colName, name);
    }
}
