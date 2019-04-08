package domain;

import dao.FileEventDao;
import dao.FileUserDao;
import java.util.List;

public class EventService {

    private FileEventDao eventDao;
    private FileUserDao userDao;
    private User loggedIn;

    public EventService(FileEventDao eventDao, FileUserDao userDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
    }

    public boolean login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return false;
        }
        if(!user.getPassword().equals(password)) return false;
        loggedIn = user;
        return true;
    }
    
    public Object getLoggedUser() {
        return loggedIn;
    }

    public boolean createEvent(String text) {
        Event todo = new Event(text, "01.01.2019", false, loggedIn);
        try {   
            eventDao.create(todo);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public void logout() {
        loggedIn=null;
    }

    public boolean createUser(String username, String name) {
        if (userDao.findByUsername(username) != null) {
            return false;
        }
        User user = new User(username, name);
        try {
            userDao.create(user);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    public void markDone(Event e2 ){
        eventDao.delete(e2);
    }

    public List<Event> getUpcoming() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
}
