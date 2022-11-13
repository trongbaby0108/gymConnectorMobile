package com.example.dashboardmodern.Apdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboardmodern.R;
import com.example.lib.Model.Request.Gym;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GymApdapter  extends RecyclerView.Adapter<GymApdapter.GymViewHolder> {

    List<Gym> gyms;
    GymApdapter.OnNoteListener mlistener;

    public GymApdapter(List<Gym> gyms, GymApdapter.OnNoteListener mlistener) {
        this.gyms = gyms;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public GymApdapter.GymViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gym_item,parent,false);
        return new GymApdapter.GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymApdapter.GymViewHolder holder, int position) {
        Gym gym = gyms.get(position);
        Picasso.get().load(gym.getAvatar()).into(holder.image);
        holder.title.setText(gym.getName());
        holder.desc.setText(gym.getAddress());
        holder.rating.setRating(gym.getRate());

        holder.itemGym.setOnClickListener(new View.OnClickListener() {
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

    public static class GymViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, desc;
        RatingBar rating ;
        LinearLayout itemGym ;

        public GymViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_gym);
            title = itemView.findViewById(R.id.gymName);
            desc = itemView.findViewById(R.id.gymAddress);
            rating =  itemView.findViewById(R.id.mv_rating);
            itemGym = itemView.findViewById(R.id.itemGym);
        }
    }

    public interface OnNoteListener{
        void onNoteClick(Gym position);
    }
}
