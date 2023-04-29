package com.example.concentrationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    Button leaderBoardBtn;
    Button settingsBtn;
    SwitchCompat onOffSwitch;

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = this.getSharedPreferences("MUSIC", 0);
        editor = preferences.edit();

        music = MediaPlayer.create(this, R.raw.bg_music);
        music.setLooping(true);
        music.start();

        playBtn = findViewById(R.id.playBtn);

        playBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameModeActivity.class);
            startActivity(intent);
        });

        leaderBoardBtn = findViewById(R.id.leaderBoardBtn);

        leaderBoardBtn.setOnClickListener(view -> showLeaderDialog());

        settingsBtn = findViewById(R.id.settingsBtn);

        settingsBtn.setOnClickListener(view -> showSettingsDialog());
    }

    protected void showLeaderDialog()
    {
        Dialog leaderDialog = new Dialog(this, R.style.DialogStyle);
        leaderDialog.setContentView(R.layout.layout_leaderboard_dialog);
        RecyclerView recyclerView = leaderDialog.findViewById(R.id.recycler);
        List<LeaderboardItem> items = new ArrayList<LeaderboardItem>();

        SharedPreferences sharedPrefs = getSharedPreferences("scores", MODE_PRIVATE);
        Map<String, ?> scores = sharedPrefs.getAll();
        for(Map.Entry<String,?> entry: scores.entrySet()){
            String name = entry.getKey();
            Object score = entry.getValue();
            items.add(new LeaderboardItem(name, (Integer)score));
        }

        items.add(new LeaderboardItem("Jeremy", 7));

        //sorts leaderboard by highest score
        Collections.sort(items, new Comparator<LeaderboardItem>(){
            @Override
            public int compare(LeaderboardItem item2, LeaderboardItem item1){
                if(item1.getScore() < item2.getScore())
                    return -1;
                else if(item1.getScore() == item2.getScore())
                    return 0;
                else
                    return 1;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LeaderboardAdapter(this, items));

        leaderDialog.show();
        leaderDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    protected void showSettingsDialog()
    {
        Dialog settingsDialog = new Dialog(this, R.style.DialogStyle);
        settingsDialog.setContentView(R.layout.layout_settings_dialog);
        settingsDialog.show();
        settingsDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        onOffSwitch = settingsDialog.findViewById(R.id.onOffSwitch);

        if(onOffSwitch != null)
        {
            onOffSwitch.setChecked(true);
        }
        onOffSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                music.start();
                editor.putBoolean("ON", true).commit();
            } else {
                music.pause();
                editor.putBoolean("ON", false).commit();
            }
        });

        // Start or pause music based on the switch state
        boolean isMusicOn = preferences.getBoolean("ON", true);
        onOffSwitch.setChecked(isMusicOn);
        if (isMusicOn) {
            music.start();
        } else {
            music.pause();
        }
    }
}