package com.backend.linzanova.service.orders;

import com.backend.linzanova.dto.OrderItemsDTO;
import com.backend.linzanova.dto.OrderPageDTO;
import com.backend.linzanova.dto.RequestDTO;
import com.backend.linzanova.entity.order.Orders;
import org.springframework.data.domain.Pageable;


public interface IOrderService {

    Orders insertOrder(Orders orders, RequestDTO items);

    OrderPageDTO getAllOrders(Pageable pageable);

    OrderItemsDTO getOneOrderById(int id);

    OrderPageDTO getAllOrdersByUserEmail(Pageable pageable, String email);

    Orders updateOrder(int id, OrderItemsDTO items);

    Orders updateOrderTrackingField(int orderNumber, String field);

    Long totalCount();

    void removeOrder(int id);

}
