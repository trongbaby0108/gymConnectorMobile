package com.example.dashboardmodern.Fragment.Client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.ComboAdapter;
import com.example.dashboardmodern.Apdapter.GymHomeApdapter;
import com.example.dashboardmodern.Apdapter.PTHomeAdapter;
import com.example.dashboardmodern.Fragment.Admin.FragmentComboDetailAdmin;
import com.example.dashboardmodern.Fragment.Admin.FragmentGymDetailAdmin;
import com.example.dashboardmodern.Fragment.Admin.FragmentPTDetailAdmin;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.Gym;
import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.combo;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Home;
import com.example.lib.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    RecyclerView Rcv_Gym,Rcv_Pt,Rcv_Combo;
    RecyclerView.Adapter adapter;
    private String mParam1;
    private String mParam2;
    MainActivity mainActivity;



    public FragmentHome() {

    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Rcv_Gym = view.findViewById(R.id.featured_recycler);
        Rcv_Pt = view.findViewById(R.id.mostviewed_recycler);
        Rcv_Combo = view.findViewById(R.id.category_recycler);

        getGym();
        getTrainer();
        getCombo();


        // xem thêm
        TextView moreGym = view.findViewById(R.id.moreGym);
        moreGym.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentGym());
                        fragmentTransaction.addToBackStack("Fragment home");
                        fragmentTransaction.commit();
                    }
                }
        );


        TextView morePT = view.findViewById(R.id.morePT);
        morePT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentTrainer());
                fragmentTransaction.addToBackStack("Fragment home");
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void getGym() {
        Rcv_Gym.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        Rcv_Gym.setLayoutManager(staggeredGridLayoutManager);
        //Get data via Api
        Home methods = RetrofitClient.getRetrofit().create(Home.class);
        Call<List<Gym>> callGym = methods.getGym();
        callGym.enqueue(new Callback<List<Gym>>() {
            @Override
            public void onResponse(Call<List<Gym>> call, Response<List<Gym>> response) {
                System.out.println(response.code());
                adapter = new GymHomeApdapter(response.body(), new GymHomeApdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(Gym position) {
                        Fragment fragment;
                        mainActivity = (MainActivity) getActivity();
                        if(mainActivity.acc.getRole().equals("ADMIN"))
                            fragment = new FragmentGymDetailAdmin(position);
                        else
                            fragment = new FragmentGymDetail(position);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                        fragmentTransaction.addToBackStack("Fragment home");
                        fragmentTransaction.commit();
                    }
                });
                Rcv_Gym.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Gym>> call, Throwable t) {

            }
        });

    }

    private void getTrainer() {
        Rcv_Pt.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        Rcv_Pt.setLayoutManager(staggeredGridLayoutManager);
        //Get data via Api
        Home methods = RetrofitClient.getRetrofit().create(Home.class);
        Call<List<Trainer>> callPT = methods.getPT();
        callPT.enqueue(new Callback<List<Trainer>>() {
            @Override
            public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response) {
                adapter = new PTHomeAdapter(response.body(), new PTHomeAdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(Trainer position) {
                        Fragment fragment;
                        mainActivity = (MainActivity) getActivity();
                        if(mainActivity.acc.getRole().equals("ADMIN"))
                            fragment = new FragmentPTDetailAdmin(position);
                        else
                            fragment = new FragmentPtDetail(position);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView,fragment );
                        fragmentTransaction.addToBackStack("Fragment home");
                        fragmentTransaction.commit();
                    }
                });
                Rcv_Pt.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Trainer>> call, Throwable t) {

            }
        });

    }

    private void getCombo() {
        Rcv_Combo.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        Rcv_Combo.setLayoutManager(staggeredGridLayoutManager);
        //Get data via Api
        Home methods = RetrofitClient.getRetrofit().create(Home.class);
        Call<List<combo>> callCombo = methods.getCombo();
        callCombo.enqueue(new Callback<List<combo>>() {
            @Override
            public void onResponse(Call<List<combo>> call, Response<List<combo>> response) {
                adapter = new ComboAdapter(R.layout.item_combo_home, response.body(), new ComboAdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(combo position) {
                        Fragment fragment;
                        mainActivity = (MainActivity) getActivity();
                        if(mainActivity.acc.getRole().equals("ADMIN"))
                            fragment = new FragmentComboDetailAdmin(position);
                        else
                            fragment =  new FragmentComboDetail(position);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
                        fragmentTransaction.addToBackStack("Fragment home");
                        fragmentTransaction.commit();
                    }
                });
                Rcv_Combo.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<combo>> call, Throwable t) {

            }
        });

    }
}