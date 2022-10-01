package com.example.dashboardmodern.Fragment.Client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.GymApdapter;

import com.example.dashboardmodern.Fragment.Admin.FragmentGymDetailAdmin;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Gym;
import com.example.lib.Repository.Methods;
import com.example.lib.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentGym extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    public FragmentGym() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGym.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGym newInstance(String param1, String param2) {
        FragmentGym fragment = new FragmentGym();
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
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.navigationView.setCheckedItem(R.id.nav_gym);
        View view =  inflater.inflate(R.layout.fragment_gym, container, false);
        recyclerView = view.findViewById(R.id.ListGym);
        recyclerView.setHasFixedSize(true);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(0, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        Methods methods = RetrofitClient.getRetrofit().create(Methods.class);
        Call<List<Gym>> callGym = methods.getGym();
        callGym.enqueue(new Callback<List<Gym>>() {
            @Override
            public void onResponse(Call<List<Gym>> call, Response<List<Gym>> response) {
                System.out.println(response.code());
                GymApdapter adapter = new GymApdapter(response.body(), new GymApdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(Gym position) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        Fragment fragment ;
                        if(mainActivity.acc.getRole().equals("ADMIN"))
                            fragment = new FragmentGymDetailAdmin(position);
                        else
                            fragment = new FragmentGymDetail(position);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
                        fragmentTransaction.addToBackStack("Fragment home");
                        fragmentTransaction.commit();
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Gym>> call, Throwable t) {

            }
        });
        return view;
    }
}