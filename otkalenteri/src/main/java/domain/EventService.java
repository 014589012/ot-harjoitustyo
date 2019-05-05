package domain;

import dao.EventDao;
import dao.UserDao;
import java.text.ParseException;
import java.util.List;
import java.util.Date;

/**
 * Class offers a variety of backend methods for Otkalenteri.
 */
public class EventService {

    private final EventDao eventDao;
    private final UserDao userDao;
    private User loggedIn;

    /**
    * Constructor for EventService initializes eventDao and userDao.
    *
    * @param   eventDao   FileEventDao for the service.
    * @param   userDao   FileUserDao for the service.
    * 
    */
    public EventService(EventDao eventDao, UserDao userDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
    }
    
    /**
    * Method logs user in.
    *
    * @param   username   Name of user.
    * @param   password   Password of user.
    * 
    * @return Boolean value for whether login worked.
    */
    public boolean login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return false;
        }
        if(!user.getPassword().equals(password)) return false;
        loggedIn = user;
        return true;
    }

    /**
    * Method gets user currently logged in.
    *
    * @return Logged user or null of no one is logged in.
    */
    public User getLoggedUser() {
        return loggedIn;
    }

    /**
    * Method creates a new event.
    *
    * @param   text   Name of the event.
    * @param   dd   Date as string (in format yyyy-MM-dd).
    * @param   prive   Boolean value for whether the event is private.
    * 
    * @return Boolean value for whether event creation worked.
    */
    public boolean createEvent(String text, String dd, boolean prive) throws ParseException {
        if(dd.isEmpty()) return false;
        Event event = new Event(text, dd, prive, loggedIn);
        boolean x = true;
        try {
            x = eventDao.create(event);
        } catch (Exception ex) {
            return false;
        }
        return x;
    }

    /**
    * Method sets currently logged in user to null.
    *
    */
    public void logout() {
        loggedIn=null;
    }

    /**
    * Method creates a new user.
    *
    * @param   username   Name of user.
    * @param   password   Password of user.
    * 
    * @return Boolean value for whether user creation worked.
    */
    public boolean createUser(String username, String password) throws Exception {
        if (userDao.findByUsername(username) != null) {
            return false;
        }
        User user = new User(username, password);
        if(userDao.create(user)) {
            return true;
        }
        return false;
    }

    /**
    * Method deletes an event.
    *
    * @param   e2   Event to be deleted.
    */
    public void markDone(Event e2 ) throws Exception{
        eventDao.delete(e2);
    }

    /**
    * Method lists upcoming public events.
    * 
    * @return List of all upcoming public events.
    */
    public List<Event> getUpcomingPublic() throws Exception {
        List<Event> z =eventDao.getAllPublic();
	Date currentdate = new Date(System.currentTimeMillis()-24*60*60*1000);
        for (Event ez : z) {
            if(currentdate.after(ez.getDate())) eventDao.delete(ez);
        }
        return eventDao.getAllPublic();
    }

    /**
    * Method lists upcoming private events.
    * 
    * @param   user   User whose private events are listed.
    * 
    * @return List of all upcoming private events for specific user.
    */
    public List<Event> getUpcomingPrivate(User user) throws Exception {
        List<Event> z =eventDao.getAllPrivate(user);
	Date currentdate = new Date(System.currentTimeMillis()-24*60*60*1000);
        for (Event ez : z) {
            if(!currentdate.before(ez.getDate())) eventDao.delete(ez);
        }
        return eventDao.getAllPrivate(user);
    }
    
    public void addDescriptionToEvent(String des, Event ev) throws Exception{
        eventDao.addDescription(des,ev);
    }




}
