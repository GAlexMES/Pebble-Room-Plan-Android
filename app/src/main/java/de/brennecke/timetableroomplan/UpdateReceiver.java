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
import java.util.List;
import java.util.UUID;

import de.brennecke.timetableroomplan.model.Lesson;
import de.brennecke.timetableroomplan.sqlite.SQLiteSourceAdapter;

/**
 * Created by Alexander on 29.10.2015.
 */
public class UpdateReceiver extends BroadcastReceiver {


    public final static String UUID_AS_STRING = "3341d598-258e-4b71-b5a9-1c3348bf383e";
    private boolean noLessonATM = false;
    private Date dateOfUsedLessonn;
    private Boolean useOtherDate = false;
    private SQLiteSourceAdapter sqLiteSourceAdapter;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("alarm", "alarm");
        sqLiteSourceAdapter = new SQLiteSourceAdapter(context);
        sqLiteSourceAdapter.open();

        Date currentDate = new Date();
        String currentRoom = getRoom(currentDate, context);

        Date nextDate = currentDate;
        if(useOtherDate){
            nextDate = dateOfUsedLessonn;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(nextDate);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.add(Calendar.MINUTE, 30);
        Date nextLessonDate = cal.getTime();
        String nextRoom = getRoom(nextLessonDate, context);

        send(currentRoom, nextRoom, context);
        sqLiteSourceAdapter.close();
    }

    private String getRoom(Date date, Context context) {
        Lesson lesson = sqLiteSourceAdapter.getLesson(date);
        if (lesson != null) {
            return lesson.getLocation();
        } else {
            List<Lesson> lessonList = sqLiteSourceAdapter.getNextLessonsOfDay(date);
            if (lessonList == null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.add(Calendar.DATE, 1);
                dateOfUsedLessonn = cal.getTime();
                lessonList = sqLiteSourceAdapter.getNextLessonsOfDay(cal.getTime());
            }
            if(lessonList== null){
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                dateOfUsedLessonn = cal.getTime();
                lessonList = sqLiteSourceAdapter.getNextLessonsOfDay(cal.getTime());
            }
            if(lessonList==null){
                return "";
            }
            else{
                Lesson nextLesson = findFirstLesson(lessonList);
                int start = nextLesson.getStart();
                int minutes = start % 60;
                int hours = (start -minutes)/60;
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateOfUsedLessonn);
                cal.set(Calendar.HOUR_OF_DAY,hours);
                cal.set(Calendar.MINUTE,minutes);
                dateOfUsedLessonn = cal.getTime();
                useOtherDate = true;
                return nextLesson.getLocation();
            }
        }
    }

    private Lesson findFirstLesson(List<Lesson> lessonList) {
        Lesson retval = lessonList.get(0);
        for (Lesson l : lessonList) {
            if (retval.getStart() > l.getStart()) {
                retval = l;
            }
        }
        return retval;
    }


    private void send(String currentRoom, String nextRoom, Context context) {
        Log.i("Service", "updating with {" + currentRoom + ";" + nextRoom + "}!");
        PebbleDictionary outgoing = new PebbleDictionary();
        outgoing.addString(0, currentRoom);
        outgoing.addString(1, nextRoom);
        UUID appUUID = UUID.fromString(UUID_AS_STRING);
        PebbleKit.sendDataToPebble(context, appUUID, outgoing);
        Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();
    }
}
