package com.example.concentrationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

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


        int numCol = gridLayout.getColumnCount();
        int numRow = gridLayout.getRowCount();

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
        for(int r = 0; r < numRow; r++)
        {
            for(int c = 0; c < numRow; c++ )
            {
                CardButton tempBtn = new CardButton(this, r, c, cards[ cardLocation[r * numCol + c]]);
                tempBtn.setId(View.generateViewId());
                tempBtn.setOnClickListener(this);
                buttons[r * numCol + c] = tempBtn;
                gridLayout.addView(tempBtn);
            }
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
            int swapIndex = random.nextInt(8);

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

            if (counter == 8)
            {
                Toast.makeText(this, "You Win " + playerScore, Toast.LENGTH_LONG).show();
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
            }, 500);
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
