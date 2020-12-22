package com.backend.linzanova.dao.orders;

import com.backend.linzanova.entity.order.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDao extends JpaRepository<Delivery, Integer> {
}
