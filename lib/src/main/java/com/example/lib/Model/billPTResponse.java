package com.example.lib.Model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class billPTResponse {
    private int id ;
    private String dayStart ;
    private String dayEnd ;
    private Trainer trainer;
}
