package com.backend.linzanova.service;

import com.backend.linzanova.dto.UserOrderDTO;
import com.backend.linzanova.dto.UserPageDTO;
import com.backend.linzanova.dto.UserPasswordRenewDTO;
import com.backend.linzanova.entity.user.User;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    String insertUser(User user);

    UserPageDTO getAllUsers(Pageable pageable);

    User getUser(int id);

    User updateUser(User user);

    Long totalCount();

    UserPageDTO getAllUsersByName(Pageable pageable, String name);

    User findByEmail(String email);

}
