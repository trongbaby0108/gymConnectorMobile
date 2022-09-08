package com.example.lib.Model;

import lombok.Data;

@Data
public class Comment {
    private int id ;
    private String content ;
    private float vote ;
    private String name ;
    private String avatar;
}
