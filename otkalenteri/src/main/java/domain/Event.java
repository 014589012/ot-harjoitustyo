package domain;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Event {
    private final String name;
    private final Date date;
    private final User user;
    private final boolean prive;
    private int id;

    public Event(String name, String date, boolean prive, User user) throws ParseException {
        this.id=0;
        this.name = name;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        this.date = df.parse(date);
        this.prive = prive;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public boolean isPrivate() {
        return prive;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }


}
