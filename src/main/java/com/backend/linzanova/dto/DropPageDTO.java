package com.backend.linzanova.dto;

import com.backend.linzanova.entity.drops.Drops;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DropPageDTO {
    private List<Drops> drops;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;

}
