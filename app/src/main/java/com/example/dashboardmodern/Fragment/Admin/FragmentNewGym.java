package com.example.dashboardmodern.Fragment.Admin;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Fragment.Client.FragmentHome;
import com.example.dashboardmodern.R;
import com.example.dashboardmodern.Utils.RealPathUtil;
import com.example.lib.Repository.Admin;
import com.example.lib.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNewGym#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNewGym extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PICK_IMAGE = 100;
    private static final int MY_REQUEST_CODE = 10;
    MainActivity mainActivity;
    Uri imageUri;
    ImageView imageView;
    Button btn_add ;
    TextInputEditText name, phone, address, email ;

    private String mParam1;
    private String mParam2;

    public FragmentNewGym() {
    }

    public static FragmentNewGym newInstance(String param1, String param2) {
        FragmentNewGym fragment = new FragmentNewGym();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_new_gym, container, false);
        TextView txtAddNew = view.findViewById(R.id.txtAddNew);
        mainActivity = (MainActivity) getActivity();
        imageView = view.findViewById(R.id.img);
        txtAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        btn_add = view.findViewById(R.id.btn_add);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        address = view.findViewById(R.id.address);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCreate(email.getText().toString(),
                        name.getText().toString(),
                        phone.getText().toString(),
                        address.getText().toString());
            }
        });

        return view;
    }


    public void doCreate(String email,
                         String name,
                         String phone,
                         String address){

        Admin methods = RetrofitClient.getRetrofit().create(Admin.class);

        Call<String> addGym = methods.addGym("Bearer "+ mainActivity.jwt, email, address, name, phone);

        addGym.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String strRealPath = RealPathUtil.getRealPath(getActivity(),imageUri);
                File file = new File(strRealPath);
                RequestBody rqAvt =  RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part mutipartBodyAvt = MultipartBody.Part.createFormData("img" , file.getName(),rqAvt);
                RequestBody rqUsername = RequestBody.create(MediaType.parse("multipart/form-data"),name);
                Call<String> uploadAvatar = methods.addGymImg("Bearer "+ mainActivity.jwt, rqUsername, mutipartBodyAvt);
                uploadAvatar.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentHome());
        fragmentTransaction.addToBackStack("Fragment home");
        fragmentTransaction.commit();
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return ;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
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

            imageView.setImageURI(imageUri);
        }

    }
}