package de.brennecke.timetableroomplan;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

import de.brennecke.timetableroomplan.model.Exchange;


/**
 * Created by Alexander on 26.10.2015.
 */
public class UpdateClockService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        long waitTime = intent.getLongExtra("TIME_TO_WAIT",3000);
        Log.i("intent", "will sleep for " + waitTime + " milliseconds");

        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Context context = getApplicationContext();
        Exchange.getInstance().updateClock(context);
        Log.i("intent", "waked up");
        Exchange.getInstance().startService(context);
        Log.i("intent", "started new service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("intent", "destroyed me");
    }


}