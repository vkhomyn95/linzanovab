package com.backend.linzanova.dao.special;

import com.backend.linzanova.entity.special.Special;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SpecialDao extends JpaRepository<Special, Integer> {
    @Query("select count(s) from Special s")
    Long totalCount();

    Page<Special> findByNameContains(Pageable pageable, String name);
}
