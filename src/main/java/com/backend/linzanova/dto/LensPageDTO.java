package com.backend.linzanova.dto;

import com.backend.linzanova.entity.lens.Lens;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LensPageDTO {
    private List<Lens> lenses;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;
}
