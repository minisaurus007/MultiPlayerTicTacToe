package edu.oakland.cse523.multiplayertictactoe;

/**
 * Created by oakland on 11/2/2014.
 */
public interface Observable {

    public void registerObserver(Observer o);
    public void notifyListeners();
    public void deregisterObserver(Observer o);

}
