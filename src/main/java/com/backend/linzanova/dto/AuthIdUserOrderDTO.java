package com.backend.linzanova.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AuthIdUserOrderDTO {
    private int id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String location;
    private String warehouse;
    private String number;
}
