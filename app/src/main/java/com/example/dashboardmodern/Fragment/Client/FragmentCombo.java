package com.example.dashboardmodern.Fragment.Client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.ComboAdapter;
import com.example.dashboardmodern.Fragment.Admin.FragmentComboDetailAdmin;
import com.example.dashboardmodern.R;
import com.example.lib.Model.combo;
import com.example.lib.Repository.Methods;
import com.example.lib.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCombo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCombo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCombo() {
        // Required empty public constructor
    }


    public static FragmentCombo newInstance(String param1, String param2) {
        FragmentCombo fragment = new FragmentCombo();
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
        View view =  inflater.inflate(R.layout.fragment_combo, container, false);
        RecyclerView rcv_combo = view.findViewById(R.id.rcv_combo);

        rcv_combo.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rcv_combo.setLayoutManager(staggeredGridLayoutManager);
        //Get data via Api
        Methods methods = RetrofitClient.getRetrofit().create(Methods.class);
        Call<List<combo>> callCombo = methods.getCombo();
        callCombo.enqueue(new Callback<List<combo>>() {
            @Override
            public void onResponse(Call<List<combo>> call, Response<List<combo>> response) {
                ComboAdapter adapter = new ComboAdapter(R.layout.item_combo, response.body(), new ComboAdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(combo position) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        Fragment fragment ;
                        if(mainActivity.acc.getRole().equals("ADMIN"))
                            fragment = new FragmentComboDetailAdmin(position);
                        else
                            fragment = new FragmentComboDetail(position);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                        fragmentTransaction.addToBackStack("Fragment home");
                        fragmentTransaction.commit();
                    }
                });
                rcv_combo.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<combo>> call, Throwable t) {

            }
        });
        return view ;
    }
}