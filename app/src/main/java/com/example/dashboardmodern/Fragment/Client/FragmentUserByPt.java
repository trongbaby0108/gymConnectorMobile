package com.example.dashboardmodern.Fragment.Client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.UserPTAdapter;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Response.PTInfoResponse;
import com.example.lib.Model.Response.userInfoResponse;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Client;
import com.example.lib.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentUserByPt extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PTInfoResponse pt;

    public FragmentUserByPt() {
        // Required empty public constructor
    }

    public FragmentUserByPt(PTInfoResponse pt) {
        this.pt = pt;
    }


    public static FragmentUserByPt newInstance(String param1, String param2) {
        FragmentUserByPt fragment = new FragmentUserByPt();
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
        View view =  inflater.inflate(R.layout.fragment_user_by_pt, container, false);
        RecyclerView rcv_Users = view.findViewById(R.id.rcv_Users);
        rcv_Users.setHasFixedSize(true);
        MainActivity mainActivity = (MainActivity) getActivity();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        rcv_Users.setLayoutManager(linearLayoutManager);
        Client client = RetrofitClient.getRetrofit().create(Client.class);
        Call<List<userInfoResponse>> getUserByPT =  client.getUserByPT("Bearer "+mainActivity.jwt,pt.getId());
        System.out.println(mainActivity.jwt);
        getUserByPT.enqueue(new Callback<List<userInfoResponse>>() {
            @Override
            public void onResponse(Call<List<userInfoResponse>> call, Response<List<userInfoResponse>> response) {
                UserPTAdapter userPTAdapter = new UserPTAdapter(response.body());
                rcv_Users.setAdapter(userPTAdapter);
            }

            @Override
            public void onFailure(Call<List<userInfoResponse>> call, Throwable t) {

            }
        });
        return view ;
    }
}