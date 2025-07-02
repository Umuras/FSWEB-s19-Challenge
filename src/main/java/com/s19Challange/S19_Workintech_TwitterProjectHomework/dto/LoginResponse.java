package com.s19Challange.S19_Workintech_TwitterProjectHomework.dto;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.entity.User;

public record LoginResponse(UserResponse userResponse, String token) {
}
