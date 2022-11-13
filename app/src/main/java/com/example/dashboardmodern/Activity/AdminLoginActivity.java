package com.example.dashboardmodern.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.loginRequest;
import com.example.lib.Model.Response.userInfoResponse;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Client;
import com.example.lib.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Button btn ;
        TextView txtUsername, txtPass;
        ImageButton btnBack;

        btn = findViewById(R.id.btn_login);
        txtUsername = findViewById(R.id.username);
        txtPass = findViewById(R.id.pass);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client methods = RetrofitClient.getRetrofit().create(Client.class);
                loginRequest loginRequest = new loginRequest(txtUsername.getText().toString(),txtPass.getText().toString());
                Call<String> login = methods.login(loginRequest);
                login.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code() == 200){
                            String jwt= response.body();
                            Call<userInfoResponse> getUser = methods.getUser(jwt);
                            getUser.enqueue(new Callback<userInfoResponse>() {
                                @Override
                                public void onResponse(Call<userInfoResponse> call, Response<userInfoResponse> response) {
                                    if(response.body().getRole().equals("ADMIN")){
                                        Bundle bundle = new Bundle();
                                        Intent intent = new Intent(AdminLoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        bundle.putSerializable("data",response.body());
                                        bundle.putString("jwt",jwt);
                                        bundle.putBoolean("isNormal",true);
                                        bundle.putBoolean("isGoogle",false);
                                        bundle.putBoolean("isFaceBook",false);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        showMessage("Bạn không phải admin");
                                    }
                                }

                                @Override
                                public void onFailure(Call<userInfoResponse> call, Throwable t) {

                                }
                            });
                        }

                        else {
                            showMessage("Tài khoảng hoặc mật khẩu không đúng");
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void showMessage(String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(AdminLoginActivity.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Thông báo");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}