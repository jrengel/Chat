package es.upm.fi.chat;

import java.util.Date;

/**
 * Created by scooby4 on 22/10/2015.
 */
public class Message {

    //Atributes
    private int id;
    private String user_origin;
    private String message;
    private Date date;

    public Message (){

    }

    public Message(int id, String user_origin, String message, Date date){
        this.id = id;
        this.user_origin = user_origin;
        this.message = message;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_origin() {
        return user_origin;
    }

    public void setUser_origin(String user_origin) {
        this.user_origin = user_origin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", date=" + date +
                ", message='" + message + '\'' +
                ", user_origin='" + user_origin + '\'' +
                '}';
    }
}
