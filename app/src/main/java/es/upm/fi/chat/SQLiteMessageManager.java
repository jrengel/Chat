package es.upm.fi.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by scooby4 on 23/10/2015.
 */
public class SQLiteMessageManager {
    private SQLiteHelper dbhelper;

    public SQLiteMessageManager(Context context){
        dbhelper = new SQLiteHelper(context);
    }

    // Reading messages from Database
    public List<Message> readMessages() throws SQLException{
        List<Message> resultado = new LinkedList<Message>();
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        //Cursor c = database.rawQuery("SELECT " + SQLiteHelper.COLUMN_ID + ", " + SQLiteHelper.COLUMN_USER + ", " + SQLiteHelper.COLUMN_MESSAGE + ", " + SQLiteHelper.COLUMN_DATE + ", "
        //+ "FROM " + SQLiteHelper.TABLA_MENSAJES + ";", new String[]{SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_USER, SQLiteHelper.COLUMN_MESSAGE, SQLiteHelper.COLUMN_DATE});
        Cursor c = database.rawQuery("SELECT * FROM " + SQLiteHelper.TABLA_MENSAJES, null);

        while(c.moveToNext()){
            int aux = c.getInt(0);
            Message m = new Message(c.getInt(0), c.getString(1), c.getString(2), new Date(c.getInt(3)));
            resultado.add(m);
        }
        c.close();
        database.close();

        return resultado;

    }

    // Inserting rows in Database
    public boolean writeMessages(List<Message> lm) {
        boolean result = false;
        SQLiteDatabase database = dbhelper.getWritableDatabase();

        for (Message m : lm){
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLiteHelper.COLUMN_ID, m.getId());
            contentValues.put(SQLiteHelper.COLUMN_USER, m.getUser_origin());
            contentValues.put(SQLiteHelper.COLUMN_MESSAGE, m.getMessage());
            contentValues.put(SQLiteHelper.COLUMN_DATE, m.getDate().getTime());
            long res = database.insert(SQLiteHelper.TABLA_MENSAJES, null, contentValues);
            result = (res != -1) ? true : false;
        }
        //close connection
        database.close();
        return result;



    }
}
