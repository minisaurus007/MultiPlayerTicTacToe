package edu.oakland.cse523.multiplayertictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class GameScreen extends Activity {
    TextView textViewturn =null;
    TextView textViewTimetaken=null;
    TextView textViewWinner=null;

    TextView textViewHiddenName = null;
    TextView textViewHiddenNamePlayer2=null;
    TextView textViewHidden = null;


    Button backButton=null;

   // GridLayout grlGame =null;
    Player[] players = null;
    RelativeLayout relGame=null;
    RelativeLayout relSplash=null;
    Intent intent =null;
    String messageTo="";
    int current =0;
    int other=1;
    int count=0;
    int btnindex=0;
    TableLayout gameButtonContainer=null;
    private RefreshHandler mRedawHandler = new RefreshHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        relGame=(RelativeLayout)findViewById(R.id.relGame);
        relSplash=(RelativeLayout)findViewById(R.id.relSplash);
        textViewTimetaken=(TextView)findViewById(R.id.textViewTimetaken);
        textViewturn=(TextView)findViewById(R.id.textViewTurn);
        textViewWinner=(TextView)findViewById(R.id.textViewWinner);
        SharedData sharedData=SharedData.getSharedData();
        players=sharedData.getPlayers();
        backButton=(Button)findViewById(R.id.backButton);

        textViewturn.setText(players[current].getName());
        textViewWinner.setText("No Winner Yet");

        intent = getIntent();
        messageTo=intent.getStringExtra("MessageTo");
        gameButtonContainer =(TableLayout)findViewById(R.id.tablelayoutSymbols);

        for(int i=0;i<4;i++) {
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setLayoutParams(params);
            for (int j = 0; j < 4; j++) {
                TTTButton button = new TTTButton(getApplicationContext());
                button.setImageResource(R.drawable.images_background_game);

                button.index = count;
                players[0].buttons[count]=button;
                players[1].buttons[count]=button;

                players[0].register(button,count);
                players[1].register(button,count);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textViewTimetaken.setText("0");
                        updateUserInterface();

                        if (messageTo.equals("Player2")) {
                            current = 1;
                            textViewturn.setText(textViewHiddenName.getText());
                        }
                        else if (messageTo.equals("Player1")) {
                            current = 0;
                            textViewturn.setText(players[1].getName());
                        }
                        other = (current == 0 ? 1 : 0);
                        for (int i = 0; i < 16; i++) {
                            //TTTButton buttonClicked = (TTTButton)v;
                            players[current].buttons[i].setEnabled(false);
                            //buttons[i].setEnabled(false);
                        }
                        TTTButton buttonClicked = (TTTButton) v;
                        players[current].MarkCell(buttonClicked.index, players, other);
                        WinnerDecision();
                    }

                });
                tableRow.addView(button);
                count++;
            }
            gameButtonContainer.addView(tableRow);
        }
            //player 1 starts the game,disabling player2 game sccreen
            if(messageTo!=null && messageTo.equals("Player1"))
            {

            }
            if(messageTo!=null && messageTo.equals("Player2"))
            {
                for(int i=0;i<16;i++)
                players[other].buttons[i].setEnabled(false);
                int s=intent.getIntExtra("FirstPlayerSymbol",0);
                textViewHidden=(TextView)findViewById(R.id.textViewHidden);
                textViewHidden.setText(String.valueOf(s));

                String n= intent.getStringExtra("FirstPlayerName");
                textViewHiddenName=(TextView)findViewById(R.id.textViewHiddenName);
                textViewHiddenName.setText(n);
                textViewturn.setText(n);

                textViewHiddenNamePlayer2=(TextView)findViewById(R.id.textViewHiddenNamePlayer2);
                textViewHiddenNamePlayer2.setText(intent.getStringExtra("SecondPlayerHardCodedName"));
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(relSplash);
                    }
                });

            }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        }

    synchronized private void updateUserInterface(){

        int currentInt = Integer.parseInt((String) textViewTimetaken.getText())+1;
        mRedawHandler.sleep(1000);
        textViewTimetaken.setText(String.valueOf(currentInt));
    }

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            updateUserInterface();
        }
        public void sleep(long delayMillis){
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0),delayMillis);
        }
    }
    public void Update(int index){
        textViewTimetaken.setText("0");
        updateUserInterface();
        //
        if(messageTo!=null && messageTo.equals("Player2")){
            int symbol = Integer.parseInt(textViewHidden.getText().toString());
            players[0].MarkCell(index, symbol);

            textViewturn.setText(textViewHiddenNamePlayer2.getText());
        }
        else if(messageTo!=null &&messageTo.equals("Player1")){
            players[1].MarkCell(index);
            WinnerDecision();
            textViewturn.setText(players[0].getName());
        }
        for(int i=0;i<16;i++){
            if(players[current].cells[i].isDataCellMarked()||players[other].cells[i].isDataCellMarked())
                continue;
            players[current].buttons[i].setEnabled(true);
        }
        current =(current==0)?1:0;
        other=(current==0)?1:0;
    }

    private void WinnerDecision()
    {
        //who is winner and whose turn
        String winner = winner();
        if(winner =="") {
            if (isGameOver()) {
                DeclareGameResult("Draw!" + "\n Replay the game ");
                String message = "Info:TTTGAME:GAMEOVER:" + "Draw" + "Replay the game";
                String phone = "";
                if (messageTo.equals("Player1")) {
                    phone = players[1].getPhone();

                } else if (messageTo.equals("Player2")) {
                    phone = players[0].getPhone();
                }
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, message, null, null);
            } else {

                //textViewturn.setText(players[current].getName());
                textViewWinner.setText("No Winner");
            }

        }
        else
        {
            textViewturn.setText("");
            textViewWinner.setText(winner);
            DeclareGameResult("Winner is "+winner+".\n Replay the game");
            String message="Info:TTTGAME:"+"Winner is "+winner+". Press Replay to play again";
            String phone="";
            if(messageTo.equals("Player1")){
                phone=players[1].getPhone();

            }
            else if(messageTo.equals("Player2"))
            {
                phone = players[0].getPhone();
            }
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,message,null,null);
        }
    }
    private String winner()
    {
        Player firstPlayer = players[0];
        Player secondPlayer = players[1];
        Boolean won = winnerLogic(firstPlayer);
        if (!won) {
            won = winnerLogic(secondPlayer);

            if (won) {
                return secondPlayer.getName();
            } else {
                return "";
            }

        }
        else{
            return firstPlayer.getName();
        }
    }
    public Boolean isGameOver(){
        Player firstPlayer = players[0];
        Player secondPlayer = players[1];
        boolean isMarkedFirstPlayer = false;
        boolean isMarkedSecondPlayer = false;
        int dataCellIndex1=0,dataCellIndex2=0;
        for(int i=0;i<16;i++){
            isMarkedFirstPlayer = firstPlayer.cells[i].isDataCellMarked();
            if(!isMarkedFirstPlayer)
                isMarkedSecondPlayer = secondPlayer.cells[i].isDataCellMarked();

            if(isMarkedFirstPlayer){

                dataCellIndex1++;
            }
            if(isMarkedSecondPlayer){
                dataCellIndex2++;
            }
        }
        if((dataCellIndex1+dataCellIndex2) == 16)
            return  true;

        return false;

    }

    public void DeclareGameResult(String s){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameScreen.this);
        dialogBuilder.setMessage(s);
        dialogBuilder.setTitle("Game Over!");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Replay",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                current =0;other=1;
                players[0].unMarkCell();
                players[1].unMarkCell();
                finish();
                Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dialogBuilder.create().show();
    }
    public Boolean winnerLogic(Player player){
        if(player.cells[0].isDataCellMarked() == true && player.cells[1].isDataCellMarked() == true && player.cells[2].isDataCellMarked()==true && player.cells[3].isDataCellMarked()==true)
            return true;
        else if(player.cells[4].isDataCellMarked() == true && player.cells[5].isDataCellMarked() == true && player.cells[6].isDataCellMarked()==true && player.cells[7].isDataCellMarked()==true)
            return true;
        else if(player.cells[8].isDataCellMarked() == true && player.cells[9].isDataCellMarked() == true && player.cells[10].isDataCellMarked()==true && player.cells[11].isDataCellMarked()==true)
            return true;
        else if(player.cells[12].isDataCellMarked() == true && player.cells[13].isDataCellMarked() == true && player.cells[14].isDataCellMarked()==true && player.cells[15].isDataCellMarked()==true)
            return true;
        else if(player.cells[0].isDataCellMarked() == true && player.cells[4].isDataCellMarked() == true && player.cells[8].isDataCellMarked()==true && player.cells[12].isDataCellMarked()==true)
            return true;
        else if(player.cells[1].isDataCellMarked() == true && player.cells[5].isDataCellMarked() == true && player.cells[9].isDataCellMarked()==true && player.cells[13].isDataCellMarked()==true)
            return true;
        else if(player.cells[2].isDataCellMarked() == true && player.cells[6].isDataCellMarked() == true && player.cells[10].isDataCellMarked()==true && player.cells[14].isDataCellMarked()==true)
            return true;
        else if(player.cells[3].isDataCellMarked() == true && player.cells[7].isDataCellMarked() == true && player.cells[11].isDataCellMarked()==true && player.cells[15].isDataCellMarked()==true)
            return true;
        else if(player.cells[0].isDataCellMarked() == true && player.cells[5].isDataCellMarked() == true && player.cells[10].isDataCellMarked()==true && player.cells[15].isDataCellMarked()==true)
            return true;
        else if(player.cells[3].isDataCellMarked() == true && player.cells[6].isDataCellMarked() == true && player.cells[9].isDataCellMarked()==true && player.cells[12].isDataCellMarked()==true)
            return true;
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        textViewTimetaken.setText("0");
        updateUserInterface();
        SMSBroadcastReceiver smsBroadcastReceiver=new SMSBroadcastReceiver(this);
        IntentFilter intentFilter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(smsBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeclareGameResult(("Sorry Game got interrupted.\n press Replay to play game again"));
        String message="Info:TTTGAME:TERMINATED:"+"Sorry! Game interruped.\n"+"Press Replay to play again";
        String phone="";
        if(messageTo.equals("Player1"))
        {
            phone=players[1].getPhone();
        }
        else if(messageTo.equals("Player2"))
        {
            phone=players[0].getPhone();
        }
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(phone,null,message,null,null);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
