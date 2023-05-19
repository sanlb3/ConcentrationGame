package com.example.concentrationgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ModerateActivity extends AppCompatActivity implements View.OnClickListener {

    private int numElements;
    private int[] cards;
    private int[] cardLocation;
    private int counter;
    private int playerScore;

    private CardButton[] buttons;
    private CardButton firstSelected;
    private CardButton secondSelected;

    private boolean isBusy = false;
    private TextView scoreTextView;

    Button no;
    Button yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderate);

        scoreTextView = findViewById(R.id.score_textview);
        Button newGameBtn = findViewById(R.id.quit_btnM);
        GridLayout gridLayout = findViewById(R.id.moderate_grid_layout);

        //set score
        playerScore = 0;
        scoreTextView.setText("Score: " + playerScore);

        int numCol = getIntent().getIntExtra("numColumns", 0);
        int numRow = getIntent().getIntExtra("numRows", 0);

        numElements = numCol * numRow;

        buttons = new CardButton[numElements];

        cards = new int[numElements / 2];

        cards[0] = R.drawable.chamber;
        cards[1] = R.drawable.sova;
        cards[2] = R.drawable.gekko;
        cards[3] = R.drawable.killjoy;
        cards[4] = R.drawable.breach;
        cards[5] = R.drawable.phoenix;
        cards[6] = R.drawable.kayo;
        cards[7] = R.drawable.yoru;


        cardLocation = new int[numElements];

        shuffleCards();

        // Assigns cards into the girdLayout
        for (int r = 0; r < numRow; r++) {
            for (int c = 0; c < numRow; c++) {
                CardButton tempBtn = new CardButton(this, r, c, cards[cardLocation[r * numCol + c]]);
                tempBtn.setId(View.generateViewId());
                tempBtn.setOnClickListener(this);
                buttons[r * numCol + c] = tempBtn;
                gridLayout.addView(tempBtn);
            }
        }

        newGameBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ModerateActivity.this);
            builder.setMessage("Are you sure you want to quit and start a new game?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        flipAllCards();
                        Toast.makeText(this, "Here are the answers", Toast.LENGTH_SHORT).show();
                        resetGame(3000);
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    protected void showSaveScoreDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_save_score_dialog, null);

        TextView tv = customLayout.findViewById(R.id.final_score);
        tv.setText(Integer.toString(playerScore));

        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.show();

        Button save = customLayout.findViewById(R.id.save_button);
        save.setOnClickListener(view -> {
            //save input name if exists, else name Anonymous
            EditText inputText = customLayout.findViewById(R.id.input_name);
            String name = inputText.getText().toString();
            if(name.matches("")){
                name = "Anonymous";
            }

            //store name and score for leaderboard
            saveScore(name, playerScore);
            //reset game
            resetGame(2000);
            dialog.dismiss();
        });

    }

    public void saveScore(String name, int score){
        SharedPreferences sharedPrefs = getSharedPreferences("scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(name + "M", score);
        editor.apply();
    }

    protected void resetGame(int delay){
        Handler handler = new Handler();
        handler.postDelayed(this::recreate, delay);
    }

    private void flipAllCards() {
        isBusy = true;
        for (CardButton card : buttons) {
            card.setFlipped();
        }
    }

    protected void shuffleCards() {
        Random random = new Random();

        for (int i = 0; i < numElements; i++) {
            cardLocation[i] = i % (numElements / 2);
        }

        for (int i = 0; i < numElements; i++) {
            int temp = cardLocation[i];
            int swapIndex = random.nextInt(16);

            cardLocation[i] = cardLocation[swapIndex];

            cardLocation[swapIndex] = temp;

        }
    }

    @Override
    public void onClick(View view) {

        if (isBusy) {
            return;
        }

        CardButton card = (CardButton) view;

        //do nothing if clicking a card that is already matched
        if (card.isMatched() || card.isFlipped)
        {
            return;
        }

        if (firstSelected == null) {
            firstSelected = card;
            firstSelected.setFlipped();
            return;
        }


        if (firstSelected.getId() == card.getId()) {
            return;
        }

        //Check if firstSelected card matches card selected if it is selected player gets points.
        // Counter to check that all cards were matched
        if (firstSelected.getFrontImage() == card.getFrontImage()) {
            card.setFlipped();
            card.setMatched(true);

            firstSelected.setMatched(true);
            firstSelected.setEnabled(false);
            card.setEnabled(false);
            firstSelected = null;

            incrementScore();
            counter++;

            if (counter == 8) {
                //Toast.makeText(this, "You Win", Toast.LENGTH_LONG).show();
                //Handler handler = new Handler();
                //handler.postDelayed(this::recreate, 3000);
                showSaveScoreDialog();
            }
        }

        // if secondSelected is not matched then score is decremented and there is a delay before
        // both cards are flipped back and user will have a chance to select again
        else {
            secondSelected = card;
            secondSelected.setFlipped();
            decrementScore();

            isBusy = true;

            final Handler handler = new Handler();

            handler.postDelayed(() -> {
                secondSelected.setFlipped();
                firstSelected.setFlipped();
                firstSelected = null;
                isBusy = false;
            }, 1000);
        }
    }


    private int incrementScore() {
        playerScore = playerScore + 2;
        scoreTextView.setText("Score: " + playerScore);
        return playerScore;
    }

    private int decrementScore() {
        if (playerScore == 0) {
            playerScore = playerScore;
        } else {
            playerScore = playerScore - 1;
        }
        scoreTextView.setText("Score: " + playerScore);
        return playerScore;
    }
}