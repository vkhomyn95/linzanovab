package com.backend.linzanova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCabinetStatsDTO {
    private int ordersQuantity;
    private int deliveredOrdersQuantity;
    private int bonusesQuantity;
}
