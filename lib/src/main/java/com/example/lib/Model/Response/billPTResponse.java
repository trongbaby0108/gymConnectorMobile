package com.example.lib.Model.Response;

import com.example.lib.Model.Request.Trainer;

import lombok.Data;

@Data
public class billPTResponse {
    private int id ;
    private String dayStart ;
    private String dayEnd ;
    private Trainer trainer;
}
