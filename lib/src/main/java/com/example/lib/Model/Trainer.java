package com.example.lib.Model;

import lombok.Data;

@Data
public class Trainer {
    private int id ;
    private String username ;
    private String name ;
    private String address;
    private String phone ;
    private String email ;
    private String avatar;
    private int fee;
    private String Role ;
    private float rate = 5;
    private boolean enable ;
    private Gym gym;
}
