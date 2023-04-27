package com.example.concentrationgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class EasyActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_easy);

        // Find the score TextView by ID
        GridLayout gridLayout = findViewById(R.id.easy_grid_layout);
        scoreTextView = findViewById(R.id.playerScore);
        Button newGameBtn = findViewById(R.id.quit_btnE);


        int numCol = getIntent().getIntExtra("numColumns", 0);
        int numRow = getIntent().getIntExtra("numRows", 0);

        numElements = numCol * numRow;

        buttons = new CardButton[numElements];

        cards = new int[numElements / 2];

        cards[0] = R.drawable.chamber;
        cards[1] = R.drawable.sova;

        cardLocation = new int[numElements];

        shuffleCards();

        // Assigns cards into the girdLayout
        for(int r = 0; r < numRow; r++)
        {
            for(int c = 0; c < numRow; c++ )
            {
                CardButton tempBtn = new CardButton(this, r, c, cards[ cardLocation[r * numCol + c]]);
                tempBtn.setId(View.generateViewId()); //creates temp ID for android to reference back to
                tempBtn.setOnClickListener(this); // setOnClickListener for our onClick() function
                buttons[r * numCol + c] = tempBtn; // stores buttons for future uses
                gridLayout.addView(tempBtn);
            }
        }

        newGameBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EasyActivity.this);
            builder.setMessage("Are you sure you want to start a new game?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        flipAllCards();
                        Handler handler = new Handler();
                        handler.postDelayed(this::recreate, 5000);
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    private void flipAllCards() {
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
                Toast.makeText(this, "You Win", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(this::recreate, 3000);
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
