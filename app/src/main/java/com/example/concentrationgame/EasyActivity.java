package com.example.concentrationgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class EasyActivity extends AppCompatActivity implements View.OnClickListener {

    private int numElements;
    private int[] cards;
    private int[] cardLocation;
    private int counter;
    private int playerScore;

    private int numCol;
    private int numRow;

    private CardButton[] buttons;
    private CardButton firstSelected;
    private CardButton secondSelected;

    private GridLayout gridLayout;

    private boolean isBusy = false;

    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_easy);

        // Find the score TextView by ID
        gridLayout = findViewById(R.id.easy_grid_layout);
        scoreTextView = findViewById(R.id.playerScore);
        Button newGameBtn = findViewById(R.id.quit_btnE);

        //set score
        playerScore = 0;
        scoreTextView.setText("Score: " + playerScore);

        numCol = getIntent().getIntExtra("numColumns", 0);
        numRow = getIntent().getIntExtra("numRows", 0);

        numElements = numCol * numRow;

        buttons = new CardButton[numElements];

        cards = new int[numElements / 2];

        cards[0] = R.drawable.chamber;
        cards[1] = R.drawable.sova;

        cardLocation = new int[numElements];

        shuffleCards();
        // Assigns cards into the gridLayout
        for(int r = 0; r < numRow; r++) {
            for (int c = 0; c < numRow; c++) {
                CardButton tempBtn = new CardButton(this, r, c, cards[cardLocation[r * numCol + c]]);
                tempBtn.setId(View.generateViewId()); //creates temp ID for android to reference back to
                tempBtn.setOnClickListener(this); // setOnClickListener for our onClick() function
                buttons[r * numCol + c] = tempBtn; // stores buttons for future uses
                gridLayout.addView(tempBtn);
            }
        }

        newGameBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EasyActivity.this);
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
            resetGame(0);
            dialog.dismiss();
        });

    }

    public void saveScore(String name, int score){
        SharedPreferences sharedPrefs = getSharedPreferences("scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(name + "E", score);
        //test clear method
        //editor.clear();
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



    protected void shuffleCards()
    {
        Random random = new Random();

        for(int i = 0; i < numElements; i++)
        {
            cardLocation[i] = i % (numElements / 2);
        }

        for(int i = 0; i < numElements; i++)
        {
            int temp = cardLocation[i];
            int swapIndex = random.nextInt(4);

            cardLocation[i] = cardLocation[swapIndex];

            cardLocation[swapIndex] = temp;

        }
    }

    @Override
    public void onClick(View view) {


        if (isBusy)
        {
            return;
        }

        CardButton card = (CardButton) view;

        //do nothing if clicking a card that is already matched
        if (card.isMatched() || card.isFlipped)
        {
            return;
        }

        if(firstSelected == null)
        {
            firstSelected = card;
            firstSelected.setFlipped();
            return;
        }


        if(firstSelected.getId() == card.getId())
        {
            return;
        }

        //Check if firstSelected card matches card selected if it is selected player gets points.
        // Counter to check that all cards were matched
        if(firstSelected.getFrontImage() == card.getFrontImage())
        {
            card.setFlipped();
            card.setMatched(true);
            firstSelected.setMatched(true);
            firstSelected.setEnabled(false);
            card.setEnabled(false);
            firstSelected = null;

            incrementScore();
            counter++;

            if (counter == 2)
            {
                //Toast.makeText(this, "You Win", Toast.LENGTH_LONG).show();
                showSaveScoreDialog();
            }
        }

        // if secondSelected is not matched then score is decremented and there is a delay before
        // both cards are flipped back and user will have a chance to select again
        else
        {
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
