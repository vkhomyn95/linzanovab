package com.backend.linzanova.dao.drops;

import com.backend.linzanova.entity.drops.DropsComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropsCommentsDao extends JpaRepository<DropsComments, Integer> {
}
