package com.example.concentrationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

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

        easy.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameModeActivity.this, EasyActivity.class);
            startActivity(intent);

            GridLayout gridLayout = findViewById(R.id.easy_grid_layout);

            int numColumns = gridLayout.getColumnCount();
            int numRows = gridLayout.getRowCount();
        });

        moderate.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameModeActivity.this, ModerateActivity.class);
            startActivity(intent);

            GridLayout gridLayout = findViewById(R.id.easy_grid_layout);

            int numColumns = gridLayout.getColumnCount();
            int numRows = gridLayout.getRowCount();
        });
    }
}