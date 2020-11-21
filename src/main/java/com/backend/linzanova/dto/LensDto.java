package com.backend.linzanova.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LensDto {

    private int lensId;
    private String name;
    private int price;
    private String userName;
}
