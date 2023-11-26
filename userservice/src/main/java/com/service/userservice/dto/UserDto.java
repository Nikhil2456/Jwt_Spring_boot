package com.service.userservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private String password;
}
