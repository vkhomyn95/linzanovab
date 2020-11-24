package com.backend.linzanova.service.orders;

import com.backend.linzanova.dao.drops.DropsDao;
import com.backend.linzanova.dao.orders.OrdersDao;
import com.backend.linzanova.dao.special.SpecialDao;
import com.backend.linzanova.dto.*;
import com.backend.linzanova.entity.drops.Drops;
//import com.backend.linzanova.entity.order.Delivery;
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
    private SpecialDao specialDao;

    @Override
    public Orders insertOrder(Orders orders, RequestDTO items) {
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
        }



        List<Item> it = new ArrayList<Item>();

        if (specialList != null && list != null) {
            it.add(new Item(orders.getId(), list, specialList));
        }
        final User user = userService.getUser(items.getUser());

        orders.setItems(it);
        orders.setCreatedAt(items.getCreatedAt());
        orders.setTotalSumm(items.getTotalSumm());
        orders.setLastName(items.getLastName());
        orders.setFirstName(items.getFirstName());
        orders.setPatronymic(items.getPatronymic());
        orders.setPhone(items.getPhone());
        orders.setCustomerComment(items.getCustomerComment());
        orders.setUser(user);

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
        text.append("%0AКоментар клієнта: " + orders.getCustomerComment());
        text.append("%0AТовар(розчини): ");

        for (int i = 0; i < list.size(); i++) {
            text.append("%0A"+ i + ") " + list.get(i).getName() + " ---------- " + list.get(i).getPrice() + " грн");
        }
        text.append("%0AТовар(вигідні пропозиції): ");
        for (int i = 0; i < specialList.size(); i++) {
            text.append("%0A"+ i + ") " + specialList.get(i).getName() + " ---------- " + specialList.get(i).getPrice() + " грн");
        }
        text.append("%0A----------------------------%0A");
        text.append("Інформація авторизованого користувача:");
        text.append("%0AІм'я: " + user.getFirstName());
        text.append("%0AПрізвище: " + user.getLastName());
        text.append("%0AEmail: " + user.getEmail());
        text.append("%0AАдреса доставки: " + user.getLocation());
        text.append("%0AВідділення: №" + user.getNumber());
        text.append("%0AАдреса відділення: " + user.getWarehouse());



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
        System.out.println(all.getContent());
        return new OrderPageDTO(all.getContent(), all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public Orders getOneOrderById(int id) {
        return ordersDao.getOne(id);
    }

    @Override
    public OrderPageDTO getAllOrdersByUserEmail(Pageable pageable, String email) {
        final Page<Orders> all = ordersDao.findAllByUserEmail(pageable, email);
        System.out.println(all);
        return new OrderPageDTO(all.getContent(), all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public Orders updateOrder(Orders orders, RequestDTO items) {
        final List<Drops>  list = new ArrayList<>();
        final List<Special> specialList = new ArrayList<>();
        for (ItemDTO r : items.getItems()){
            for (DropDTO i : r.getDrops()) {
                final Drops drops = dropsDao.getOne(i.getDropId());
                list.add(drops);
            }
            for (SpecialDTO s : r.getOffers()) {
                final Special special = specialDao.getOne(s.getOfferId());
                specialList.add(special);
            }
        }
        List<Item> it = new ArrayList<Item>();
        it.add(new Item(orders.getId(), list, specialList));

        orders.setItems(it);
        orders.setCreatedAt(items.getCreatedAt());
        orders.setTotalSumm(items.getTotalSumm());
        orders.setLastName(items.getLastName());
        orders.setFirstName(items.getFirstName());
        orders.setPatronymic(items.getPatronymic());
        orders.setPhone(items.getPhone());
        orders.setCustomerComment(items.getCustomerComment());
        System.out.println(list);
        System.out.println(orders);
        return ordersDao.save(orders);
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
