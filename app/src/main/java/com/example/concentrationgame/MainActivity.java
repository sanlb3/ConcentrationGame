package com.example.concentrationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    Button leaderBoardBtn;
    Button settingsBtn;
    Switch onOffSwitch;

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        music = MediaPlayer.create(this, R.raw.bg_music);
        music.setLooping(true);

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

        preferences = this.getSharedPreferences("MUSIC", 0);
        editor = preferences.edit();

        onOffSwitch = findViewById(R.id.onOffSwitch);

        if(onOffSwitch != null) {
            music.start();
            onOffSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    music.start();
                    editor.putBoolean("ON", true).commit();
                } else {
                    music.pause();
                    editor.putBoolean("ON", false).commit();
                }
            });
        }
    }
}