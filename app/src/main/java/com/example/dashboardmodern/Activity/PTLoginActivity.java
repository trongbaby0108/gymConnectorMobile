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
import com.example.lib.Model.PTInfoResponse;
import com.example.lib.Model.loginRequest;
import com.example.lib.Model.userInfoResponse;
import com.example.lib.Repository.Methods;
import com.example.lib.RetrofitClient;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PTLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptlogin);

        TextView txtnewAccount = findViewById(R.id.newAccount);
        txtnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PTLoginActivity.this, PTSignUpActivity.class);
                startActivity(intent);
            }
        });
        TextView username = findViewById(R.id.username);
        TextView pass = findViewById(R.id.pass);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Methods methods = RetrofitClient.getRetrofit().create(Methods.class);
                loginRequest loginRequest = new loginRequest(username.getText().toString(),pass.getText().toString());
                Call<String> login = methods.login(loginRequest);
                login.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code() == 200){
                            String jwt= response.body();
                            Call<PTInfoResponse> getUser = methods.getPTInfo(jwt);
                            getUser.enqueue(new Callback<PTInfoResponse>() {
                                @Override
                                public void onResponse(Call<PTInfoResponse> call, Response<PTInfoResponse> response) {
                                    if(response.body().getRole().equals("PERSONAL_TRAINER")){
                                        Bundle bundle = new Bundle();
                                        Intent intent = new Intent(PTLoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        bundle.putSerializable("pt",response.body());
                                        bundle.putString("jwt",jwt);
                                        bundle.putBoolean("isNormal",true);
                                        bundle.putBoolean("isGoogle",false);
                                        bundle.putBoolean("isFaceBook",false);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        showMessage("Bạn không phải PT");
                                    }
                                }

                                @Override
                                public void onFailure(Call<PTInfoResponse> call, Throwable t) {

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

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PTLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void showMessage(String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(PTLoginActivity.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Thông báo");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}