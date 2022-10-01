package com.example.dashboardmodern.Fragment.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.PTAdapter;
import com.example.dashboardmodern.Fragment.Admin.FragmentPTDetailAdmin;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Trainer;
import com.example.lib.Repository.Methods;
import com.example.lib.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTrainer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTrainer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rcvTrainer;
    private PTAdapter PTAdapter;
    View view;



    public FragmentTrainer() {
    }

    public static FragmentTrainer newInstance(String param1, String param2) {
        FragmentTrainer fragment = new FragmentTrainer();
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
        view = inflater.inflate(R.layout.fragment_trainer, container, false);
        rcvTrainer = view.findViewById(R.id.ListPt);
        LinearLayoutManager LinearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        rcvTrainer.setLayoutManager(LinearLayout);
        MainActivity mainActivity = (MainActivity) getActivity();
        Methods methods = RetrofitClient.getRetrofit().create(Methods.class);
        if(!mainActivity.acc.getRole().equals("ADMIN")) {
            Call<List<Trainer>> callPT = methods.getPT();
            callPT.enqueue(new Callback<List<Trainer>>() {
                @Override
                public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response) {
                    PTAdapter = new PTAdapter(response.body(), new PTAdapter.OnNoteListener() {
                        @Override
                        public void onNoteClick(Trainer position) {
                            Fragment fragment ;
                            if(mainActivity.acc.getRole().equals("ADMIN"))
                                fragment = new FragmentPTDetailAdmin(position);
                            else
                                fragment = new FragmentPtDetail(position);

                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                            fragmentTransaction.addToBackStack("Fragment home");
                            fragmentTransaction.commit();
                        }
                    });
                    rcvTrainer.setAdapter(PTAdapter);
                }

                @Override
                public void onFailure(Call<List<Trainer>> call, Throwable t) {

                }
            });
        }
        else{
            Call<List<Trainer>> callPT = methods.getALlPTAdmin("Bearer "+ mainActivity.jwt);
            callPT.enqueue(new Callback<List<Trainer>>() {
                @Override
                public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response) {
                    PTAdapter = new PTAdapter(response.body(), new PTAdapter.OnNoteListener() {
                        @Override
                        public void onNoteClick(Trainer position) {
                            Fragment fragment ;
                            if(mainActivity.acc.getRole().equals("ADMIN"))
                                fragment = new FragmentPTDetailAdmin(position);
                            else
                                fragment = new FragmentPtDetail(position);

                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                            fragmentTransaction.addToBackStack("Fragment home");
                            fragmentTransaction.commit();
                        }
                    });
                    rcvTrainer.setAdapter(PTAdapter);
                }

                @Override
                public void onFailure(Call<List<Trainer>> call, Throwable t) {

                }
            });
        }
        return view;
    }
}