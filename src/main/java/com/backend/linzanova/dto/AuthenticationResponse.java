package com.backend.linzanova.dto;

import com.backend.linzanova.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthenticationResponse {
    private String token;
}
