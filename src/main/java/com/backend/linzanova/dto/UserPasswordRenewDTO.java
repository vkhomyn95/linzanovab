package com.backend.linzanova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserPasswordRenewDTO {
    private String oldPassword;
    private String newPassword;
}
