package dao;

import domain.Event;
import domain.User;
import java.util.*;


public class FileEventDao implements EventDao{
    public List<Event> events;

    public FileEventDao() {
        this.events = new ArrayList<Event>();
    }
    
    
    private int generateId(){
        return events.size()+1;
    }
    @Override
    public void create(Event event) throws Exception {
        event.setId(generateId());
        events.add(event);
    }

    @Override
    public void delete(Event event){
        events.remove(event);
    }

    @Override
    public List<Event> getAllPrivate(User user) {
        List<Event> ans = new ArrayList<Event>();
        for (Event e2 : events) {
            if(e2.getUser().getId()==user.getId() && e2.isPrivate()) ans.add(e2);
        }
        return ans;
    }

    @Override
    public List<Event> getAllPublic() {
        List<Event> ans = new ArrayList<Event>();
        for (Event e2 : events) {
            if(!e2.isPrivate()) ans.add(e2);
        }
        return ans;
    }
    
}
