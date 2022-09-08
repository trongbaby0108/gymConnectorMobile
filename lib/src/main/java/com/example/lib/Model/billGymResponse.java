package com.example.lib.Model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class billGymResponse {
    private int id ;
    private String dayStart ;
    private String dayEnd ;
    private Gym gym;
    private combo combo;
}