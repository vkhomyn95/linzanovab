package com.backend.linzanova.dto;

import com.backend.linzanova.entity.order.Delivery;
import com.backend.linzanova.entity.order.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RequestDTO {
    private String createdAt;
    private int totalSumm;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String phone;
    private String customerComment;
    private Delivery delivery;
    private List<ItemDTO> items;
    private int user;
}
