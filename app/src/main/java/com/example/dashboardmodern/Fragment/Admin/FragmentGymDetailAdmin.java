package com.example.dashboardmodern.Fragment.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.dashboardmodern.R;
import com.example.dashboardmodern.Activity.MainActivity;

import com.example.lib.Model.Response.Gym;
import com.example.lib.Repository.Admin;
import com.example.lib.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentGymDetailAdmin extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Gym gym;

    public FragmentGymDetailAdmin(Gym gym) {
        this.gym = gym;
    }

    public FragmentGymDetailAdmin() {

    }

    public static FragmentGymDetailAdmin newInstance(String param1, String param2) {
        FragmentGymDetailAdmin fragment = new FragmentGymDetailAdmin();
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
        View view =  inflater.inflate(R.layout.fragment_gym_profile, container, false);
        TextInputEditText name, phone, address, email ;
        Button btnUpdate;
        btnUpdate = view.findViewById(R.id.btnUpdate);
        name = view.findViewById(R.id.name);
        name.setText(gym.getName());
        email = view.findViewById(R.id.email);
        email.setText(gym.getEmail());
        phone = view.findViewById(R.id.phone);
        phone.setText(gym.getPhone());
        address = view.findViewById(R.id.address);
        address.setText(gym.getAddress());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admin methods = RetrofitClient.getRetrofit().create(Admin.class);
                MainActivity mainActivity = (MainActivity) getActivity();
                Call<String> update = methods.updateGym("Bearer " + mainActivity.jwt,gym.getId(),email.getText().toString(),address.getText().toString(),name.getText().toString(),phone.getText().toString());
                update.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        ShowMessage("Cập nhật thành công");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        return view ;
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