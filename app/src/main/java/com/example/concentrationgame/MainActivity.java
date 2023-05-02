package com.example.concentrationgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button playBtn;
    private Button leaderBoardBtn;
    private Button settingsBtn;
    private Button exitBtn;
    private SwitchCompat onOffSwitch;
    private MusicService musicService;
    private boolean isServiceRunning;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent musicServiceIntent = new Intent(this, MusicService.class);
        bindService(musicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);

        isServiceRunning = true;
        preferences = this.getSharedPreferences("MUSIC", 0);
        editor = preferences.edit();

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

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
            musicService = binder.getService();
            boolean isMusicOn = preferences.getBoolean("ON", true);
            if (isMusicOn) {
                musicService.startMusic();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    protected void showLeaderDialog() {
        Dialog leaderDialog = new Dialog(this, R.style.DialogStyle);
        leaderDialog.setContentView(R.layout.layout_leaderboard_dialog);
        RecyclerView recyclerView = leaderDialog.findViewById(R.id.recycler);
        List<LeaderboardItem> items = new ArrayList<>();

        SharedPreferences sharedPrefs = getSharedPreferences("scores", MODE_PRIVATE);
        Map<String, ?> scores = sharedPrefs.getAll();
        for (Map.Entry<String, ?> entry : scores.entrySet()) {
            String name = entry.getKey();
            Object score = entry.getValue();
            items.add(new LeaderboardItem(name, (Integer) score));
        }

        //test add
        //items.add(new LeaderboardItem("Jeremy", 7));

        //sorts leaderboard by highest score
        Collections.sort(items, new Comparator<LeaderboardItem>() {
            @Override
            public int compare(LeaderboardItem item2, LeaderboardItem item1) {
                if (item1.getScore() < item2.getScore())
                    return -1;
                else if (item1.getScore() == item2.getScore())
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
    protected void showSettingsDialog() {
        Dialog settingsDialog = new Dialog(this, R.style.DialogStyle);
        settingsDialog.setContentView(R.layout.layout_settings_dialog);
        settingsDialog.show();
        settingsDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);

        onOffSwitch = settingsDialog.findViewById(R.id.onOffSwitch);
        exitBtn = settingsDialog.findViewById(R.id.exit_button);

        if (onOffSwitch != null) {
            onOffSwitch.setChecked(true);
        }
        onOffSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                musicService.startMusic();
                editor.putBoolean("ON", true).commit();
            } else {
                musicService.stopMusic();
                editor.putBoolean("ON", false).commit();
            }
        });

        exitBtn.setOnClickListener( view ->
        {
            settingsDialog.dismiss();
        });


        boolean isMusicOn = preferences.getBoolean("ON", true);
        onOffSwitch.setChecked(isMusicOn);
        if (isMusicOn) {
            musicService.startMusic();
        } else {
            musicService.stopMusic();
        }
    }
}