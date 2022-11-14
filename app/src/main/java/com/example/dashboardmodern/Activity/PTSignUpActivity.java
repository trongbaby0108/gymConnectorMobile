package com.example.dashboardmodern.Activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.dashboardmodern.R;
import com.example.dashboardmodern.Utils.RealPathUtil;
import com.example.lib.Model.Request.ptSignIn;
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

public class PTSignUpActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private static final int MY_REQUEST_CODE = 10;
    EditText username, pass, address, phone, gymCode, email,fullname,price;
    Uri imageUri;
    ImageView img ;
    Button btn_choose_pic,btn_signup;
    ImageButton btnBack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptsign_up);
        username = findViewById(R.id.account);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        fullname = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone_num);
        gymCode = findViewById(R.id.idGym);
        price = findViewById(R.id.price);
        btn_choose_pic = findViewById(R.id.choose_pic);
        btn_signup = findViewById(R.id.btn_signup);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PTSignUpActivity.this, PTLoginActivity.class);
                startActivity(intent);
            }
        });

        btn_choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        img = findViewById(R.id.imageProfile);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSignup(
                        username.getText().toString(),
                        pass.getText().toString(),
                        email.getText().toString(),
                        fullname.getText().toString(),
                        phone.getText().toString(),
                        address.getText().toString(),
                        Integer.parseInt(price.getText().toString()),
                        Integer.parseInt(gymCode.getText().toString())
                );
            }
        });

    }


    private void doSignup(String username,
                          String pass,
                          String email,
                          String name,
                          String phone,
                          String address,
                          int price,
                          int gymID
    ) {
        Client methods = RetrofitClient.getRetrofit().create(Client.class);
        String strRealPath = RealPathUtil.getRealPath(this,imageUri);
        File file = new File(strRealPath);
        RequestBody rqAvt =  RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part mutipartBodyAvt = MultipartBody.Part.createFormData("avatar" , file.getName(),rqAvt);
        Call<String> signUpUser = methods.signUpPT(new ptSignIn(username,pass,name,address,email,phone,gymID,price));
        signUpUser.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                RequestBody rqUsername = RequestBody.create(MediaType.parse("multipart/form-data"),username);
                Call<String> uploadAvatarPT = methods.uploadAvatarPT(rqUsername,mutipartBodyAvt);

                uploadAvatarPT.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

                Bundle bundle = new Bundle();
                Intent intent = new Intent(PTSignUpActivity.this, ConfirmPTActivity.class);
                bundle.putString("username",username);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3 );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            System.out.println(imageUri);
            img.setImageURI(imageUri);
        }

    }
}