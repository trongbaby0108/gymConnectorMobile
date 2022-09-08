package com.example.dashboardmodern.Apdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.R;
import com.example.lib.Model.Trainer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PTHomeAdapter extends RecyclerView.Adapter<PTHomeAdapter.PTViewHolder> {

    List<Trainer> trainers;
    PTHomeAdapter.OnNoteListener mListener ;
    public PTHomeAdapter(List<Trainer> trainers,PTHomeAdapter.OnNoteListener mListener) {
        this.trainers = trainers;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public PTHomeAdapter.PTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pt_home,parent,false);
        PTHomeAdapter.PTViewHolder PtViewHolder = new PTHomeAdapter.PTViewHolder(view);
        return PtViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PTHomeAdapter.PTViewHolder holder, int position) {
        Trainer trainer = trainers.get(position);
        Picasso.get().load(trainer.getAvatar()).into(holder.image);
        holder.title.setText(trainer.getName());
        holder.desc.setText(trainer.getAddress());
        holder.rate.setRating(trainer.getRate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNoteClick(trainer);
            }
        });
    }


    @Override
    public int getItemCount() {
        return trainers.size();
    }

    public static class PTViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, desc;
        RatingBar rate;
        CardView cardView;

        public PTViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.mv_image);
            title = itemView.findViewById(R.id.mv_title);
            desc = itemView.findViewById(R.id.mv_desc);
            rate = itemView.findViewById(R.id.mv_rating);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

    public interface OnNoteListener{
        void onNoteClick(Trainer position);
    }
}
