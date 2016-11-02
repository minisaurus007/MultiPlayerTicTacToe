package edu.oakland.cse523.multiplayertictactoe;

import java.util.ArrayList;

/**
 * Created by oakland on 11/2/2014.
 */
public class DataCell implements Observable{
    private int symbol = R.drawable.images_background_game;
    ArrayList<Observer> observers = new ArrayList<Observer>();
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyListeners() {
        for (Observer o:observers)
        {
            o.update(symbol);
        }
    }

    @Override
    public void deregisterObserver(Observer o) {
        observers.remove(o);
    }
    public int getSymbol(){
        return this.symbol;
    }
    public void setSymbol(int symbol)
    {
        this.symbol = symbol;
        notifyListeners();
    }
    public boolean isDataCellMarked(){
        if(this.symbol== R.drawable.images_background_game)
            return false;
        else
            return  true;
    }

}
