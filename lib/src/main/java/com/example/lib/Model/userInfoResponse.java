package com.example.lib.Model;

import java.io.Serializable;

import lombok.Data;

@Data
public class userInfoResponse implements Serializable {
    private int id ;
    private String username ;
    private String name ;
    private String address;
    private String phone ;
    private String email ;
    private String avatar;
    private String role;
    private boolean enable;
}
