package com.backend.linzanova.controller.goods.drops;

import com.backend.linzanova.dto.comments.CommentDTO;
import com.backend.linzanova.dto.comments.DropCommentsPageDTO;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.drops.DropsComments;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.drops.IDropCommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/drop/comments")
public class DropsCommentsController {

    private IDropCommentService dropCommentService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService uDetails;

    @GetMapping
    public DropCommentsPageDTO getAllComments(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "4") int size) {
        log.info("Handling GET /drop/comments");
        PageRequest pageRequest = PageRequest.of(page, size);
        return dropCommentService.getAllDropComments(pageRequest);
    }

    @PostMapping("/{dropId}")
    public CommentDTO saveComment(@PathVariable int dropId,
                                  @RequestBody @Valid CommentDTO comment){

        final DropsComments dropsComments = dropCommentService.insertComment(comment, dropId);
        return new CommentDTO(dropsComments.getText(), dropsComments.getUserName(), dropId);
    }

}
