package com.example.domain.user.model;

import lombok.Data;

@Data
public class MUser {
    private String userId;
    private String password;
    private String userName;
    private String role;
}
