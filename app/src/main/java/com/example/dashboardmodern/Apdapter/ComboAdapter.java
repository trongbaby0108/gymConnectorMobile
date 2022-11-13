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
import com.example.lib.Model.Request.combo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {

    List<combo> combos;
    int resouces;
    ComboAdapter.OnNoteListener mListener;

    public ComboAdapter(int resouces,List<combo> combos, ComboAdapter.OnNoteListener mListener) {
        this.combos = combos;
        this.resouces= resouces;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ComboAdapter.ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resouces,parent,false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder holder, int position) {

        combo combo = combos.get(position);
        Picasso.get().load(combo.getGym().getAvatar()).into(holder.image);
        holder.title.setText(combo.getName());
        holder.price.setText(combo.getPrice() + " VNĐ");
        holder.gym.setText("Gym: "+combo.getGym().getName());
        holder.comboItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNoteClick(combo);
            }
        });
        if(holder.address != null) holder.address.setText("Địa chỉ: "+ combo.getGym().getAddress());
    }

    @Override
    public int getItemCount() {
        return combos.size();
    }

    public static class ComboViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,price,gym,address;
        CardView comboItem;

        public ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_combo);
            title = itemView.findViewById(R.id.comboName);
            price = itemView.findViewById(R.id.comboPrice);
            gym = itemView.findViewById(R.id.comboGymName);
            address = itemView.findViewById(R.id.comboWhere);
            comboItem = itemView.findViewById(R.id.itemCombo);

        }
    }
    public interface OnNoteListener{
        void onNoteClick(combo position);
    }
}
