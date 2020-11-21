package com.backend.linzanova.dao;

import com.backend.linzanova.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUserDao extends JpaRepository<User, Integer> {

    @Query("SELECT COUNT(u) FROM User u")
    Long countUsers();

    Page<User> findByFirstNameContains(Pageable pageable, String name);

    User findByEmail(String email);
}
