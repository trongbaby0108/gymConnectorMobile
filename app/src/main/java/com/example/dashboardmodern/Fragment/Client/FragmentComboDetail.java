package com.example.dashboardmodern.Fragment.Client;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dashboardmodern.Activity.LoginActivity;
import com.example.dashboardmodern.Activity.MainActivity;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Response.billGymResponse;
import com.example.lib.Model.Request.combo;
import com.example.lib.Repository.Admin;
import com.example.lib.Repository.Client;
import com.example.lib.RetrofitClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentComboDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentComboDetail extends Fragment {

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


    public combo combo;

    public FragmentComboDetail() {
        // Required empty public constructor
    }

    public FragmentComboDetail(combo combo) {
        this.combo = combo;
    }

    public static FragmentComboDetail newInstance(String param1, String param2) {
        FragmentComboDetail fragment = new FragmentComboDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
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
        View view =  inflater.inflate(R.layout.fragment_combo_detail, container, false);
        ImageView gymImg = view.findViewById(R.id.gymImg);
        TextView comboName = view.findViewById(R.id.comboName);
        TextView comboPrice = view.findViewById(R.id.comboPrice);
        TextView comboDesc =  view.findViewById(R.id.comboDesc);
        LinearLayout Checkout = view.findViewById(R.id.Checkout);
        LinearLayout book = view.findViewById(R.id.book);
        Picasso.get().load(combo.getGym().getAvatar()).into(gymImg);
        comboName.setText("Tên Combo: "+combo.getName());
        comboDesc.setText("Địa chỉ: "+combo.getGym().getAddress());
        comboPrice.setText("Giá: "+ combo.getPrice());

        MainActivity mainActivity = (MainActivity) getActivity();
        Client client = RetrofitClient.getRetrofit().create(Client.class);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mainActivity.acc == null){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Call<billGymResponse> checkGymExit = client.checkGymExit("Bearer "+mainActivity.jwt,mainActivity.acc.getId());
                    checkGymExit.enqueue(new Callback<billGymResponse>() {
                        @Override
                        public void onResponse(Call<billGymResponse> call, Response<billGymResponse> response) {
                            if(response.body() !=null){
                                ShowMessage("Bạn đã có phòng gym rồi mà........");
                            }
                            else {
                                Call<Boolean> checkout = client.checkout("Bearer "+mainActivity.jwt,mainActivity.acc.getId(),combo.getGym().getId(),combo.getId());
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
                        public void onFailure(Call<billGymResponse> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                }
            }
        });

        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainActivity.acc == null){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else{
                    requestPayment();
                }
            }
        });

        return view ;
    }

    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put("merchantname", merchantName);
        eventValue.put("merchantcode", merchantCode);
        eventValue.put("amount", combo.getPrice());
        eventValue.put("orderId", "123123123");
        eventValue.put("orderLabel", "Mã đơn hàng");
        eventValue.put("merchantnamelabel", "Dịch vụ");
        eventValue.put("fee", "0");
        eventValue.put("description", description);
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", combo.getName());
            objExtraData.put("screen_code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(getActivity(), eventValue);


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