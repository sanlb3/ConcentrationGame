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
        holder.textName.setText(items.get(position).getName());
        holder.textScore.setText(Integer.toString(items.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
