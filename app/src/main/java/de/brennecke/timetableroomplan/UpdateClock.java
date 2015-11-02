package de.brennecke.timetableroomplan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Alexander on 02.11.2015.
 */
public class UpdateClock {

    public static void setTimer(Context context){

        Intent alarmIntent = new Intent(context, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = 20*60*1000;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 57);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, interval, interval, pendingIntent);
        Log.i("alarm", "created alarm");
    }

}

