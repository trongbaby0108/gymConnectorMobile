package com.example.lib.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ptSignIn {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private int price;
    private int id;
}
