package com.backend.linzanova.service.drops;

import com.backend.linzanova.dto.comments.CommentDTO;
import com.backend.linzanova.dto.comments.DropCommentsPageDTO;
import com.backend.linzanova.entity.drops.DropsComments;
import org.springframework.data.domain.PageRequest;

public interface IDropCommentService {
    DropCommentsPageDTO getAllDropComments(PageRequest pageRequest);

    DropsComments insertComment(CommentDTO comment, int dropId);
}
