package dao;

import domain.Event;
import domain.User;
import java.util.*;


public class FileEventDao implements EventDao{
    public List<Event> events;

    public FileEventDao() {
        this.events = new ArrayList<>();
    }


    private int generateId(){
        return events.size()+1;
    }
    @Override
    public boolean create(Event event) throws Exception {
        if(event.getName().length()>24 || event.getName().length()<3) throw new Exception();
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
