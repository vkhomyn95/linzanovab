package com.backend.linzanova.dto;

import com.backend.linzanova.entity.order.Delivery;
import com.backend.linzanova.entity.order.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RequestDTO {
    private String createdAt;
    private int totalSumm;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String email;
    private String meestTrackingId;
    private String phone;
    private String customerComment;
    private Delivery delivery;
    private List<ItemDTO> items;
    private int user;
}
