package com.example.dashboardmodern.Fragment.Client;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dashboardmodern.Activity.LoginActivity;
import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.Apdapter.CommentApdapter;
import com.example.dashboardmodern.Apdapter.UserImgAdapter;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.Comment;
import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.addGymComment;
import com.example.lib.Model.Request.addPtComment;
import com.example.lib.Model.Response.billPTResponse;
import com.example.lib.Model.Response.ptImgResponse;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Client;
import com.example.lib.Repository.Home;
import com.example.lib.RetrofitClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPtDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPtDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final String amount = "100000";
    private final String fee = "0";
    int environment = 0;//developer default
    private final String merchantName = "trong";
    private final String merchantCode = "MOMOWOWK20220503";
    private final String merchantNameLabel = "Nhà cung cấp";
    private final String description = "Thanh toán dịch vụ Gym";


    Client client;
    Home home;

    Trainer trainer ;

    public FragmentPtDetail() {

    }

    public FragmentPtDetail(Trainer trainer) {
        this.trainer = trainer;
    }

    public static FragmentPtDetail newInstance(String param1, String param2) {
        FragmentPtDetail fragment = new FragmentPtDetail();
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
        client = RetrofitClient.getRetrofit().create(Client.class);
        home = RetrofitClient.getRetrofit().create(Home.class);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pt_detail, container, false);

        ImageView gymPic = view.findViewById(R.id.gymPic);
        Picasso.get().load(trainer.getAvatar()).into(gymPic);

        TextView gymName = view.findViewById(R.id.gymName);
        gymName.setText(trainer.getGym().getName());

        TextView ptName = view.findViewById(R.id.ptName);
        ptName.setText(trainer.getName());

        TextView ptPhone = view.findViewById(R.id.ptPhone);
        ptPhone.setText(trainer.getPhone());

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        if(Float.toString(trainer.getRate()).equals("NaN"))
        ratingBar.setRating(5.0f);

        LinearLayout book = view.findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Admin methods = RetrofitClient.getRetrofit().create(Admin.class);
                if(mainActivity.acc != null){
                    Call<billPTResponse> checkPTExit = methods.checkPTExit(mainActivity.acc.getId());
                    checkPTExit.enqueue(new Callback<billPTResponse>() {
                        @Override
                        public void onResponse(Call<billPTResponse> call, Response<billPTResponse> response) {
                            if(response.body().getTrainer() !=null){
                                ShowMessage("Bạn đã có phòng Huấn Luyện Viên rồi mà........");
                            }
                            else {
                                Call<Boolean> checkout = methods.checkoutPT(mainActivity.acc.getId(),trainer.getId());
                                checkout.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        ShowMessage("Book thành công");
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        ShowMessage(t.getMessage());
                                        System.out.println(t.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<billPTResponse> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout payment = view.findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity.acc != null)
                    requestPayment();
                else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        RecyclerView comment_feedback_pt = view.findViewById(R.id.comment_feedback_pt);
        Call<List<Comment>> getComment = home.getJudgeByPT(trainer.getId());
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

        Button btn_new_feedback_pt = view.findViewById(R.id.btn_new_feedback_pt);
        btn_new_feedback_pt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity.acc != null){
                    openCommandDialog();
                }
                else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        RecyclerView rcv_img_detail_pt = view.findViewById(R.id.rcv_img_detail_pt);

        Call<List<ptImgResponse>> getImg = home.getByPt(trainer.getId());
        getImg.enqueue(new Callback<List<ptImgResponse>>() {
            @Override
            public void onResponse(Call<List<ptImgResponse>> call, Response<List<ptImgResponse>> response) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                rcv_img_detail_pt.setLayoutManager(gridLayoutManager);
                rcv_img_detail_pt.setAdapter(new UserImgAdapter(response.body()));
            }

            @Override
            public void onFailure(Call<List<ptImgResponse>> call, Throwable t) {

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

    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", 10000); //Kiểu integer
        eventValue.put("orderId", "123123123"); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", "0"); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(getActivity(), eventValue);


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
                Admin methods = RetrofitClient.getRetrofit().create(Admin.class);
                Call<String> addComment = client.addPtComment(new addPtComment(comment.getText().toString(),ratingBar.getRating(),trainer.getId(),mainActivity.acc.getId()));
                addComment.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Call<List<Comment>> getComment = home.getJudgeByPT(trainer.getId());
                        getComment.enqueue(new Callback<List<Comment>>() {
                            @Override
                            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentPtDetail(trainer));
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