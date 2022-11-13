package com.example.lib.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class combo {
    private int id ;
    private String name ;
    private int price ;
    private Gym gym;
}
