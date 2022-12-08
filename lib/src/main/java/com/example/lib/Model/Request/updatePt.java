package com.example.lib.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@AllArgsConstructor
@ToString
public class updatePt {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private int price;
}
