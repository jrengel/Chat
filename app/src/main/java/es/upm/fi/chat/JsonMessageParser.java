package es.upm.fi.chat;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by scooby4 on 22/10/2015.
 */
public class JsonMessageParser {

    public List<Message> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return leerMensaje(reader);
        } finally {
            reader.close();
        }
    }


    public List<Message> leerArrayMensajes(JsonReader reader) throws IOException {
        // Lista temporal
        ArrayList<Message> myMessages = new ArrayList<Message>();
        String idString = null;
        int id = -1;
        String user_origin = null;
        String message = null;
        String dateString = null;
        Date date = null;

        reader.beginArray();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")){
                idString = reader.nextString();
                id = Integer.parseInt(idString);
            } else if (name.equals("user_origin")){
                user_origin = reader.nextString();
            } else if (name.equals("message")){
                message = reader.nextString();
            } else if (name.equals("date_mod")){
                dateString = reader.nextString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'HH:mm:ss");
                try {
                    date = formatter.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }  else {
                reader.skipValue();
            }
            myMessages.add(new Message(id, user_origin, message, date));
        }
        reader.endArray();
        return myMessages;
    }

    public List<Message> leerMensaje(JsonReader reader) throws IOException {
        ArrayList<Message> myMessages = new ArrayList<Message>();
        String idString = null;
        int id = -1;
        String user_origin = null;
        String message = null;
        String dateString = null;
        Date date = null;

        // Iniciar objeto
        reader.beginObject();
        // Lectura de cada atributo

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("messages")) {
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    while(reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals("id")) {
                            idString = reader.nextString();
                            id = Integer.parseInt(idString);
                        } else if (name.equals("user_origin")) {
                            user_origin = reader.nextString();
                        } else if (name.equals("message")) {
                            message = reader.nextString();
                        } else if (name.equals("date_mod")) {
                            dateString = reader.nextString();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                            try {
                                date = formatter.parse(dateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    myMessages.add(new Message(id, user_origin, message, date));
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return myMessages;
    }


}
