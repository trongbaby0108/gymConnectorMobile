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
import com.example.lib.Model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentApdapter extends RecyclerView.Adapter<CommentApdapter.CommentViewHolder> {

    List<Comment> comments ;

    public CommentApdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        CommentApdapter.CommentViewHolder CommentViewHolder = new  CommentApdapter.CommentViewHolder(view);
        return CommentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        if(!comment.getAvatar().equals(""))
            Picasso.get().load(comment.getAvatar()).into(holder.image);
        holder.name.setText(comment.getName());
        holder.comment.setText(comment.getContent());
        holder.ratingBar.setRating(comment.getVote());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,comment;
        RatingBar ratingBar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_Avatar);
            name = itemView.findViewById(R.id.txt_Name);
            comment = itemView.findViewById(R.id.txt_Comment);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }
}
