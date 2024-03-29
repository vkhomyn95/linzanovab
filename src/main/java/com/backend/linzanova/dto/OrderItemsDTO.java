package com.backend.linzanova.dto;

import com.backend.linzanova.entity.order.Delivery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
public class OrderItemsDTO {
    private int id;
    private int totalSumm;
    private int priceToPayAfterDelivery;
    private int priceToPayNow;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

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
