package com.backend.linzanova.dao.drops;


import com.backend.linzanova.entity.drops.Drops;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DropsDao extends JpaRepository<Drops, Integer> {

    @Query("select count(d) from Drops d")
    Long totalCount();

    Page<Drops> findByNameContains(Pageable pageable, String name);
}
