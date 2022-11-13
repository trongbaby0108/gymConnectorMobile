package com.example.dashboardmodern.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dashboardmodern.R;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Client;
import com.example.lib.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPTActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ptactivity);

        Button btn = findViewById(R.id.btn_confirm);
        TextView txt = findViewById(R.id.txt_confim);
        Bundle bundle = getIntent().getExtras();
        String username ;
        username = bundle.getString("username");
        if (bundle != null) {

            Client methods = RetrofitClient.getRetrofit().create(Client.class);
            Call<String> call = methods.sendTokenPT(username);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<String> confirmToken = methods.confirmTokenPT(username,txt.getText().toString());
                    confirmToken.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Intent intent = new Intent(ConfirmPTActivity.this, PTLoginActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            });
        }
    }
}