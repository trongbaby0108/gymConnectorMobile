package com.example.dashboardmodern.Apdapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.Trainer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PTAdapter extends RecyclerView.Adapter<PTAdapter.PTViewHolder>{
    List<Trainer> trainers;
    PTAdapter.OnNoteListener mlistener;

    public PTAdapter(List<Trainer> trainers, OnNoteListener mlistener) {
        this.trainers = trainers;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public PTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer,parent,false);
        return new PTAdapter.PTViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PTViewHolder holder, int position) {
        Trainer trainer = trainers.get(position);
        Picasso.get().load(trainer.getAvatar()).into(holder.image);
        holder.name.setText("Tên: "+trainer.getName());
        holder.address.setText("Địa chỉ: "+ trainer.getAddress());
        holder.phone.setText("SDT: "+trainer.getPhone());
        holder.email.setText("Email: "+trainer.getEmail());
        if(!trainer.isEnable()) holder.cardView.setBackgroundColor(Color.parseColor("#7f8c8d"));
        holder.rating.setRating(trainer.getRate());

        holder.itemPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onNoteClick(trainer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trainers.size();
    }


    public static class PTViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, address , email ,phone  ;
        RatingBar rating ;
        CardView itemPT ;
        ConstraintLayout cardView ;

        public PTViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_gym);

            name = itemView.findViewById(R.id.tv_trainer);
            email = itemView.findViewById(R.id.tv_email);
            phone = itemView.findViewById(R.id.tv_phone);
            address = itemView.findViewById(R.id.tv_address);
            cardView = itemView.findViewById(R.id.cardView);
            rating =  itemView.findViewById(R.id.mv_rating);
            itemPT = itemView.findViewById(R.id.itemPt);
        }
    }

    public interface OnNoteListener{
        void onNoteClick(Trainer position);
    }
}
