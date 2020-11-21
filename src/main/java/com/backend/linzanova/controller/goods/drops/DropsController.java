package com.backend.linzanova.controller.goods.drops;


import com.backend.linzanova.dto.DropPageDTO;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.service.drops.IDropsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/drops")
public class DropsController {

    private IDropsService dropsService;

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

    @PostMapping(value = "/user/{user}")
    @ResponseStatus(HttpStatus.CREATED)
    public Drops saveDrops (@RequestBody @Valid Drops drops, @PathVariable int user) {
        drops.setCategory(0);
        return dropsService.insertDrops(drops, user);
    }

    @PostMapping(value = "/{dropId}/user/{userId}")
    public Drops updateDrop(@RequestBody Drops drops, @PathVariable int dropId, @PathVariable int userId) {
        drops.setId(dropId);
        return dropsService.updateDrops(drops, userId);
    }

    @GetMapping("/name")
    public DropPageDTO getDropsByName(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "2") int size,
                                      @RequestParam String name) {
        Pageable pageRequest = PageRequest.of(page, size);
        return dropsService.getDropsByName(pageRequest, name);
    }
}
