package com.backend.linzanova.dto.comments;


import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.drops.DropsComments;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DropCommentsPageDTO {
    private List<DropsComments> drops;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;
}
