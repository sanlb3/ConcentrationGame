package com.example.concentrationgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatDrawableManager;

public class CardButton extends androidx.appcompat.widget.AppCompatButton {

    protected int columns;
    protected int rows;
    protected int frontImage;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable frontView;
    protected Drawable backView;

    @SuppressLint("RestrictedApi")
    public CardButton (Context context, int row, int col, int frontImageID)
    {
        super(context);

        columns = col;
        rows = row;
        frontImage = frontImageID;

        frontView = AppCompatDrawableManager.get().getDrawable(context, frontImageID);
        backView = AppCompatDrawableManager.get().getDrawable(context, R.drawable.card_back_view);

        setBackground(backView);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(col), GridLayout.spec(rows));

        tempParams.width = (int) getResources().getDisplayMetrics().density *100;
        tempParams.height = (int) getResources().getDisplayMetrics().density *100;

        setLayoutParams(tempParams);

    }

    public boolean isMatched(){
        return isMatched;
    }

    public void setMatched(boolean matched)
    {
        isMatched = matched;
    }

    public int getFrontImage()
    {
        return frontImage;
    }

    public void setFlipped()
    {
        if (isMatched)
        {
            return;
        }
        if(isFlipped)
        {
            setBackground(backView);
            isFlipped = false;
        }
        else
        {
            setBackground(frontView);
            isFlipped = true;
        }
    }
}
