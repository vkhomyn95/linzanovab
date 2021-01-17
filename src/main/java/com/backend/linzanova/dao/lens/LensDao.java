package com.backend.linzanova.dao.lens;

import com.backend.linzanova.entity.lens.Lens;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface LensDao extends JpaRepository<Lens, Integer>, JpaSpecificationExecutor<Lens> {

    @Query("SELECT COUNT(l) FROM Lens l")
    Long countLenses();

    Page<Lens> findByNameContains(Pageable pageable, String name);

}
