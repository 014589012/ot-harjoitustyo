package domain;

import dao.FileEventDao;
import dao.FileUserDao;
import java.text.ParseException;
import java.util.List;
import java.util.Date;

public class EventService {

    private final FileEventDao eventDao;
    private final FileUserDao userDao;
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

    public User getLoggedUser() {
        return loggedIn;
    }

    public boolean createEvent(String text, String dd, boolean prive) throws ParseException {
        Event event = new Event(text, dd, prive, loggedIn);
        try {
            eventDao.create(event);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public void logout() {
        loggedIn=null;
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByUsername(username) != null) {
            return false;
        }
        User user = new User(username, password);
        if(userDao.create(user)) {
            return true;
        }
        return false;
    }

    public void markDone(Event e2 ){
        eventDao.delete(e2);
    }

    public List<Event> getUpcomingPublic() {
        return eventDao.getAllPublic();
    }

    public List<Event> getUpcomingPrivate(User user) {
        List<Event> z =eventDao.getAllPrivate(user);
	Date currentdate = new Date();
        for (Event ez : z) {
            if(ez.getDate().before(currentdate)) z.remove(ez);
        }
        return z;
    }




}
