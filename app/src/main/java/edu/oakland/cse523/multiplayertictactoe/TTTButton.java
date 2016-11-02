package edu.oakland.cse523.multiplayertictactoe;

import android.content.Context;
import android.widget.ImageButton;


public class TTTButton extends ImageButton implements Observer {
    int index;
    public TTTButton(Context context) {
        super(context);
    }

    @Override
    public void update(int symbol) {
        this.setImageResource(symbol);
    }
}
