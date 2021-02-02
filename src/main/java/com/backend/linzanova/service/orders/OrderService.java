package com.backend.linzanova.service.orders;

import com.backend.linzanova.dao.drops.DropsDao;
import com.backend.linzanova.dao.lens.LensDao;
import com.backend.linzanova.dao.orders.OrdersDao;
import com.backend.linzanova.dao.solution.SolutionDao;
import com.backend.linzanova.dao.special.SpecialDao;
import com.backend.linzanova.dto.*;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.order.Item;
import com.backend.linzanova.entity.order.Orders;
import com.backend.linzanova.entity.solution.Solution;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private SolutionDao solutionDao;

    @Autowired DeliveryService deliveryService;

    @Override
    public Orders insertOrder(Orders orders, RequestDTO items) {
        StringBuilder allProperties = new StringBuilder();
        StringBuilder goodsList = new StringBuilder();
        int totalSumm = 0; int priceToPayNow = 0; int priceToPayAfterDelivery = 0;
        final List<Lens> lensesList = new ArrayList<>();
        final List<Drops>  dropsList = new ArrayList<>();
        final List<Special> specialList = new ArrayList<>();
        final List<Solution> solutionList = new ArrayList<>();
        for (ItemDTO r : items.getItems()){
            if (r.getDrops() != null && r.getDrops().size() > 0) {
                goodsList.append("%0A*Товар(догляд):* ");
                for (DropDTO i : r.getDrops()) {
                    final Drops drops = dropsDao.getOne(i.getDropId());
                    dropsList.add(drops);
                    allProperties.append(drops.getName() + " - " + i.getProperties());
                    totalSumm += drops.getPrice();
                    priceToPayAfterDelivery += drops.getPrice();

                }
                for (int it = 0; it < dropsList.size(); it++) {
                    goodsList.append("%0A"+ it + ") " + dropsList.get(it).getName() + " ---------- " + dropsList.get(it).getPrice() + " грн");
                }
            }
            if (r.getOffers() != null && r.getOffers().size() > 0){
                goodsList.append("%0A*Товар(вигідні пропозиції):* ");
                for (SpecialDTO s : r.getOffers()) {
                    final Special special = specialDao.getOne(s.getOfferId());
                    specialList.add(special);
                    allProperties.append(special.getName() + " - " + s.getProperties());
                    totalSumm += special.getPrice();
                    priceToPayNow += special.getPrice();
                }
                for (int i = 0; i < specialList.size(); i++) {
                    goodsList.append("%0A"+ i + ") " + specialList.get(i).getName() + " ---------- " + specialList.get(i).getPrice() + " грн");
                }
            }
            if (r.getLenses() != null && r.getLenses().size() > 0){
                goodsList.append("%0A*Товар(лінзи):* ");
                for (LensOrderDTO l : r.getLenses()) {
                    final Lens lense = lensDao.getOne((l.getLenseId()));
                    lensesList.add(lense);
                    allProperties.append(lense.getName() + " - " + l.getProperties());
                    totalSumm += lense.getPrice();
                    priceToPayNow += lense.getPrice();
                }
                for (int i = 0; i < lensesList.size(); i++) {
                    goodsList.append("%0A"+ i + ") " + lensesList.get(i).getName() + " ---------- " + lensesList.get(i).getPrice() + " грн");
                }
            }
            if (r.getSolutions() != null && r.getSolutions().size() > 0){
                goodsList.append("%0A*Товар(розчини):* ");
                for (SolutionOrderDTO s: r.getSolutions()) {
                    final Solution solution = solutionDao.getOne(s.getSolutionId());
                    solutionList.add(solution);
                    allProperties.append(solution.getName() + "-" + s.getProperties());
                    totalSumm += solution.getPrice();
                    priceToPayAfterDelivery += solution.getPrice();

                }
                for (int i = 0; i < solutionList.size(); i++) {
                    goodsList.append("%0A"+ i + ") " + solutionList.get(i).getName() + " ---------- " + solutionList.get(i).getPrice() + " грн");
                }
            }
        }

        List<Item> it = new ArrayList<Item>();

        it.add(new Item(orders.getId(), lensesList, dropsList, specialList, solutionList));

        final User user = userService.getUser(items.getUser());
        deliveryService.insertDelivery(items.getDelivery());
        orders.setItems(it);
        orders.setCreatedAt(LocalDateTime.now());
        orders.setTotalSumm(totalSumm);
        if (items.getDelivery().getPaymentType().equals("byCardPay")){
            orders.setPriceToPayNow(totalSumm);
            orders.setPriceToPayAfterDelivery(0);
        }else {
            orders.setPriceToPayNow(priceToPayNow);
            orders.setPriceToPayAfterDelivery(priceToPayAfterDelivery);
        }

        orders.setLastName(items.getLastName());
        orders.setFirstName(items.getFirstName());
        orders.setPatronymic(items.getPatronymic());
        orders.setEmail(items.getEmail());
        orders.setPhone(items.getPhone());
        orders.setDelivery(items.getDelivery());
        orders.setCustomerComment(items.getCustomerComment());
        orders.setProperties(allProperties.toString());
        orders.setUser(user);
        user.setShoppingQuantity(user.getShoppingQuantity() + 1);

        StringBuilder text = new StringBuilder();
        text.append("Дата створення замовлення: " + orders.getCreatedAt());
        text.append("%0AВартість замовлення: " + "`" + totalSumm + "грн`");
        if (items.getDelivery().getPaymentType().equals("byCardPay")){
            text.append("%0AТип оплати: " + "_Картою_");
            text.append("%0AОплатити зараз: `" + totalSumm + " грн`");
            text.append("%0AОплатити на пошті: ```0грн```");
        }else {
            text.append("%0AТип оплати: " + "_Оплата на пошті_");
            text.append("%0AОплатити зараз: `" + priceToPayNow + " грн`");
            text.append("%0AОплатити на пошті: `" + priceToPayAfterDelivery + " грн`");
        }
        text.append("%0AІмя клієнта: " + orders.getLastName() + " " + orders.getFirstName() + " " + orders.getPatronymic());
        text.append("%0AТелефон: " + orders.getPhone());
        if (items.getCustomerComment() != null) {
            text.append("%0AКоментар клієнта: " + orders.getCustomerComment());
        }
        if (items.getDelivery() != null){
            text.append("%0A--------------------------%0A");
            text.append("Тип доставки: " + items.getDelivery().getDeliveryType());
            if (items.getDelivery().getDeliveryType().equals("ukr")){
                text.append("%0AПоштовий індекс: " + items.getDelivery().getPostIndex());
            }else {
                text.append("%0AДоставка: %0A_" + orders.getDelivery().getCityName() + "%0A" + orders.getDelivery().getDescription() + "%0AНомер відділення: №" + orders.getDelivery().getWarehouseNumber() + "_");
            }
            text.append("%0A--------------------------");
        }
        text.append(goodsList);
        text.append("%0A----------------------------%0A");
        if (user.getId() != 2){
            text.append("Інформація авторизованого користувача:");
            text.append("%0AІм'я: " + user.getLastName() + " " + user.getFirstName() + " " + user.getPatronymic());
            text.append("%0AEmail: " + user.getEmail());
            text.append("%0AАдреса доставки: " + user.getLocation());
            text.append("%0AВідділення: №" + user.getNumber());
            text.append("%0AАдреса відділення: " + user.getWarehouse());
            text.append("%0AПоштовий індекс: " + user.getPostIndex());
        }else {
            text.append("Замовлення створив не зареєстрований користувач, шукайте замовлення у адмінці!");
        }
        text.append("%0A----------------------------%0A");
        text.append(allProperties.toString().replace("\n", "%0A"));



        String urlString = "https://api.telegram.org/bot%s/sendMessage?parse_mode=markdown&chat_id=%s&text=%s";

        String apiToken = "1436198018:AAGVEmv55adxOP58btioZLCFpRO8M_-YvEg";
        String chatId = "-1001466406983";
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
        return new OrderPageDTO(orders, all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public OrderItemsDTO getOneOrderById(int id) {
        Orders one = ordersDao.getOne(id);
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

        return new OrderPageDTO(orders, all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public Orders updateOrder(int id, OrderItemsToUpdateDTO items) {
        final Orders order = ordersDao.getOne(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setCreatedAt(LocalDateTime.parse(items.getCreatedAt(), formatter));
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
