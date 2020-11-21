package com.backend.linzanova.dto;

import com.backend.linzanova.entity.order.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderPageDTO {

    private List<Orders> orders;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;
}
