package es.upm.fi.chat;

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

    public List<Message> readMessages() throws SQLException{
        List<Message> resultado = new LinkedList<Message>();
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT" + SQLiteHelper.COLUMN_ID + ", " + SQLiteHelper.COLUMN_USER + ", " + SQLiteHelper.COLUMN_MESSAGE + ", " + SQLiteHelper.COLUMN_DATE + ", "
        + "FROM" + SQLiteHelper.TABLA_MENSAJES + ";", new String[]{SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_USER, SQLiteHelper.COLUMN_MESSAGE, SQLiteHelper.COLUMN_DATE});

        while(!c.isAfterLast()){
            Message m = new Message(c.getInt(0), c.getString(1), c.getString(2), new Date(c.getInt(3)));
            resultado.add(m);
        }
        return resultado;

    }
}
