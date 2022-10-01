package com.example.dashboardmodern.Apdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.R;
import com.example.lib.Model.userImg;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserImgAdapter extends RecyclerView.Adapter<UserImgAdapter.UserImgViewHolder> {

    public List<userImg> mListUser;
    public void setData(List<userImg> list){
        this.mListUser = list;
        notifyDataSetChanged();
    }

    public UserImgAdapter(List<userImg> mListUser) {
        this.mListUser = mListUser;
    }

    @NonNull
    @Override
    public UserImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img,parent,false);
        return new UserImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserImgViewHolder holder, int position) {
        userImg userImg  = mListUser.get(position);
        if(userImg==null){
            return;
        }
        Picasso.get().load(userImg.getImg()).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        if(mListUser != null){
            return mListUser.size();
        }
        return 0;
    }

    public class UserImgViewHolder extends RecyclerView.ViewHolder{
        ImageView imgUser;
        public UserImgViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img);
        }
    }
}
