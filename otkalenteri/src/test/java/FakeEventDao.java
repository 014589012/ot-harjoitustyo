
import dao.EventDao;
import domain.Event;
import domain.User;
import java.util.ArrayList;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kalle
 */
class FakeEventDao implements EventDao {
    public List<Event> events;
    public FakeEventDao(FakeUserDao fud) {
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
    public void delete(Event event) throws Exception{
        events.remove(event);
    }

    @Override
    public List<Event> getAllPrivate(User user) {
        List<Event> ans = new ArrayList<>();
        if(user == null) return ans;
        for (Event e2 : events) {
            if(e2.getUser().getUsername().equals(user.getUsername()) && e2.isPrivate()) ans.add(e2);
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

    @Override
    public void addDescription(String des, Event ev) throws Exception{
        ev.setDescription(des);
    }
    
    
    
}
