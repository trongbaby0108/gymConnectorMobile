package com.example.lib.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class addGymComment {
    private String content;
    private float vote;
    private int gymId;
    private int userId;
}