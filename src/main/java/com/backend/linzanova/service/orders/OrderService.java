package com.backend.linzanova.service.orders;

import com.backend.linzanova.dao.drops.DropsDao;
import com.backend.linzanova.dao.lens.LensDao;
import com.backend.linzanova.dao.orders.OrdersDao;
import com.backend.linzanova.dao.special.SpecialDao;
import com.backend.linzanova.dto.*;
import com.backend.linzanova.entity.drops.Drops;
//import com.backend.linzanova.entity.order.Delivery;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.order.Item;
import com.backend.linzanova.entity.order.Orders;
import com.backend.linzanova.entity.special.Special;
import com.backend.linzanova.entity.user.User;
import com.backend.linzanova.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private UserService userService;

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private DropsDao dropsDao;

    @Autowired
    private LensDao lensDao;

    @Autowired
    private SpecialDao specialDao;

    @Autowired DeliveryService deliveryService;

    @Override
    public Orders insertOrder(Orders orders, RequestDTO items) {
        System.out.println(items.getDelivery());
        StringBuilder allProperties = new StringBuilder();
        final List<Lens> lensesList = new ArrayList<>();
        final List<Drops>  list = new ArrayList<>();
        final List<Special> specialList = new ArrayList<>();
        for (ItemDTO r : items.getItems()){
            System.out.println(r.getOffers());
            System.out.println(r.getDrops());
            for (DropDTO i : r.getDrops()) {
                final Drops drops = dropsDao.getOne(i.getDropId());
                list.add(drops);
            }
            if (r.getOffers() != null){
                for (SpecialDTO s : r.getOffers()) {
                    final Special special = specialDao.getOne(s.getOfferId());
                    specialList.add(special);
                }
            }
            if (r.getLenses() != null){
                for (LensOrderDTO l : r.getLenses()) {
                    final Lens lense = lensDao.getOne((l.getLenseId()));
                    System.out.println(lense);

                    lensesList.add(lense);
                    allProperties.append(lense.getName() + " - " + l.getProperties());

                    System.out.println(allProperties);

                }
            }
        }



        List<Item> it = new ArrayList<Item>();

        if (specialList != null && list != null && lensesList != null) {
            it.add(new Item(orders.getId(), lensesList, list, specialList));
        }
        final User user = userService.getUser(items.getUser());
        deliveryService.insertDelivery(items.getDelivery());
        orders.setItems(it);
        orders.setCreatedAt(items.getCreatedAt());
        orders.setTotalSumm(items.getTotalSumm());
        orders.setLastName(items.getLastName());
        orders.setFirstName(items.getFirstName());
        orders.setPatronymic(items.getPatronymic());
        orders.setPhone(items.getPhone());
        orders.setDelivery(items.getDelivery());
        orders.setCustomerComment(items.getCustomerComment());
        orders.setProperties(allProperties.toString());
        orders.setUser(user);
        user.setShoppingQuantity(user.getShoppingQuantity() + 1);

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

        //Add Telegram token (given Token is fake)
        String apiToken = "1436198018:AAGVEmv55adxOP58btioZLCFpRO8M_-YvEg";

        //Add chatId (given chatId is fake)
        String chatId = "-1001466406983";

        StringBuilder text = new StringBuilder();
        text.append("Дата створення замовлення: " + orders.getCreatedAt());
        text.append("%0AВартість замовлення: " + orders.getTotalSumm());
        text.append("%0AІмя клієнта: " + orders.getFirstName());
        text.append("%0AПрізвище клієнта: " + orders.getLastName());
        text.append("%0AТелефон клієнта: " + orders.getPhone());
        if (items.getDelivery() != null){
            text.append("%0A----------------------------%0A");
            text.append("Доставка: %0A" + orders.getDelivery().getCityName() + "%0A" + orders.getDelivery().getWarehouseNumber());
            text.append("%0A----------------------------%0A");
        }
        text.append("%0AКоментар клієнта: " + orders.getCustomerComment());
        text.append("%0AТовар(розчини): ");

        for (int i = 0; i < list.size(); i++) {
            text.append("%0A"+ i + ") " + list.get(i).getName() + " ---------- " + list.get(i).getPrice() + " грн");
        }
        text.append("%0AТовар(вигідні пропозиції): ");
        for (int i = 0; i < specialList.size(); i++) {
            text.append("%0A"+ i + ") " + specialList.get(i).getName() + " ---------- " + specialList.get(i).getPrice() + " грн");
        }
        text.append("%0AТовар(лінзи): ");
        for (int i = 0; i < lensesList.size(); i++) {
            text.append("%0A"+ i + ") " + lensesList.get(i).getName() + " ---------- " + lensesList.get(i).getPrice() + " грн");
        }
        text.append("%0A----------------------------%0A");
        text.append("Інформація авторизованого користувача:");
        text.append("%0AІм'я: " + user.getFirstName());
        text.append("%0AПрізвище: " + user.getLastName());
        text.append("%0AEmail: " + user.getEmail());
        text.append("%0AАдреса доставки: " + user.getLocation());
        text.append("%0AВідділення: №" + user.getNumber());
        text.append("%0AАдреса відділення: " + user.getWarehouse());
        text.append("%0A----------------------------%0A");
        text.append(allProperties.toString().replace("\n", "%0A"));



        urlString = String.format(urlString, apiToken, chatId, text);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ordersDao.save(orders);
    }

    @Override
    public OrderPageDTO getAllOrders(Pageable pageable) {
        final Page<Orders> all = ordersDao.findAll(pageable);
        final List<Orders> content = all.getContent();
        final List<OrderItemsDTO> orders = new ArrayList<>();

        content.stream().map(e -> {
            OrderItemsDTO item = new OrderItemsDTO(e.getId(), e.getTotalSumm(), e.getPriceToPayAfterDelivery(), e.getPriceToPayNow(),
                    e.getCreatedAt(), e.getLastName(), e.getFirstName(), e.getEmail(), e.getPatronymic(), e.getPhone(),
                    e.getCustomerComment(), e.getDelivery(), e.getProperties(), e.getMeestTrackingId(), e.getNovaPoshtaTTN(),
                    e.isDelivered(), e.isCanceled(), e.getUser().getEmail(), e.getUser().getId());
            return orders.add(item);
        }).collect(Collectors.toList());

        System.out.println(content);
        System.out.println();
        return new OrderPageDTO(orders, all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public OrderItemsDTO getOneOrderById(int id) {
        Orders one = ordersDao.getOne(id);
        System.out.println(one.getProperties());
        return new OrderItemsDTO(one.getId(), one.getTotalSumm(), one.getPriceToPayAfterDelivery(), one.getPriceToPayNow(),
                one.getCreatedAt(), one.getLastName(), one.getFirstName(), one.getEmail(), one.getPatronymic(), one.getPhone(),
                one.getCustomerComment(), one.getDelivery(), one.getProperties(), one.getMeestTrackingId(), one.getNovaPoshtaTTN(),
                one.isDelivered(), one.isCanceled(), one.getUser().getEmail(), one.getUser().getId());
    }

    @Override
    public OrderPageDTO getAllOrdersByUserEmail(Pageable pageable, String email) {
        final Page<Orders> all = ordersDao.findAllByUserEmail(pageable, email);
        final List<Orders> content = all.getContent();
        final List<OrderItemsDTO> orders = new ArrayList<>();
        content.stream().map(e -> {
            OrderItemsDTO item = new OrderItemsDTO(e.getId(), e.getTotalSumm(), e.getPriceToPayAfterDelivery(), e.getPriceToPayNow(),
                    e.getCreatedAt(), e.getLastName(), e.getFirstName(), e.getEmail(), e.getPatronymic(), e.getPhone(),
                    e.getCustomerComment(), e.getDelivery(), e.getProperties(), e.getMeestTrackingId(), e.getNovaPoshtaTTN(),
                    e.isDelivered(), e.isCanceled(), e.getUser().getEmail(), e.getUser().getId());
            return orders.add(item);
        }).collect(Collectors.toList());

        System.out.println(content);
        System.out.println();
        return new OrderPageDTO(orders, all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public Orders updateOrder(int id, OrderItemsDTO items) {
        System.out.println(items);
        final Orders order = ordersDao.getOne(id);
        order.setId(id);
        order.setCanceled(items.isCanceled());
        order.setPatronymic(items.getPatronymic());
        order.setMeestTrackingId(items.getMeestTrackingId());
        order.setNovaPoshtaTTN(items.getNovaPoshtaTTN());
        order.setPriceToPayAfterDelivery(items.getPriceToPayAfterDelivery());
        order.setPriceToPayNow(items.getPriceToPayNow());
        order.setTotalSumm(items.getTotalSumm());
        return ordersDao.save(order);
    }

    @Override
    public Long totalCount() {
        return ordersDao.CountOrders();
    }

    @Override
    public Orders updateOrderTrackingField(int orderNumber, String field) {
        final Orders one = ordersDao.getOne(orderNumber);
        one.setMeestTrackingId(field);
        return ordersDao.save(one);
    }

    @Override
    public void removeOrder(int id) {
        ordersDao.deleteById(id);
    }
}
