package com.backend.linzanova.dao.drops;


import com.backend.linzanova.entity.drops.Drops;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DropsDao extends JpaRepository<Drops, Integer>, JpaSpecificationExecutor<Drops> {

    @Query("select count(d) from Drops d")
    Long totalCount();

    @EntityGraph(value = "Drops.noJoins", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<Drops> findAll (Pageable pageable);

    Page<Drops> findByNameContains(Pageable pageable, String name);
}
