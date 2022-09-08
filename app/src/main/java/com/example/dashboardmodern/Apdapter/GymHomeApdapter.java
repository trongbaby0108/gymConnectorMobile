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
import com.example.lib.Model.Gym;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GymHomeApdapter extends RecyclerView.Adapter<GymHomeApdapter.GymHomeViewHolder> {

    List<Gym> gyms;
    OnNoteListener mlistener;
    public GymHomeApdapter(List<Gym> gyms , OnNoteListener mlistener) {
        this.gyms = gyms;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public GymHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design,parent,false);
        return new GymHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymHomeViewHolder holder, int position) {

        Gym gym = gyms.get(position);
        Picasso.get().load(gym.getAvatar()).into(holder.image);
        holder.title.setText(gym.getName());
        holder.desc.setText(gym.getAddress());
        holder.rating.setRating(gym.getRate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onNoteClick(gym);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gyms.size();
    }

    public static class GymHomeViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, desc;
        CardView cardView ;
        RatingBar rating ;
        public GymHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            desc = itemView.findViewById(R.id.featured_desc);
            cardView = itemView.findViewById(R.id.cardView);
            rating = itemView.findViewById(R.id.rating);
        }
    }


    public interface OnNoteListener{
        void onNoteClick(Gym position);
    }
}










