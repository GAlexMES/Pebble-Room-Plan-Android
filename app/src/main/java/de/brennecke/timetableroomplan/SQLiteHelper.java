package de.brennecke.timetableroomplan;

/**
 * Created by Alexander on 28.10.2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TTBL = "ttbl";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_LESSON= "lesson";
    public static final String COLUMN_START= "start";
    public static final String COLUMN_END= "end";
    public static final String COLUMN_WEEKMODE= "weekmode";
    public static final String COLUMN_ROOM= "room";

    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath()+"/PebbleRoomPlan/times.db";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_TTBL + "(" +
            COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DAY + " integer, "
            + COLUMN_LESSON + " integer, "
            + COLUMN_ROOM+ " text, "
            + COLUMN_START + " integer, "
            + COLUMN_END + " integer, "
            + COLUMN_WEEKMODE + " integer, "
            + ");";

    public SQLiteHelper(Context context) {
        super(context,DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TTBL);
        onCreate(db);
    }

}