package com.example.concentrationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class GameModeActivity extends AppCompatActivity {

    private Button easy;
    private Button moderate;
    private Button hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        easy = findViewById(R.id.easyBtn);
        moderate = findViewById(R.id.moderateBtn);
        hard = findViewById(R.id.hardBtn);

        easy.setOnClickListener(view -> {
            Intent intent = new Intent(GameModeActivity.this, EasyActivity.class);
            int numColumns = 2;
            int numRows = 2;
            intent.putExtra("numColumns", numColumns);
            intent.putExtra("numRows", numRows);
            startActivity(intent);
        });

        moderate.setOnClickListener(view -> {
            Intent intent = new Intent(GameModeActivity.this, ModerateActivity.class);
            int numColumns = 4;
            int numRows = 4;
            intent.putExtra("numColumns", numColumns);
            intent.putExtra("numRows", numRows);
            startActivity(intent);
        });

        hard.setOnClickListener(view -> {
            Intent intent = new Intent(GameModeActivity.this, HardActivity.class);
            int numColumns = 6;
            int numRows = 6;
            intent.putExtra("numColumns", numColumns);
            intent.putExtra("numRows", numRows);
            startActivity(intent);
        });
    }
}