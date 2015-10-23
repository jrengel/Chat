package es.upm.fi.chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by scooby4 on 23/10/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper{
    public static String TABLA_MENSAJES = "messages";
    public static String COLUMN_ID = "id";
    public static String COLUMN_USER = "user_origin";
    public static String COLUMN_DATE = "date";
    public static String COLUMN_MESSAGE = "message";

    private static final String DATABASE_NAME = "messages.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(DATA_BASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static final String DATA_BASE_CREATE = "create table " +
            TABLA_MENSAJES + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_USER + " text not null, " +
            COLUMN_MESSAGE + " text not null, " +
            COLUMN_DATE + " text not null" ;

}
