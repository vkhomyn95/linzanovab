package com.backend.linzanova.entity.settlement;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.sql.rowset.spi.SyncResolver;

@AllArgsConstructor
@Data
public class Tracking {
    private String tracking_number;
    private String carrier_code;
    private String destination_code;
    private String customer_name;
    private String customer_email;
    private String customer_phone;
    private String order_id;
    private String lang;


}
