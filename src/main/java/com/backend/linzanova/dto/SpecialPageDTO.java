package com.backend.linzanova.dto;

import com.backend.linzanova.entity.special.Special;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SpecialPageDTO {
    private List<Special> specials;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;
}
