package edu.oakland.cse523.multiplayertictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsScreen extends Activity {
    //grid layout
    GridLayout grlSymbols = null;
    //Edit Text for entering name
    EditText textName = null;
    //ButtonNext for getting next activity
    Button buttonNext;
    //Button p1Button;
    Button backButton;
    boolean handled = false;
    ImageButton startButton=null;
    int buttonindex=0;
    ImageView imageViewPSymbol =null;
    //ImageView imageViewP2Symbol = null;
    //List of symbols for players
   // int id[] = new int[]{android.R.drawable.alert_dark_frame,android.R.drawable.btn_star,android.R.drawable.btn_radio,
    //        android.R.drawable.ic_btn_speak_now,android.R.drawable.ic_menu_compass,
     //       android.R.drawable.ic_secure};
    int id[] = new int[]{R.drawable.cubs1,R.drawable.maveriks1,R.drawable.lions1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        buttonNext =(Button)findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);
        textName =(EditText)findViewById(R.id.txtName);
        textName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if(v.length()>0)
                        handled=true;

                    else
                        handled=false;

                    if (handled==true)
                        buttonNext.setEnabled(true);

                    else
                        buttonNext.setEnabled(false);
                }

                return handled;
            }
        });
        imageViewPSymbol = (ImageView)findViewById(R.id.imageViewPSymbol);
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonindex = v.getId();
               // Toast.makeText(getApplicationContext(),buttonindex,Toast.LENGTH_LONG).show();
                imageViewPSymbol.setImageResource(buttonindex);
                handled = true;
                if(handled == true)
                    buttonNext.setEnabled(true);
                else
                    buttonNext.setEnabled(false);
            }
        };
        grlSymbols = (GridLayout)findViewById(R.id.grlSymbols);
        for(int i=0;i<3;i++){
            ImageButton imageButton = (ImageButton)getLayoutInflater().inflate(R.layout.activity_image_button,grlSymbols,false);
            imageButton.setId(id[i]);
            imageButton.setAdjustViewBounds(true);
            imageButton.setImageResource(id[i]);
            imageButton.setOnClickListener(onclick);
            grlSymbols.addView(imageButton);

        }
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedData sharedData=SharedData.getSharedData();
                Player[] players = sharedData.getPlayers();
                if(players[0]==null){
                    players[0] = new Player(textName.getText().toString(),buttonindex,"5556");
                }
                else
                {
                    players[0].setName(textName.getText().toString());
                    players[0].setSymbol(buttonindex);
                }
                Intent intent =new Intent(v.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings_screen, menu);
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
