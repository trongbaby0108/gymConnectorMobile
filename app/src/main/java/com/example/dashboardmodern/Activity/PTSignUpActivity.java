package com.example.dashboardmodern.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dashboardmodern.R;
import com.example.dashboardmodern.Utils.RealPathUtil;
import com.example.lib.Model.Response.Gym;
import com.example.lib.Model.Request.ptSignIn;
import com.example.lib.Repository.Client;
import com.example.lib.Repository.Home;
import com.example.lib.RetrofitClient;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PTSignUpActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private static final int MY_REQUEST_CODE = 10;
    EditText username, pass, address, phone,  email,fullName,price;
    Spinner gymCode;
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
        fullName = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone_num);
        gymCode = findViewById(R.id.idGym);
        price = findViewById(R.id.price);
        btn_choose_pic = findViewById(R.id.choose_pic);
        btn_signup = findViewById(R.id.btn_signup);
        btnBack = findViewById(R.id.btnBack);

        Home home = RetrofitClient.getRetrofit().create(Home.class);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateUsername();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateUsername();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateUsername();
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassword();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePassword();
            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateFullName();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateFullName();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateFullName();
            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateAddress();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePhone();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePrice();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Call<List<Gym>> callGym = home.getGym();
        callGym.enqueue(new Callback<List<Gym>>() {
            @Override
            public void onResponse(Call<List<Gym>> call, Response<List<Gym>> response) {
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,response.body());
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                gymCode.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Gym>> call, Throwable t) {

            }
        });

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
                Gym gymSelected = (Gym) gymCode.getSelectedItem();
                if(!validateUsernameOnSubmit()
                        || !validateFullNameOnSubmit()
                        || !validatePasswordOnSubmit()
                        || !validateAddressOnSubmit()
                        || !validateEmailOnSubmit()
                        || !validatePhoneOnSubmit()
                        || !validatePriceOnSubmit()
                )
                    ShowMessage("vui lòng nhập đúng thông tin");
                else doSignup(
                        username.getText().toString(),
                        pass.getText().toString(),
                        email.getText().toString(),
                        fullName.getText().toString(),
                        phone.getText().toString(),
                        address.getText().toString(),
                        Integer.parseInt(price.getText().toString()),
                        gymSelected.getId()
                );

            }
        });

    }

    private boolean validateUsernameOnSubmit() {
        String val = username.getText().toString().trim();
        if (val.isEmpty()) {
            username.setError("Tên tài khoảng không được để trống");
            username.requestFocus();
            return false;
        } else if (val.length() > 20) {
            username.setError("Tên tài khoảng quá dài");
            username.requestFocus();
            return false;
        } else if (val.indexOf(' ') != -1)  {
            username.setError("Tên tài khoảng không được để khoảng trống!");
            username.requestFocus();
            return false;
        }
        else{
            return true;
        }
    }
    private boolean validatePasswordOnSubmit() {
        String val = pass.getText().toString().trim();
        String checkLength = "^.{6,}$";
        String checkPassword = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,20}$";
        if (val.isEmpty()) {
            pass.setError("Mật khẩu không được để trống");
            pass.requestFocus();
            return false;
        } else if (!val.matches(checkLength)) {
            pass.setError("Mật khẩu phải có ít nhất 6 ký tự!");
            pass.requestFocus();
            return false;
        } else if (val.length() > 20) {
            pass.setError("Mật khẩu quá dài");
            pass.requestFocus();
            return false;
        } else if (!val.matches(checkPassword)) {
            pass.setError("Mật khẩu phải bao gồm cả số lẫn ký tự");
            pass.requestFocus();
            return false;
        }
        else if (val.indexOf(' ') != -1)  {
            pass.setError("Tên tài khoảng không được để khoảng trống!");
            pass.requestFocus();
            return false;
        } else {
            return true;
        }
    }
    private boolean validateFullNameOnSubmit() {
        String val = fullName.getText().toString().trim();
        if (val.isEmpty()) {
            fullName.setError("Tên không được để trống");
            return false;
        } else {
            fullName.setError(null);
            return true;
        }
    }
    private boolean validatePriceOnSubmit() {
        String val = price.getText().toString().trim();
        if (val.isEmpty()) {
            price.setError("Tên không được để trống");
            return false;
        } else {
            price.setError(null);
            return true;
        }
    }
    private boolean validateAddressOnSubmit() {
        String val = address.getText().toString().trim();
        if (val.isEmpty()) {
            address.setError("Tên không được để trống");
            address.requestFocus();
            return false;
        }
        else return true;
    }
    private boolean validateEmailOnSubmit() {
        String val = email.getText().toString().trim();
        String pattern = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (val.isEmpty()) {
            email.setError("Tên không được để trống");
            email.requestFocus();
            return true;
        } else if(!val.matches(pattern)){
            email.setError("Sai email");
            email.requestFocus();
            return false;
        }
        else return true;
    }
    private boolean validatePhoneOnSubmit() {
        String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        String val = phone.getText().toString().trim();
        if (val.isEmpty()) {
            phone.setError("Tên không được để trống");
            phone.requestFocus();
            return false;
        } else if(!val.matches(pattern)){
            phone.setError("Sai email");
            phone.requestFocus();
            return false;
        }
        else return true;
    }
    private void validateUsername() {
        String val = username.getText().toString().trim();
        if (val.isEmpty()) {
            username.setError("Tên tài khoảng không được để trống");
            username.requestFocus();
        } else if (val.length() > 20) {
            username.setError("Tên tài khoảng quá dài");
            username.requestFocus();
        } else if (val.indexOf(' ') != -1) {
            username.setError("Tên tài khoảng không được để khoảng trống!");
            username.requestFocus();
        }
    }
    private void validatePassword() {
        String val = pass.getText().toString().trim();
        String checkLength = "^.{6,}$";
        String checkPassword =  "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,20}$";
        if (val.isEmpty()) {
            pass.setError("Mật khẩu không được để trống");
            pass.requestFocus();
        } else if (!val.matches(checkLength)) {
            pass.setError("Mật khẩu phải có ít nhất 6 ký tự!");
            pass.requestFocus();
        } else if (val.length() > 20) {
            pass.setError("Mật khẩu quá dài");
            pass.requestFocus();
        } else if (!val.matches(checkPassword)) {
            pass.setError("Mật khẩu phải bao gồm cả số lẫn ký tự");
            pass.requestFocus();
        }
        else if (val.indexOf(' ') != -1)  {
            pass.setError("Tên tài khoảng không được để khoảng trống!");
            pass.requestFocus();
        }
    }
    private void validateFullName() {
        String val = fullName.getText().toString().trim();
        if (val.isEmpty()) {
            fullName.setError("Tên không được để trống");
        }
    }
    private void validatePrice() {
        String val = price.getText().toString().trim();
        if (val.isEmpty()) {
            price.setError("Tên không được để trống");
        }
    }
    private void validateAddress() {
        String val = address.getText().toString().trim();
        if (val.isEmpty()) {
            address.setError("Tên không được để trống");
            address.requestFocus();
        }
    }
    private void validateEmail() {
        String val = email.getText().toString().trim();
        String pattern = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (val.isEmpty()) {
            email.setError("Tên không được để trống");
            email.requestFocus();
        } else if(!val.matches(pattern)){
            email.setError("Sai email");
            email.requestFocus();
        }
    }
    private void validatePhone() {
        String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        String val = phone.getText().toString().trim();
        if (val.isEmpty()) {
            phone.setError("Tên không được để trống");
            phone.requestFocus();
        } else if(!val.matches(pattern)){
            phone.setError("Sai email");
            phone.requestFocus();
        }
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
    public void ShowMessage(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cám ơn bạn");
        builder.setTitle(text);
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);
        builder.create().show();
    }

}