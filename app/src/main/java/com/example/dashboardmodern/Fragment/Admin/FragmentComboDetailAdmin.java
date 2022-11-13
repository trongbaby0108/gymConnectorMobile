package com.example.dashboardmodern.Fragment.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.combo;
import com.example.lib.Repository.Admin;
import com.example.lib.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentComboDetailAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentComboDetailAdmin extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public combo combo;

    public FragmentComboDetailAdmin() {

    }

    public FragmentComboDetailAdmin(combo combo) {
        this.combo = combo;
    }

    public static FragmentComboDetailAdmin newInstance(String param1, String param2) {
        FragmentComboDetailAdmin fragment = new FragmentComboDetailAdmin();
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
        View view = inflater.inflate(R.layout.fragment_combo_detail_admin, container, false);
        Button btn_update ;
        TextView txtName , txtPrice;
        btn_update = view.findViewById(R.id.btn_update);
        txtName = view.findViewById(R.id.name);
        txtName.setText(combo.getName());
        txtPrice = view.findViewById(R.id.price);
        txtPrice.setText(String.valueOf(combo.getPrice()));
        Admin methods = RetrofitClient.getRetrofit().create(Admin.class);
        MainActivity mainActivity = (MainActivity) getActivity();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> update = methods.updateCombo(
                        "Bearer " + mainActivity.jwt,
                        combo.getId(),
                        txtName.getText().toString(),
                        Integer.parseInt(txtPrice.getText().toString()),
                        combo.getGym().getId());
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