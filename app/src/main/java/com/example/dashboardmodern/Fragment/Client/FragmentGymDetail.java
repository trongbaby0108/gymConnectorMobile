package com.example.dashboardmodern.Fragment.Client;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.Activity.LoginActivity;
import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.ComboAdapter;
import com.example.dashboardmodern.Apdapter.CommentApdapter;
import com.example.dashboardmodern.Apdapter.PTAdapter;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.Comment;
import com.example.lib.Model.Request.Gym;
import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.addGymComment;
import com.example.lib.Model.Request.addPtComment;
import com.example.lib.Model.Request.combo;
import com.example.lib.Repository.Client;
import com.example.lib.Repository.Home;
import com.example.lib.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentGymDetail extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Gym gym ;

    Client client;
    Home home;
    TextView name, address, phone ;
    ImageView img;
    RecyclerView rcvCombo,rcv_pt,rcv_Comment;
    RatingBar rate ;
    Button btn_open_dialog_center;


    public FragmentGymDetail() {
    }

    public FragmentGymDetail(Gym gym) {
        this.gym = gym;
    }


    public static FragmentGymDetail newInstance(String param1, String param2) {
        FragmentGymDetail fragment = new FragmentGymDetail();
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
        View view =  inflater.inflate(R.layout.fragment_gym_detail, container, false);
        name = view.findViewById(R.id.gymName);
        address = view.findViewById(R.id.gymAddress);
        phone = view.findViewById(R.id.gymPhone);
        img = view.findViewById(R.id.gymPic);
        rate = view.findViewById(R.id.ratingBar);
        rcv_Comment = view.findViewById(R.id.rcv_Comment);
        btn_open_dialog_center = view.findViewById(R.id.btn_open_dialog_center);

        name.setText("Tên: "+ gym.getName());
        address.setText("Địa chỉ: "+ gym.getAddress());
        phone.setText("Sdt: "+ gym.getPhone());
        Picasso.get().load(gym.getAvatar()).into(img);
        rate.setRating(gym.getRate());

        rcvCombo = view.findViewById(R.id.rcv_combo);
        rcv_pt = view.findViewById(R.id.rcv_pt);
        client = RetrofitClient.getRetrofit().create(Client.class);
        home = RetrofitClient.getRetrofit().create(Home.class);
        Call<List<combo>> getCombo = home.getComboByGym(gym.getId());
        getCombo.enqueue(new Callback<List<combo>>() {
            @Override
            public void onResponse(Call<List<combo>> call, Response<List<combo>> response) {
                if(response.body() != null){
                    ComboAdapter comboAdapter = new ComboAdapter(R.layout.item_combo, response.body(), new ComboAdapter.OnNoteListener() {
                        @Override
                        public void onNoteClick(combo position) {
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentComboDetail(position));
                            fragmentTransaction.addToBackStack("Fragment home");
                            fragmentTransaction.commit();
                        }
                    });
                    rcvCombo.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
                    rcvCombo.setLayoutManager(linearLayoutManager);
                    rcvCombo.setAdapter(comboAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<combo>> call, Throwable t) {

            }
        });

        Call<List<Trainer>> getPT = home.getPTByGym(gym.getId());
        getPT.enqueue(new Callback<List<Trainer>>() {
            @Override
            public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response) {
                PTAdapter ptAdapter = new PTAdapter(response.body(), new PTAdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(Trainer position) {

                    }
                });
                rcv_pt.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false);
                rcv_pt.setLayoutManager(linearLayoutManager);
                rcv_pt.setAdapter(ptAdapter);
            }
            @Override
            public void onFailure(Call<List<Trainer>> call, Throwable t) {

            }
        });

        btn_open_dialog_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity.acc != null){
                    openCommandDialog();
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        Call<List<Comment>> getComment = home.getJudgeByGym(gym.getId());
        getComment.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                CommentApdapter commentApdapter = new CommentApdapter(response.body());
                rcv_Comment.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
                rcv_Comment.setLayoutManager(linearLayoutManager);
                rcv_Comment.setAdapter(commentApdapter);
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });

        return view ;
    }

    private void openCommandDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_feedback);

        Window window = dialog.getWindow();
        if(window == null){
            return ;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);

        TextView comment = dialog.findViewById(R.id.editTextFeedBack);
        RatingBar ratingBar = dialog.findViewById(R.id.rating);
        MainActivity mainActivity = (MainActivity) getActivity();
        Button btn_send = dialog.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Call<String> addComment = client.addGymComment("Bearer "+mainActivity.jwt,new addGymComment(comment.getText().toString(),ratingBar.getRating(),gym.getId(),mainActivity.acc.getId()));
                    addComment.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Call<List<Comment>> getComment = home.getJudgeByGym(gym.getId());
                            getComment.enqueue(new Callback<List<Comment>>() {
                                @Override
                                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentGymDetail(gym));
                                    fragmentTransaction.addToBackStack("Fragment home");
                                    fragmentTransaction.commit();
                                    dialog.cancel();
                                }

                                @Override
                                public void onFailure(Call<List<Comment>> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
            }
        });
        Button btnNoThanks = dialog.findViewById(R.id.btn_no_thanks);
        btnNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}