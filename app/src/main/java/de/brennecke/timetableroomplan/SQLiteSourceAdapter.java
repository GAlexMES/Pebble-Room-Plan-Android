package de.brennecke.timetableroomplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.brennecke.timetableroomplan.model.Lesson;
import de.brennecke.timetableroomplan.model.TTBL;

/**
 * Created by Alexander on 28.10.2015.
 */
public class SQLiteSourceAdapter {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_DAY, SQLiteHelper.COLUMN_LESSON, SQLiteHelper.COLUMN_START, SQLiteHelper.COLUMN_END, SQLiteHelper.COLUMN_WEEKMODE, SQLiteHelper.COLUMN_ROOM};

    public SQLiteSourceAdapter(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTTBL(TTBL ttbl){
        List<Lesson> lessonList = ttbl.getLessonList();
        for(Lesson l : lessonList) {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_DAY, l.getDayOfWeek());
            values.put(SQLiteHelper.COLUMN_END, l.getEnd());
            values.put(SQLiteHelper.COLUMN_LESSON, l.getLesson());
            values.put(SQLiteHelper.COLUMN_ROOM, l.getLocation());
            values.put(SQLiteHelper.COLUMN_START, l.getStart());
            values.put(SQLiteHelper.COLUMN_WEEKMODE, l.getWeekmode());
            long insertId = database.insert(SQLiteHelper.TABLE_TTBL, null, values);
        }
    }

    public Lesson getNextLesson() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int weekmode = cal.get(Calendar.WEEK_OF_YEAR)%2;
        int day = cal.get(Calendar.DAY_OF_WEEK);


        String condition =  SQLiteHelper.COLUMN_WEEKMODE +"="+weekmode+" AND "
                            + SQLiteHelper.COLUMN_DAY+"="+day;
        Cursor cursor = database.query(SQLiteHelper.TABLE_TTBL,
                allColumns, condition, null,
                null, null, null);

        cursor.moveToFirst();
        Lesson newLesson = cursorToLesson(cursor);
        cursor.close();
        return newLesson;
    }

    private Lesson cursorToLesson(Cursor cursor) {
        Lesson retval = new Lesson();
        retval.setDayOfWeek(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_DAY)));
        retval.setStart(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_START)));
        retval.setEnd(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_END)));
        retval.setWeekmode(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_WEEKMODE)));
        retval.setLesson(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_LESSON)));
        retval.setLocation(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_ROOM)));
        return retval;
    }


}
