package com.example.dashboardmodern.Fragment.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.UserImgAdapter;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.updateUser;
import com.example.lib.Model.Response.billGymResponse;
import com.example.lib.Model.Response.billPTResponse;
import com.example.lib.Model.Response.userInfoResponse;
import com.example.lib.Repository.Admin;
import com.example.lib.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUserInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvUser;
    private UserImgAdapter userAdapter;
    View view;

    userInfoResponse user  ;

    public FragmentUserInfo() {
        // Required empty public constructor
    }
    public FragmentUserInfo(userInfoResponse user ) {
        this.user = user;
    }

    public static FragmentUserInfo newInstance(String param1, String param2) {
        FragmentUserInfo fragment = new FragmentUserInfo();
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
        Admin methods = RetrofitClient.getRetrofit().create(Admin.class);

        view = inflater.inflate(R.layout.fragment_user, container, false);
        if(user != null){
            TextView txtName = view.findViewById(R.id.full_name);
            txtName.setText(user.getName());
            ImageView img = view.findViewById(R.id.profile_image);
            if(user.getAvatar() != null)
                Picasso.get().load(user.getAvatar()).into(img);

            TextInputEditText name = view.findViewById(R.id.txtName);
            name.setText(user.getName());

            TextInputEditText email = view.findViewById(R.id.txtEmail);
            email.setText(user.getEmail());

            TextInputEditText phone = view.findViewById(R.id.txtPhone);
            phone.setText(user.getPhone());

            TextInputEditText address = view.findViewById(R.id.txtAddress);
            address.setText(user.getAddress());

            Button btn_update = view.findViewById(R.id.btn_update);
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<userInfoResponse> update =methods.update(new updateUser(user.getId(),name.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString()));
                    update.enqueue(new Callback<userInfoResponse>() {
                        @Override
                        public void onResponse(Call<userInfoResponse> call, Response<userInfoResponse> response) {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.acc = response.body();
                            ShowMessage("cập nhật thành công");
                        }

                        @Override
                        public void onFailure(Call<userInfoResponse> call, Throwable t) {

                        }
                    });
                }
            });


            Call<billGymResponse> checkGymExit = methods.checkGymExit(user.getId());
            checkGymExit.enqueue(new Callback<billGymResponse>() {
                @Override
                public void onResponse(Call<billGymResponse> call, Response<billGymResponse> response) {
                    if(response.body().getGym() != null){
                        ImageView imgGym = view.findViewById(R.id.imgGym);
                        Picasso.get().load(response.body().getGym().getAvatar()).into(imgGym);
                        TextView txt_gym = view.findViewById(R.id.txt_gym);
                        txt_gym.setText(response.body().getGym().getName());
                    }
                }

                @Override
                public void onFailure(Call<billGymResponse> call, Throwable t) {

                }
            });

            Call<billPTResponse> checkPTExit = methods.checkPTExit(user.getId());
            checkPTExit.enqueue(new Callback<billPTResponse>() {
                @Override
                public void onResponse(Call<billPTResponse> call, Response<billPTResponse> response) {
                    if(response.body().getTrainer() != null){
                        ImageView imgPT = view.findViewById(R.id.imgPT);
                        Picasso.get().load(response.body().getTrainer().getAvatar()).into(imgPT);
                        TextView txt_pt = view.findViewById(R.id.txt_pt);
                        txt_pt.setText(response.body().getTrainer().getName());
                    }
                }

                @Override
                public void onFailure(Call<billPTResponse> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }
        return view;
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