package com.backend.linzanova.entity.settlement;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserTracking {
    private String tracking_number;
    private String carrier_code;
    private String destination_code;
    private String lang;
}
