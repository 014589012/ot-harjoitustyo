package dao;

import domain.Event;
import domain.User;
import java.util.*;

/**
 * Class for dealing with a list of events.
 */
public class FileEventDao implements EventDao{
    /**
    * List of events
    */
    public List<Event> events;

    /**
    * Constructor for FileEventDao initializes list of events to an empty ArrayList.
    * 
    */
    public FileEventDao() {
        this.events = new ArrayList<>();
    }


    private int generateId(){
        return events.size()+1;
    }
    @Override
    public boolean create(Event event) throws Exception {
        if(event.getName().length()>24 || event.getName().length()<3) return false;
        event.setId(generateId());
        events.add(event);
        return true;
    }

    @Override
    public void delete(Event event){
        events.remove(event);
    }

    @Override
    public List<Event> getAllPrivate(User user) {
        List<Event> ans = new ArrayList<>();
        for (Event e2 : events) {
            if(e2.getUser().getId()==user.getId() && e2.isPrivate()) ans.add(e2);
        }
        return ans;
    }

    @Override
    public List<Event> getAllPublic() {
        List<Event> ans = new ArrayList<>();
        for (Event e2 : events) {
            if(!e2.isPrivate()) ans.add(e2);
        }
        return ans;
    }

}
