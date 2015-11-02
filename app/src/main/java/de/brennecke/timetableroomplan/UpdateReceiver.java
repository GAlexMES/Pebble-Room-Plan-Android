package de.brennecke.timetableroomplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import de.brennecke.timetableroomplan.model.Lesson;
import de.brennecke.timetableroomplan.sqlite.SQLiteSourceAdapter;

/**
 * Created by Alexander on 29.10.2015.
 */
public class UpdateReceiver extends BroadcastReceiver {


    public final static String UUID_AS_STRING = "3341d598-258e-4b71-b5a9-1c3348bf383e";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("alarm","alarm");

        Date currentDate = new Date();
        String currentRoom = getRoom(currentDate, context);

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.add(Calendar.MINUTE, 30);
        Date nextLessonDate = cal.getTime();
        String nextRoom = getRoom(nextLessonDate, context);

        send(currentRoom, nextRoom, context);
    }

    private String getRoom(Date date, Context context){
        SQLiteSourceAdapter sqLiteSourceAdapter = new SQLiteSourceAdapter(context);
        sqLiteSourceAdapter.open();
        Lesson lesson = sqLiteSourceAdapter.getLesson(date);
        sqLiteSourceAdapter.close();
        if(lesson!=null) {
            return lesson.getLocation();
        }

        return "";
    }

    private void send (String currentRoom, String nextRoom, Context context){
        Log.i("Service", "updating with {" + currentRoom + ";" + nextRoom + "}!");
        PebbleDictionary outgoing = new PebbleDictionary();
        outgoing.addString(0, currentRoom);
        outgoing.addString(1, nextRoom);
        UUID appUUID = UUID.fromString(UUID_AS_STRING);
        PebbleKit.sendDataToPebble(context, appUUID, outgoing);
        Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();
    }
}
