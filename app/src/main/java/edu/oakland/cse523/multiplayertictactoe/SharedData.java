package edu.oakland.cse523.multiplayertictactoe;


public class SharedData{
    private static SharedData _sharedataobj;
    private Player[] players;

    private SharedData(){
        players = new Player[2];
        players[1]= new Player("Arthur",R.drawable.cubs1,"55545556");
    }
    public static SharedData getSharedData()
    {
        if(_sharedataobj == null)
            _sharedataobj = new SharedData();

        return _sharedataobj;

    }
    public Player[] getPlayers()
    {
        return players;
    }

}
