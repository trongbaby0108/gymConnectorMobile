package com.example.lib.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class loginRequest {
    private String username;
    private String password;
}
