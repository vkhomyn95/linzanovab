package com.backend.linzanova.controller.order;

import com.backend.linzanova.dto.OrderPageDTO;
import com.backend.linzanova.dto.RequestDTO;
import com.backend.linzanova.entity.order.Orders;
import com.backend.linzanova.entity.settlement.MethodProperties;
import com.backend.linzanova.entity.settlement.Settlement;
import com.backend.linzanova.entity.settlement.Tracking;
import com.backend.linzanova.service.IUserService;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.orders.IOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @PostMapping("/track/{trackId}")
    public String setOrderTrackingId(@RequestBody int orderNumber,
                                     @PathVariable String trackId) throws JsonProcessingException {
        final Orders oneOrderById = orderService.getOneOrderById(orderNumber);
        String customer = null;
        String email = null;
        String phone = null;
        if (oneOrderById.getFirstName() != null &&
                oneOrderById.getLastName() != null &&
                oneOrderById.getPhone() != null &&
                oneOrderById.getEmail() != null) {
            customer = oneOrderById.getFirstName() + " " + oneOrderById.getLastName();
            email = oneOrderById.getEmail();
            phone = oneOrderById.getPhone();
            System.out.println(customer + email + phone);
        } else {
            customer = oneOrderById.getUser().getFirstName() + " " + oneOrderById.getUser().getLastName();
            email = oneOrderById.getUser().getEmail();
            phone = oneOrderById.getUser().getPhone();
            System.out.println(customer + email + phone);
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.tracktry.com/v1/trackings/post";

        ObjectMapper mapper = new ObjectMapper();
        Tracking tracking = new Tracking(trackId, "meest", "ua", customer, email, phone, Integer.toString(orderNumber), "en");
        System.out.println(tracking);
        String requestJson = mapper.writeValueAsString(tracking);
        System.out.println(requestJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tracktry-Api-Key", "87897b1b-8240-4500-80da-c219900a49e5");

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        System.out.println(answer);
//        return orderService.insertOrder(orders, requestDTO);
        orderService.updateOrderTrackingField(orderNumber, trackId);
        return answer;
    }
}
