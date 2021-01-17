package com.backend.linzanova.dto;

import com.backend.linzanova.entity.order.Delivery;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderItemsDTO {
    private int id;
    private int totalSumm;
    private int priceToPayAfterDelivery;
    private int priceToPayNow;

    private String createdAt;

    private String lastName;
    private String firstName;
    private String email;
    private String patronymic;
    private String phone;
    private String customerComment;

    private Delivery delivery;

    private String properties;

    private String meestTrackingId;
    private String novaPoshtaTTN;

    private boolean delivered;
    private boolean canceled;

    private String userEmail;
    private int userId;


}
