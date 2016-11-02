package edu.oakland.cse523.multiplayertictactoe;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;


public class SMSBroadcastReceiver extends BroadcastReceiver {
    static final String GAMECODE="Info";
    static final String GAMENAME="TTTGAME";
    static final String GAMEINVITE="INVITE";
    static final String GAMEACCEPTED="ACCEPTED";
    static final String GAMEDENIED="DENIED";
    static final String GAMESELECTED="SELECTED";
    static final String GAMETERMINATED="TERMINATED";
    static final String GAMEPLAY="PLAYGAME";
    static final String GAMEOVER="GAMEOVER";
    SharedData sharedData = SharedData.getSharedData();
    Player[] player = sharedData.getPlayers();
    GameScreen gameScreen;


   public SMSBroadcastReceiver(GameScreen gameScreen)
   {
        this.gameScreen=gameScreen;
    }

    public SMSBroadcastReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //get the bundle
        Bundle bundle=intent.getExtras();
        //create pdu object
        if(bundle !=null){
            final Object[] pdusObj = (Object[])bundle.get("pdus");
            for(int i =0;i<pdusObj.length;i++){
                SmsMessage message = SmsMessage.createFromPdu((byte[])pdusObj[i]);
                String msgString =message.getMessageBody();
                String phoneNumber=message.getDisplayOriginatingAddress();
                GameMessage(msgString, phoneNumber, context);

            }
        }
    }

    public void GameMessage(String msgString, final String phoneNumber, Context context) {
        Player[] players=SharedData.getSharedData().getPlayers();
        String[] message = msgString.split(":");
        //String gameAction=msgString;
        if(message.length ==4){
            String gameCode =message[0];
            if(gameCode.equals(GAMECODE)){
                String gameName=message[1];
                if(gameName.equals(GAMENAME)){
                    String gameAction=message[2];
                    if(gameAction.equals(GAMEINVITE)){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("You are invited by " +message[3]+
                                                "   to play TicTacToe game. Do you accept this Invitation?");
                        dialog.setTitle("Game Invitation");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String message = "Info:TTTGAME:ACCEPTED:"+player[1].getName();
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                            }

                        });
                        dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String message = "Info:TTTGAME:DENIED:"+player[1].getName();
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                            }
                        });

                        dialog.create().show();
                    }
                else if(gameAction.equals(GAMEACCEPTED))
                    {
                        Intent intent =new Intent(context,GameScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("MessageTo","Player1");
                        context.startActivity(intent);

                        String messg = "Info:TTTGAME:PLAYGAME:"+player[0].getName()+","+player[0].getSymbol();
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(player[1].getPhone(),null,messg,null,null);
                    }
                else if(gameAction.equals(GAMEDENIED))
                    {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("Your game is denied by " +message[3]);
                        alertDialogBuilder.setTitle("Message!");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface alertDialogBuilder, int i) {
                                        alertDialogBuilder.cancel();
                                    }

                                });
                        alertDialogBuilder.create().show();
                    }
                else if(gameAction.equals(GAMESELECTED))
                    {
                        try{
                            String gameSelectedCell = message[3];
                            String[] selectedCells = gameSelectedCell.split(",");
                            String selectedrow=selectedCells[0].substring(1);
                            int rows = Integer.parseInt(selectedrow);
                            String selectedcol = selectedCells[1].substring(0,1);
                            int cols =Integer.parseInt(selectedcol);
                            int index = 4*rows+cols;
                            gameScreen.Update(index);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else if(gameAction.equals(GAMEPLAY)){
                         String[] playerInformation = message[3].split(",");
                         Intent intent = new Intent(context,GameScreen.class);
                         intent.putExtra("MessageTo","Player2");
                         intent.putExtra("FirstPlayerName",playerInformation[0]);
                         int playerSymbol = Integer.parseInt(playerInformation[1]);
                         intent.putExtra("FirstPlayerSymbol",playerSymbol);
                         intent.putExtra("SecondPlayerHardCodedName",players[1].getName());
                         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                         context.startActivity(intent);
                    }
                    else if(gameAction.equals(GAMEOVER)|| gameAction.equals(GAMETERMINATED))
                    {
                        if(gameScreen instanceof GameScreen)
                        {
                            try{
                                gameScreen.DeclareGameResult(message[3]);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

        }
    }
}
