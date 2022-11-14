package com.example.dashboardmodern.Apdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.R;
import com.example.lib.Model.Response.userInfoResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import cz.msebera.android.httpclient.HttpStatus;
import okhttp3.HttpUrl;

public class UserPTAdapter extends RecyclerView.Adapter<UserPTAdapter.UserPTViewHolder> {

    private final List<userInfoResponse> userInfoResponses;
    UserPTAdapter.OnNoteListener mlistener;

    public UserPTAdapter(List<userInfoResponse> userInfoResponses) {
        this.userInfoResponses = userInfoResponses;
    }

    @NonNull
    @Override
    public UserPTAdapter.UserPTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserPTAdapter.UserPTViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,null));
    }

    @Override
    public void onBindViewHolder(@NonNull UserPTAdapter.UserPTViewHolder holder, int position) {
        userInfoResponse user = userInfoResponses.get(position);
        if(!user.getAvatar().equals(""))
            Picasso.get().load(user.getAvatar()).into(holder.imageView);
        holder.name.setText(user.getName());
        holder.address.setText("Địa chỉ:" + user.getAddress());
        holder.phone.setText("Số điện thoại:" + user.getPhone());
        holder.itemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onNoteClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfoResponses.size();
    }

    public static class UserPTViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name, address, phone;
        CardView itemUser;
        public UserPTViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.img_user);
            name = itemView.findViewById(R.id.tv_name);
            address = itemView.findViewById(R.id.tv_address);
            phone = itemView.findViewById(R.id.tv_phone);
            itemUser = itemView.findViewById(R.id.itemUserPt);
        }
    }

    public interface OnNoteListener{
        void onNoteClick(userInfoResponse position);
    }
}
