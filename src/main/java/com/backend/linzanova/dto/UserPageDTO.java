package com.backend.linzanova.dto;

import com.backend.linzanova.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserPageDTO {
    private List<User> users;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;
}
