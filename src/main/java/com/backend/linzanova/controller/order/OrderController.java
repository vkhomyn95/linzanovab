package com.backend.linzanova.controller.order;

import com.backend.linzanova.dto.OrderPageDTO;
import com.backend.linzanova.dto.RequestDTO;
import com.backend.linzanova.entity.order.Orders;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.orders.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private IOrderService orderService;
    @Autowired
    private JwtService jwtService;

    @PostMapping
    public Orders saveOrder(@RequestBody RequestDTO requestDTO) {
        final Orders orders = new Orders();
        return orderService.insertOrder(orders, requestDTO);
    }

    @GetMapping
    public OrderPageDTO getAllOrders(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "4") int size) {
        final Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrders(pageable);
    }

    @GetMapping(value = "/user")
    public OrderPageDTO getAllByUserId(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "4") int size,
                                       @RequestHeader(value = "Authorization", required = false) String auth) {
        String jwtToken = auth.substring(7);
        String user = jwtService.extractUsername(jwtToken);
        final Pageable pageable = PageRequest.of(page, size);

        return orderService.getAllOrdersByUserEmail(pageable, user);
    }

    @GetMapping(value = "/{orderId}")
    public Orders getSolution(@PathVariable int orderId) {
        return orderService.getOneOrderById(orderId);
    }

    @PostMapping("/{orderId}")
    public Orders updateSolution(@RequestBody RequestDTO requestDTO,
                                @PathVariable int orderId) {
        final Orders orders = new Orders();
        orders.setId(orderId);
        return orderService.insertOrder(orders, requestDTO);
    }
}
