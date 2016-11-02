package edu.oakland.cse523.multiplayertictactoe;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import edu.oakland.cse523.multiplayertictactoe.Player;
import edu.oakland.cse523.multiplayertictactoe.SMSBroadcastReceiver;
import edu.oakland.cse523.multiplayertictactoe.SettingsScreen;
import edu.oakland.cse523.multiplayertictactoe.SharedData;


public class MainActivity extends Activity {
    EditText editTextPhone =null;
    Button btnInvite = null;
    RelativeLayout relMain =null;
    SharedData sharedData = SharedData.getSharedData();
    Player[] player = sharedData.getPlayers();
    ImageView imageviewSettings = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relMain = (RelativeLayout) findViewById(R.id.relMain);
        editTextPhone = (EditText) findViewById(R.id.editText);
        btnInvite = (Button) findViewById(R.id.buttonPlay);
      //  imageviewSettings = (ImageView) findViewById(R.id.imageView2);

        btnInvite.setEnabled(false);
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                    btnInvite.setEnabled(true);
                else
                    btnInvite.setEnabled(false);

            }
        });
//            imageviewSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(),SettingsScreen.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//                }
//        });
            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player[1].setPhone(editTextPhone.getText().toString());
                    String message="Info:TTTGAME:INVITE:"+player[0].getName();
                    String phoneNumber=player[1].getPhone();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber,null,message,null,null);
                }
            });

            SMSBroadcastReceiver smsBroadcastReceiver = new SMSBroadcastReceiver();
            IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            this.registerReceiver(smsBroadcastReceiver,filter);

    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
