package com.example.lib.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class addPtComment {
    private String content;
    private float vote;
    private int ptId;
    private int userId;
}
