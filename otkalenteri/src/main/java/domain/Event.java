package domain;
import domain.User;


public class Event {
    private String name;
    private String date;
    private User user;
    private boolean prive;
    private int id;

    public Event(String name, String date, boolean prive, User user) {
        this.id=0;
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
