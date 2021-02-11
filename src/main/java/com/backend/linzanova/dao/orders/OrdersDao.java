package com.backend.linzanova.dao.orders;

import com.backend.linzanova.entity.order.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.NamedEntityGraph;

public interface OrdersDao extends JpaRepository<Orders, Integer> {
    @Query("select count(o) from Orders o")
    Long CountOrders();

    @Query("select o from Orders o where o.user.email = :email order by o.id desc")
    Page<Orders> findAllByUserEmail(Pageable pageable, String email);

    @Query("select o from Orders o order by o.id desc")
    Page<Orders> findAll(Pageable pageable);
}
