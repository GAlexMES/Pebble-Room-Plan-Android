package de.brennecke.timetableroomplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends Activity {

    Button showOnClock;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isConnected = PebbleKit.isWatchConnected(this);
        Toast.makeText(this, "Pebble " + (isConnected ? "is" : "is not") + " connected!", Toast.LENGTH_LONG).show();

        if(isConnected){
           // pushNotificationToPebble();
        }
    }


    private void pushNotificationToPebble(){
        // Push a notification
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");

        final Map data = new HashMap();
        data.put("title", "Test Message");
        data.put("body", "Whoever said nothing was impossible never tried to slam a revolving door.");
        final JSONObject jsonData = new JSONObject(data);
        final String notificationData = new JSONArray().put(jsonData).toString();

        i.putExtra("messageType", "PEBBLE_ALERT");
        i.putExtra("sender", "PebbleKit Android");
        i.putExtra("notificationData", notificationData);
        sendBroadcast(i);
    }
    public void addListenerOnButton() {

        showOnClock = (Button) findViewById(R.id.showOnClockButton);

        showOnClock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Context context = getApplicationContext();

                boolean isConnected = PebbleKit.isWatchConnected(context);
                String uuidAsString = "3341d598-258e-4b71-b5a9-1c3348bf383e";
                UUID appUUID =  UUID.fromString(uuidAsString);
                if(isConnected) {
                    //PebbleKit.startAppOnPebble(context, appUUID);

                    Toast.makeText(context, "Launching...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Watch is not connected!", Toast.LENGTH_LONG).show();
                }

                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        String currentRoom = ((EditText)findViewById(R.id.currentRoom)).getText().toString();
                        String nextRoom = ((EditText)findViewById(R.id.nextRoom)).getText().toString();
                        String uuidAsString = "3341d598-258e-4b71-b5a9-1c3348bf383e";
                        UUID appUUID =  UUID.fromString(uuidAsString);
                        PebbleDictionary outgoing = new PebbleDictionary();
                        outgoing.addString(0, currentRoom);
                        outgoing.addString(1, nextRoom);

                        PebbleKit.sendDataToPebble(getApplicationContext(), appUUID, outgoing);

                        Context context = getApplicationContext();
                        Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();
                    }

                }, 5000L);


            }

        });

    }
}
