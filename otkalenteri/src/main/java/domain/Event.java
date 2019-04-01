package domain;
import domain.User;


public class Event {
    private String name;
    private String date;
    private User user;
    private boolean prive;

    public Event(String name, String date, boolean prive, User user) {
        this.name = name;
        this.date = date;
        this.prive = prive;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    
}
