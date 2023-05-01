package com.example.concentrationgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<MyViewHolder>{
    Context context;
    List<LeaderboardItem> items;

    public LeaderboardAdapter(Context context, List<LeaderboardItem> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String nameDiff = items.get(position).getName();
        int nameLength = nameDiff.length();
        holder.textName.setText(nameDiff.substring(0, nameLength - 1));
        holder.textScore.setText(Integer.toString(items.get(position).getScore()) + " " + nameDiff.charAt(nameLength - 1));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
