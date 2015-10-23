package es.upm.fi.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends ActionBarActivity {

    HttpURLConnection con; // Connection to receive messages
    HttpURLConnection conSend; // Connection to send message

    // ASYNCTASK PARA JSON
    public class JsonTask extends AsyncTask<URL, Void, List<Message>> {

        @Override
        protected List<Message> doInBackground(URL... url) {
            List<Message> messagesList = null;

            try {
                // Establecer la conexión
                con = (HttpURLConnection)url[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    messagesList = new ArrayList<Message>();
                    messagesList.add(new Message(0, null, null, null));

                } else {
                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    JsonMessageParser parser = new JsonMessageParser();
                    messagesList = parser.leerFlujoJson(in);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return messagesList;
        }

        @Override
        protected void onPostExecute(List<Message> messagesList) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(messagesList !=null) {
                // Hacer lo que queramos con la agenda, en un adapter, en un activity, en un toast...etc.
                ArrayList<String> showArray = new ArrayList<String>();
                for(Message m: messagesList){
                    showArray.add(m.getUser_origin() + ": " + m.getMessage());
                }
                if (showArray.size() == 0){
                    Toast.makeText(getBaseContext(), "No hay mensajes nuevos", Toast.LENGTH_SHORT).show();
                } else {

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, showArray);
                    ListView lv = (ListView) findViewById(R.id.listView);
                    lv.setAdapter(arrayAdapter);
                }

            }else{
                Toast.makeText(getBaseContext(), "Ocurrió un error de Parsing Json", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class SendMessageTask extends AsyncTask<URL, Void, Void> {
        @Override
        protected Void doInBackground(URL... url) {
            // Establecer la conexión
            try {
                conSend = (HttpURLConnection)url[0].openConnection();

                conSend.setConnectTimeout(15000);
                conSend.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = conSend.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                conSend.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getBaseContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messages);



        Button receiveButton = (Button) findViewById(R.id.button2);
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // JSON petition
                try {
                    new JsonTask().execute(new URL("http", "ross.dia.fi.upm.es", "/andchat/get_messages.php?user=" + getIntent().getStringExtra("User") )); //http://ross.dia.fi.upm.es/andchat/get_messages.php?user=YO
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText myMessage = (EditText) findViewById(R.id.editTextMessage);
                    String myMessageString = myMessage.getText().toString();
                    new SendMessageTask().execute(new URL("http", "ross.dia.fi.upm.es", "/andchat/send_message.php?user=" +
                            getIntent().getStringExtra("User") + "&message=" + myMessageString));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });




    }

}
