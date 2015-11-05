package de.brennecke.timetableroomplan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by Alexander on 02.11.2015.
 */
public class UpdateClock {

    public static void setTimer(Context context, List<Integer> timeList) {
        int interval = 24 * 60 * 60 * 1000;
        Intent alarmIntent = new Intent(context, UpdateReceiver.class);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (Integer i : timeList) {
            final int _id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id, alarmIntent,PendingIntent.FLAG_ONE_SHOT);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, interval, interval, pendingIntent);
            Log.i("alarm", "created alarm for: "+i);
        }

        long nextTriggerTime = manager.getNextAlarmClock().getTriggerTime();
        Log.i("alarm", "next alarm will be at: "+nextTriggerTime);
    }

}

