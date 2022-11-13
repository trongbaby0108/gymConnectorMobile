package com.example.lib.Model.Request;

import lombok.Data;
import lombok.ToString;

@Data
public class Gym {
    private int id ;
    private String name ;
    private String address ;
    private String phone ;
    private String email ;
    private String avatar;
    private float rate;

    @Override
    public String toString() {
        return
                name+
                ", địa chỉ: " + address;
    }
}
