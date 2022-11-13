package com.example.dashboardmodern.Fragment.Client;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dashboardmodern.R;
import com.example.dashboardmodern.Utils.RealPathUtil;
import com.example.lib.Model.Response.PTInfoResponse;
import com.example.lib.Repository.Admin;
import com.example.lib.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPtNewImg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPtNewImg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Uri imageUri;
    private static final int PICK_IMAGE = 100;
    private static final int MY_REQUEST_CODE = 10;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PTInfoResponse ptInfoResponse;
    ImageView imageView;
    Button choose_pic,btn_add;

    public FragmentPtNewImg() {
        // Required empty public constructor
    }

    public FragmentPtNewImg(PTInfoResponse ptInfoResponse) {
        this.ptInfoResponse = ptInfoResponse;
    }

    public static FragmentPtNewImg newInstance(String param1, String param2) {
        FragmentPtNewImg fragment = new FragmentPtNewImg();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pt_new_img, container, false);
        imageView = view.findViewById(R.id.imageProfile);
        choose_pic = view.findViewById(R.id.choose_pic);
        choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admin methods = RetrofitClient.getRetrofit().create(Admin.class);
                String strRealPath = RealPathUtil.getRealPath(getContext(),imageUri);
                File file = new File(strRealPath);
                RequestBody rqAvt =  RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part mutipartBodyAvt = MultipartBody.Part.createFormData("pic" , file.getName(),rqAvt);
                RequestBody rqID= RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(ptInfoResponse.getId()));
                Call<String> newImgPT = methods.newImgPT(rqID,mutipartBodyAvt);
                newImgPT.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        ShowMessage("upload xong");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        return view;
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

    public void ShowMessage(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Cám ơn bạn");
        builder.setTitle(text);
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);
        builder.create().show();
    }

}