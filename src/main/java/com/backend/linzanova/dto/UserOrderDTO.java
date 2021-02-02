package com.backend.linzanova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserOrderDTO {
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String location;
    private String warehouse;
    private String number;
    private String postIndex;

    public UserOrderDTO(Field email) {
    }
}
