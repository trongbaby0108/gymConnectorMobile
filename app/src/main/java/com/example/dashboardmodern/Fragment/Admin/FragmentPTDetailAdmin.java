package com.example.dashboardmodern.Fragment.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.CommentApdapter;
import com.example.dashboardmodern.Apdapter.UserImgAdapter;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.Comment;
import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.userImg;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Home;
import com.example.lib.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPTDetailAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPTDetailAdmin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Trainer position;

    public FragmentPTDetailAdmin() {
        // Required empty public constructor
    }

    public FragmentPTDetailAdmin(Trainer position) {
        this.position = position;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPTDetailAdmin.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPTDetailAdmin newInstance(String param1, String param2) {
        FragmentPTDetailAdmin fragment = new FragmentPTDetailAdmin();
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
        MainActivity mainActivity = (MainActivity) getActivity();
        Admin admin = RetrofitClient.getRetrofit().create(Admin.class);
        Home home = RetrofitClient.getRetrofit().create(Home.class);
        View view = inflater.inflate(R.layout.fragment_p_t_detail_admin, container, false);
        RecyclerView comment_feedback_pt = view.findViewById(R.id.comment_feedback_pt);
        Call<List<Comment>> getComment = home.getJudgeByPT(position.getId());
        getComment.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.body()!=null){
                    CommentApdapter commentApdapter = new CommentApdapter(response.body());
                    comment_feedback_pt.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
                    comment_feedback_pt.setLayoutManager(linearLayoutManager);
                    comment_feedback_pt.setAdapter(commentApdapter);
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });

        RecyclerView rcv_img_detail_pt = view.findViewById(R.id.rcv_img_detail_pt);


        Call<List<userImg>> getImg = home.getByPt(position.getId());
        getImg.enqueue(new Callback<List<userImg>>() {
            @Override
            public void onResponse(Call<List<userImg>> call, Response<List<userImg>> response) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                rcv_img_detail_pt.setLayoutManager(gridLayoutManager);
                rcv_img_detail_pt.setAdapter(new UserImgAdapter(response.body()));
            }

            @Override
            public void onFailure(Call<List<userImg>> call, Throwable t) {

            }
        });

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setRating(position.getRate());

        ImageView gymPic = view.findViewById(R.id.gymPic);
        Picasso.get().load(position.getAvatar()).into(gymPic);

        Button btn_disable =  view.findViewById(R.id.btn_disable);
        btn_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> disable = admin.disablePT(mainActivity.jwt,position.getId());
                disable.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        ShowMessage("Khóa tài khoản thành công");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        Button btn_enable =  view.findViewById(R.id.btn_enable);
        btn_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> enable = admin.enablePT(mainActivity.jwt,position.getId());
                enable.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        ShowMessage("Mở khóa tài khoản thành công");
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