package edu.oakland.cse523.multiplayertictactoe;

import android.telephony.SmsManager;

/**
 * Created by oakland on 11/2/2014.
 */
public class Player {
    public DataCell cells[] = new DataCell[16];
    public TTTButton[] buttons=new TTTButton[16];
    public String name;
    public int symbol;
    public String phone;

    public String getName(){
        return this.name;
    }
    public  void setName(String name){
        this.name= name;
    }
    public int getSymbol(){
        return this.symbol;

    }
    public void setSymbol(int symbol){
        this.symbol =symbol;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    Player(String _name, int s, String p)
    {
        name = _name;
        symbol = s;
        phone = p;
        for(int i = 0; i < 16; i++)
        {
            cells[i] = new DataCell();
        }
    }


    void MarkCell(int _index,Player[] player,int otherplayerindex)
    {
        cells[_index].setSymbol(this.getSymbol());
        int rows=0;
        for(int i=1;i<=4;i++){
            if(_index<4*i) {
                rows = i - 1;
                break;
            }
        }
        int cols = _index%4;
        SmsManager smsManager = SmsManager.getDefault();
        String msg = "Info:TTTGAME:SELECTED:("+String.valueOf(rows)+","+String.valueOf(cols)+")";
        smsManager.sendTextMessage(player[otherplayerindex].getPhone(),null,msg,null,null);

    }


    void register(Observer o, int _index)
    {
        cells[_index].registerObserver(o);
    }
    void unMarkCell(){
        for(int i =0;i<16;i++)
            cells[i].setSymbol(R.drawable.images_background_game);
    }
    void MarkCell(int index){
        cells[index].setSymbol(this.getSymbol());
    }
    void MarkCell(int index,int symbol){
        cells[index].setSymbol(symbol);
    }
}
