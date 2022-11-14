package com.example.lib.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ptSignIn {
    private String username;
    private String password;
    private String name;
    private String address;
    private String email;
    private String phone;
    private int gymId;
    private int price;
}
