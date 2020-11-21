package com.backend.linzanova.dao.orders;

import com.backend.linzanova.entity.order.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrdersDao extends JpaRepository<Orders, Integer> {

    @Query("select o from Orders o where o.user.email = :email")
    Page<Orders> findAllByUserEmail(Pageable pageable, String email);

}
