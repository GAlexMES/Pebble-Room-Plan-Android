package de.brennecke.timetableroomplan.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Alexander on 26.10.2015.
 */
public class Exchange {

    public final static String UUID_AS_STRING = "3341d598-258e-4b71-b5a9-1c3348bf383e";

    private static Exchange instance = null;

    private long waitTime = 5000;
    private TTBL ttbl;
    private  PendingIntent pendingIntent;


    public static Exchange getInstance(){
        if(instance == null){
            instance = new Exchange();
        }
        return instance;
    }

    public void setTTBL(TTBL ttbl){
        this.ttbl = ttbl;
    }

    public void updateClock(Context context){
        boolean isConnected = PebbleKit.isWatchConnected(context);
        UUID appUUID = UUID.fromString(UUID_AS_STRING);
        if (isConnected) {
            //PebbleKit.startAppOnPebble(context, appUUID);
            Toast.makeText(context, "Launching...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Watch is not connected!", Toast.LENGTH_LONG).show();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int weekmode = c.get(Calendar.WEEK_OF_YEAR)%2;
        int minutesOfDay = c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE);
        int currentLesson = getLesson(minutesOfDay);
        int nextLesson = currentLesson+1;

        String currentRoom = getRoom(dayOfWeek,weekmode,currentLesson);
        String nextRoom = getRoom(dayOfWeek,weekmode,nextLesson);

        updateWaitTime(weekmode, dayOfWeek, currentLesson, minutesOfDay, nextRoom);

        Log.i("Service", "updating with {" + currentRoom + ";" + nextRoom + "}!");
        PebbleDictionary outgoing = new PebbleDictionary();
        outgoing.addString(0, currentRoom);
        outgoing.addString(1, nextRoom);
        PebbleKit.sendDataToPebble(context, appUUID, outgoing);
        Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();
    }

    private void updateWaitTime(int weekmode, int day, int currentLesson, int minutesOfDay,  String nextRoom){
        int nextLesson = 0;
        boolean nextDay = false;
        boolean weekend = false;
        if(nextRoom.equals("")){
            nextDay = true;
            if(day<7){
                nextLesson = findFirstLessonOfDay(weekmode,day+1);
            }
            else{
                weekend = true;
                nextLesson = findFirstLessonOfDay((weekmode+1)%2,2);
            }
        }
        else{
            nextLesson = currentLesson +1;
        }
        int timeOfNextLesson = ttbl.times.get(nextLesson);
        long waitTimeInMinutes = 0;

        if(nextDay){
            waitTimeInMinutes = waitTimeInMinutes + (24*60)-minutesOfDay;
            if(weekend){
                waitTimeInMinutes = waitTimeInMinutes + (2*24*60);
            }
            waitTimeInMinutes = waitTimeInMinutes + timeOfNextLesson;
        }
        else{
            waitTimeInMinutes = timeOfNextLesson - minutesOfDay;
        }

        long waitTimeInMilliseconds = waitTimeInMinutes * 60 * 1000;
        Log.i("nextUpdate", "Next update of clock will be in  " + waitTimeInMilliseconds + " milliseconds");
        waitTime = waitTimeInMilliseconds;
    }


    private int findFirstLessonOfDay(int weekmode, int day){
        Map<Integer, String> lessonsOfDay = ttbl.sortedLocationList.get(weekmode).get(day);

        int firstLesson = 0;
        for(int i = 0; i< ttbl.times.keySet().size()-1; i++){
            String room = lessonsOfDay.get(i);
            if(room!=null){
                Log.i("firstLesson", "First lesson is: " + firstLesson);
                return i;
            }
            else{
                firstLesson = i;
            }
        }
        Log.i("firstLesson", "First lesson is: " + firstLesson);
        return 0;
    }

    private int getLesson(int minutesOfDay){
        for(Integer i : ttbl.times.keySet()){
            if(ttbl.times.get(i)<minutesOfDay&&ttbl.times.get(i)+110>minutesOfDay){
                return i;
            }
        }
        return -1;
    }

    private String getRoom(int dayOfWeek, int weekmode, int lesson){
        try {
            String retval =  ttbl.sortedLocationList.get(weekmode).get(dayOfWeek).get(lesson);
            if(retval == null){
                retval = "";
            }
            return retval;
        }
        catch(NullPointerException npe){
            return "";
        }
    }

    public void startAlarm(Context context){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), waitTime, pendingIntent);
    }

    public void setPendingIntent(PendingIntent pendingIntent){
        this.pendingIntent = pendingIntent;
    }
}
