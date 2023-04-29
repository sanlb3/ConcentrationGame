package com.example.concentrationgame;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView textName, textScore;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.score_name);
        textScore = itemView.findViewById(R.id.score_value);
    }
}
