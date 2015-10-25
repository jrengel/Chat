package es.upm.fi.chat;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReceiveMessageService extends IntentService {

    public static final String ACTION_WRITE_DATABASE = "es.upm.fi.receivemessageservice.action.WRITE_DATABASE";
    private HttpURLConnection con; // Connection to receive messages

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ReceiveMessageService.class);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ReceiveMessageService.class);
        context.startService(intent);
    }

    public ReceiveMessageService() {
        super("ReceiveMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            List<Message> messagesList = null;

            try {
                // Establecer la conexión
                con = (HttpURLConnection)new URL("http", "ross.dia.fi.upm.es", "/andchat/get_messages.php?user=YO").openConnection();
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

            //TODO: Add list objects in the database
            SQLiteMessageManager databaseManager = new SQLiteMessageManager(getApplicationContext());
            if (databaseManager.writeMessages(messagesList)){
                Toast.makeText(getBaseContext(), "Mensajes escritos en la base de datos ", Toast.LENGTH_SHORT).show();
            }

            // sending intent to announce that service is done
            Intent i = new Intent();
            i.setAction(ACTION_WRITE_DATABASE);
            sendBroadcast(i);



            // sending pending intent to restart the service in 30 seconds
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Context context = getApplicationContext();
                    Intent i = new Intent(context, ReceiveMessageService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 322, i, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (3000), pendingIntent);
                }
            });


        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
