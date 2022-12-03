package com.example.dashboardmodern.Fragment.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Response.Gym;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Home;
import com.example.lib.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNewCombo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNewCombo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn_add ;
    TextView txtName , txtPrice;
    public FragmentNewCombo() {
        // Required empty public constructor
    }

    public static FragmentNewCombo newInstance(String param1, String param2) {
        FragmentNewCombo fragment = new FragmentNewCombo();
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
        View view =  inflater.inflate(R.layout.fragment_new_combo, container, false);

        Spinner spinner = view.findViewById(R.id.spnGym);
        btn_add = view.findViewById(R.id.btn_add);
        txtName = view.findViewById(R.id.name);
        txtPrice = view.findViewById(R.id.price);

        Admin methods = RetrofitClient.getRetrofit().create(Admin.class);
        Home home = RetrofitClient.getRetrofit().create(Home.class);
        Call<List<Gym>> callGym = home.getGym();
        callGym.enqueue(new Callback<List<Gym>>() {
            @Override
            public void onResponse(Call<List<Gym>> call, Response<List<Gym>> response) {
                ArrayAdapter<Gym> dataAdapter = new ArrayAdapter<Gym>(getContext(), android.R.layout.simple_spinner_item, response.body());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<List<Gym>> call, Throwable t) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gym gym = (Gym) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                MainActivity mainActivity = (MainActivity) getActivity();
                Call<String> callNewCombo = methods.addCombo(
                        "Bearer "+mainActivity.jwt,
                        txtName.getText().toString(),
                        Integer.parseInt(txtPrice.getText().toString()),
                        gym.getId()
                );

                callNewCombo.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code() == 200){
                            showMessage("thành công");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    public void showMessage(String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Thông báo");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

}