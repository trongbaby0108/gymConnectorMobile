package com.example.lib.Model.Response;

import com.example.lib.Model.Request.Gym;

import lombok.Data;

@Data
public class billGymResponse {
    private int id ;
    private String dayStart ;
    private String dayEnd ;
    private Gym gym;
    private com.example.lib.Model.Request.combo combo;
}