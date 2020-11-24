package com.backend.linzanova.service.orders;

import com.backend.linzanova.dto.OrderPageDTO;
import com.backend.linzanova.dto.RequestDTO;
import com.backend.linzanova.entity.order.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


public interface IOrderService {

    Orders insertOrder(Orders orders, RequestDTO items);

    OrderPageDTO getAllOrders(Pageable pageable);

    Orders getOneOrderById(int id);

    OrderPageDTO getAllOrdersByUserEmail(Pageable pageable, String email);

    Orders updateOrder(Orders orders, RequestDTO items);

    Orders updateOrderTrackingField(int orderNumber, String field);

    void removeOrder(int id);

}
