package com.backend.linzanova.dto.comments;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDTO {
    private String body;
    private String user;
    private int postId;
}
