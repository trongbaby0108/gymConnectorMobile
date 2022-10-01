package com.example.dashboardmodern.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dashboardmodern.R;

public class LoginActivity extends AppCompatActivity {
    Button btnAdmin,btnUser,btnPt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAdmin = findViewById(R.id.btn_admin);
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnUser = findViewById(R.id.btn_user);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnPt = findViewById(R.id.btn_pt);
        btnPt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PTLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}