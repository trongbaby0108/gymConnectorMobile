package com.example.dashboardmodern.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.dashboardmodern.R;
import com.example.dashboardmodern.Utils.RealPathUtil;
import com.example.lib.Model.Request.userSignIn;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Client;
import com.example.lib.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSignUpActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private static final int MY_REQUEST_CODE = 10;
    Button signup ,btn_choose_pic;
    EditText email,pass,phone,name,username,address;
    float v=0;
    Uri imageUri;
    ImageView imageView;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        username= findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone_num);
        signup = findViewById(R.id.btn_signup);
        address = findViewById(R.id.address);
        imageView= findViewById(R.id.imageProfile);
        btn_choose_pic = findViewById(R.id.choose_pic);
        btnBack = findViewById(R.id.btnBack);

        btn_choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSignup(
                        username.getText().toString(),
                        pass.getText().toString(),
                        email.getText().toString(),
                        name.getText().toString(),
                        phone.getText().toString(),
                        address.getText().toString()
                );
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSignUpActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return ;
        }

        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }

        else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,MY_REQUEST_CODE);
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3 );

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            System.out.println(imageUri);
            imageView.setImageURI(imageUri);
        }

    }


    private void doSignup(String username,
                          String pass,
                          String email,
                          String name,
                          String phone,
                          String address
    ) {
        Client methods = RetrofitClient.getRetrofit().create(Client.class);
        String strRealPath = RealPathUtil.getRealPath(this,imageUri);
        File file = new File(strRealPath);
        RequestBody rqAvt =  RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part mutipartBodyAvt = MultipartBody.Part.createFormData("avatar" , file.getName(),rqAvt);
        Call<String> signUpUser = methods.signUpUser(new userSignIn(username,pass,name,email,address,phone));
        signUpUser.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                RequestBody rqUsername = RequestBody.create(MediaType.parse("multipart/form-data"),username);
                Call<String> uploadAvatar = methods.uploadAvatar(rqUsername,mutipartBodyAvt);
                uploadAvatar.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                Bundle bundle = new Bundle();
                Intent intent = new Intent(UserSignUpActivity.this, ConfirmUserActivity.class);
                bundle.putString("username",username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}