package com.backend.linzanova.controller.order;

import com.backend.linzanova.dto.OrderItemsDTO;
import com.backend.linzanova.dto.OrderItemsToUpdateDTO;
import com.backend.linzanova.dto.OrderPageDTO;
import com.backend.linzanova.dto.RequestDTO;
import com.backend.linzanova.entity.order.Orders;
import com.backend.linzanova.entity.settlement.Tracking;
import com.backend.linzanova.entity.settlement.UserTracking;
import com.backend.linzanova.entity.user.User;
import com.backend.linzanova.exeption.NoRightsException;
import com.backend.linzanova.service.JwtService;
import com.backend.linzanova.service.UserService;
import com.backend.linzanova.service.orders.IOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private IOrderService orderService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @GetMapping("/count")
    public Long getOrdersCount(){
        log.info("Handling GET /order/count");
        return orderService.totalCount();
    }

    //admin rights only
    @GetMapping
    public OrderPageDTO getAllOrders(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "4") int size,
                                     @RequestHeader(value = "Authorization", required = false) String auth) {
        log.info("Handling GET /order | get all orders");
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            final Pageable pageable = PageRequest.of(page, size);
            return orderService.getAllOrders(pageable);
        }else {
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }

    @GetMapping(value = "/user")
    public OrderPageDTO getAllByUserId(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "4") int size,
                                       @RequestHeader(value = "Authorization") String auth) {
        log.info("Handling GET /order/user");
        String jwtToken = auth.substring(7);
        String user = jwtService.extractUsername(jwtToken);
        final Pageable pageable = PageRequest.of(page, size);

        return orderService.getAllOrdersByUserEmail(pageable, user);
    }

    @GetMapping(value = "/{orderId}")
    public OrderItemsDTO getOrder(@PathVariable int orderId,
                                  @RequestHeader(value = "Authorization") String auth) {
        log.info("Handling GET /order/" + orderId);
        String jwtToken = auth.substring(7);
        String user = jwtService.extractUsername(jwtToken);
        if (user != null){
            return orderService.getOneOrderById(orderId, user);
        }else {
            throw new NoRightsException("No rights for user: " + user);
        }
    }

    @PostMapping("/track/{trackId}")
    public String setOrderTrackingId(@RequestBody int orderNumber,
                                     @PathVariable String trackId,
                                     @RequestHeader(value = "Authorization") String auth) throws JsonProcessingException {
        log.info("Handling POST /order/track/" + trackId + " set tracking");
        OrderItemsDTO oneOrderById = null;
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        if (jwtUser != null){
            oneOrderById = orderService.getOneOrderById(orderNumber, jwtUser);
        }else {
            throw new NoRightsException("No rights for user: " + jwtUser);
        }

        final User user = userService.getUser(oneOrderById.getUserId(), jwtUser);
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
            customer = user.getFirstName() + " " + user.getLastName();
            email = user.getEmail();
            phone = user.getPhone();
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
        orderService.updateOrderTrackingField(orderNumber, trackId);
        return answer;
    }

    @PostMapping("/tracking/realtime")
    public String getOrderTrackingId(@RequestBody String trackingId) throws JsonProcessingException {
        log.info("Handling POST /order/tracking/realtime");
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.tracktry.com/v1/trackings/realtime";

        ObjectMapper mapper = new ObjectMapper();
        UserTracking tracking = new UserTracking(trackingId, "meest", "UA", "ua");
        System.out.println(tracking);
        String requestJson = mapper.writeValueAsString(tracking);
        System.out.println(requestJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tracktry-Api-Key", "87897b1b-8240-4500-80da-c219900a49e5");

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        return answer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Orders saveOrder(@RequestBody RequestDTO requestDTO) {
        log.info("Handling POST /order | new order " + requestDTO);
        final Orders orders = new Orders();
        if (requestDTO.getUser() == 0){
            requestDTO.setUser(2);
        }
        return orderService.insertOrder(orders, requestDTO);
    }

    @PostMapping("/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Orders updateOrder(@RequestHeader(value = "Authorization") String auth,
                              @RequestBody OrderItemsToUpdateDTO requestDTO,
                              @PathVariable int orderId) {
        log.info("Handling update POST /order/" + orderId);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            return orderService.updateOrder(orderId, requestDTO);
        }else{
            throw new NoRightsException("No rights for user: " + jwtUser);
        }
    }
}
