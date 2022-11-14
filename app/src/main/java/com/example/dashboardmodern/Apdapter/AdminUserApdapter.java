package com.example.dashboardmodern.Apdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.R;
import com.example.lib.Model.Response.userInfoResponse;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdminUserApdapter extends RecyclerView.Adapter<AdminUserApdapter.AdminUserViewHolder>  {

    private final List<userInfoResponse> userInfoResponses;
    private final OnNoteListener mlistener;

    public AdminUserApdapter(List<userInfoResponse> userInfoResponses, OnNoteListener mlistener) {
        this.userInfoResponses = userInfoResponses;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public AdminUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminUserApdapter.AdminUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin,null));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserViewHolder holder, int position) {
        userInfoResponse user = userInfoResponses.get(position);
        if(user.getAvatar() != null && !user.getAvatar().equals("")){
            Picasso.get().load(user.getAvatar()).into(holder.img_Avatar);
        }

        holder.txtInfo.setText("Email: " + user.getEmail());
        holder.txtName.setText("Tên: "+ user.getName());
        holder.Enable.setChecked(user.isEnable());
        holder.Enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mlistener.onNoteClick(user,b);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(userInfoResponses == null)
            return 0;
        else
            return userInfoResponses.size();
    }

    public static class AdminUserViewHolder extends RecyclerView.ViewHolder{

        TextView txtName,txtInfo;
        Switch Enable;
        ImageView img_Avatar;

        public AdminUserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            Enable = itemView.findViewById(R.id.Enable);
            img_Avatar = itemView.findViewById(R.id.img_Avatar);
        }
    }
    public interface OnNoteListener{
        void onNoteClick(userInfoResponse position, Boolean enable);
    }
}
