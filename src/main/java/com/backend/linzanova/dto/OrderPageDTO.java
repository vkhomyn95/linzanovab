package com.backend.linzanova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderPageDTO {

    private List<OrderItemsDTO> orders;
    private long totalElements;
    private int size;
    private boolean empty;
    private int totalPages;
}
