package com.backend.linzanova.service.drops;

import com.backend.linzanova.dao.drops.DropsCommentsDao;
import com.backend.linzanova.dao.drops.DropsDao;
import com.backend.linzanova.dto.comments.CommentDTO;
import com.backend.linzanova.dto.comments.DropCommentsPageDTO;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.drops.DropsComments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Slf4j
public class DropCommentService implements IDropCommentService{

    @Autowired
    private DropsCommentsDao dropsCommentsDao;
    @Autowired
    private DropsDao dropsDao;

    @Override
    public DropCommentsPageDTO getAllDropComments(PageRequest pageRequest) {
        final Page<DropsComments> all = dropsCommentsDao.findAll(pageRequest);
        return new DropCommentsPageDTO(all.getContent(), all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public DropsComments insertComment(CommentDTO comment, int dropId) {
        final Drops drop = dropsDao.getOne(dropId);
        DropsComments dropsComments = new DropsComments();
        dropsComments.setCreatedAt(LocalDateTime.now());
        dropsComments.setText(comment.getBody());
        dropsComments.setUserName(comment.getUser());
        dropsComments.setDrops(drop);

        return dropsCommentsDao.save(dropsComments);
    }
}
