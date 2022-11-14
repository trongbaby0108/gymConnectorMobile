package com.example.lib.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class updateUser{
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
}