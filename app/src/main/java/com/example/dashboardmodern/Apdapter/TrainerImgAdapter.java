package com.example.dashboardmodern.Apdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lib.Model.Trainer;
import com.example.dashboardmodern.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrainerImgAdapter extends RecyclerView.Adapter<TrainerImgAdapter.TrainerImgViewHolder>{

    public List<Trainer> mListTrainer;

    public TrainerImgAdapter(List<Trainer> mListTrainer) {
        this.mListTrainer = mListTrainer;
    }

    public TrainerImgAdapter() {
    }

    @NonNull
    @Override
    public TrainerImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer,parent,false);
        return new TrainerImgAdapter.TrainerImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerImgViewHolder holder, int position) {
        Trainer trainer  = mListTrainer.get(position);
        if(trainer==null){
            return;
        }
        Picasso.get().load(trainer.getAvatar()).into(holder.imgUser);
        holder.tvName.setText(trainer.getName());
    }

    @Override
    public int getItemCount() {
        if(mListTrainer != null){
            return mListTrainer.size();
        }
        return 0;
    }

    public class TrainerImgViewHolder extends RecyclerView.ViewHolder{

        ImageView imgUser;
        TextView tvName;

        public TrainerImgViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_gym);
            tvName = itemView.findViewById(R.id.tv_trainer);
        }
    }
}
