package com.example.dashboardmodern.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.loginRequest;
import com.example.lib.Model.Response.userInfoResponse;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Client;
import com.example.lib.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {
    int RC_SIGN_IN =001;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView txtUsername  = findViewById(R.id.username);
        TextView txtPass  = findViewById(R.id.pass);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client methods = RetrofitClient.getRetrofit().create(Client.class);
                loginRequest loginRequest = new loginRequest(txtUsername.getText().toString(),txtPass.getText().toString());
                Call<String> login = methods.login(loginRequest);
                login.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body()!=null){
                            String jwt = response.body();
                            Call<userInfoResponse> getUser = methods.getUser(response.body());
                            getUser.enqueue(new Callback<userInfoResponse>() {
                                @Override
                                public void onResponse(Call<userInfoResponse> call, Response<userInfoResponse> response) {
                                    Bundle bundle = new Bundle();
                                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    bundle.putSerializable("data",response.body());
                                    bundle.putString("jwt", jwt);
                                    bundle.putBoolean("isNormal",true);
                                    bundle.putBoolean("isGoogle",false);
                                    bundle.putBoolean("isFaceBook",false);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<userInfoResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        FloatingActionButton btnGG = findViewById(R.id.fab_google);
        btnGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(UserLoginActivity.this,gso);

                signIn();
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(UserLoginActivity.this);
                Client methods = RetrofitClient.getRetrofit().create(Client.class);
                Call<userInfoResponse> loginGoogle;
                if(acct.getPhotoUrl() == null)
                    loginGoogle = methods.createGoogleUser(acct.getEmail(),acct.getDisplayName(),"");
                else
                    loginGoogle = methods.createGoogleUser(acct.getEmail(),acct.getDisplayName(),acct.getPhotoUrl().toString());
                loginGoogle = methods.createGoogleUser(acct.getEmail(),acct.getDisplayName(),"");
                loginGoogle.enqueue(new Callback<userInfoResponse>() {
                    @Override
                    public void onResponse(Call<userInfoResponse> call, Response<userInfoResponse> response) {
                        Call<String> login = methods.login(new loginRequest(response.body().getUsername(),response.body().getUsername()));
                        login.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String jwt = response.body();
                                Call<userInfoResponse> getUser = methods.getUser(response.body());
                                getUser.enqueue(new Callback<userInfoResponse>() {
                                    @Override
                                    public void onResponse(Call<userInfoResponse> call, Response<userInfoResponse> response) {
                                        Bundle bundle = new Bundle();
                                        Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        bundle.putSerializable("data",response.body());
                                        bundle.putString("jwt",jwt);
                                        bundle.putBoolean("isNormal",false);
                                        bundle.putBoolean("isGoogle",true);
                                        bundle.putBoolean("isFaceBook",false);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<userInfoResponse> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<userInfoResponse> call, Throwable t) {

                    }
                });
            }
        });

        FloatingActionButton btnFb = findViewById(R.id.fab_facebook);
        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView txtNewAccount = findViewById(R.id.newAccount);
        txtNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, UserSignUpActivity.class);
                startActivity(intent);
            }
        });

        TextView txtForgetPass = findViewById(R.id.forget_pass);
        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


}