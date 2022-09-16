package com.lianxi.springboot.controller.dto;

import lombok.Data;

//接收前端登录请求
@Data
public class UserDto {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;

    private String token;
}
